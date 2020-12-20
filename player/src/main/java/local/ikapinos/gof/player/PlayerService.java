package local.ikapinos.gof.player;

import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import local.ikapinos.gof.common.event.AbstractGameEvent;
import local.ikapinos.gof.common.event.ContinueGameEvent;
import local.ikapinos.gof.common.event.EndGameEvent;
import local.ikapinos.gof.common.event.StartGameEvent;

@Service
public class PlayerService
{
  private static final Logger logger = LoggerFactory.getLogger(PlayerService.class); 
  
  public static final int NEXT_RANDOM_OFFSET = 2; 
  
  @Value("${gof.service-name}")
  private String serviceName;

  @Value("${gof.max-number: 100}")
  private int maxNumber;
  
  @Value("${gof.kafka.peer-ingress-topic}") 
  private String peerIngressTopic;
  
  @Autowired
  private Random random;
  
  @Autowired
  private KafkaTemplate<String, AbstractGameEvent> kafkaTemplate;

  @KafkaListener(topics = "${gof.kafka.ingress-topic}")
  public void handleEvent(ConsumerRecord<String, AbstractGameEvent> record) 
  {
    logger.info("Consumed: {}", record);
    
    AbstractGameEvent event = record.value();
    
    if (event instanceof StartGameEvent) // Java 8. Need explicit casting
    {
      startGame((StartGameEvent) event);
    }
    else if (event instanceof ContinueGameEvent)
    {
      continueGame((ContinueGameEvent) event);
    }
    else if (event instanceof EndGameEvent)
    {
      // Nothing to do. To be handled on control-panel
    }
    else
    {
      new IllegalArgumentException("Unknown game event: " + event);
    }
  }
  
  private void startGame(StartGameEvent startGameEvent)
  {
    Integer number = startGameEvent.getNumber();
    if (number == null)
    {
      // we want generate number in interval [2 .. maxNumber] to avoid trivial cases
      number = random.nextInt(maxNumber - NEXT_RANDOM_OFFSET) + NEXT_RANDOM_OFFSET;
    }
        
    AbstractGameEvent event = new ContinueGameEvent(startGameEvent.getGameId(), 
                                                    null, // First move
                                                    number);   
    producedPeerEvent(event);
  }
  
  private void continueGame(ContinueGameEvent continueGameEvent)
  {
    int number = continueGameEvent.getNumber();
    
    // Selects correct number to be added {-1, 0, 1}
    // To have result divisible by 3 without remainder   
    int added = 1 - (number + 1) % 3;  
    
    int newNumber = (number + added) / 3;
    
    AbstractGameEvent event;
    if (newNumber == 1)
    { 
      // Game ended 
      event = new EndGameEvent(continueGameEvent.getGameId(),
                                 added);
    }
    else
    {
      event = new ContinueGameEvent(continueGameEvent.getGameId(), 
                                      added,
                                      newNumber);
    }
    
    producedPeerEvent(event);
  }
  
  private void producedPeerEvent(AbstractGameEvent event)
  {
    kafkaTemplate.send(peerIngressTopic, 
                       serviceName,
                       event);

    logger.info("Produced: topic={}, key={}, message={}", peerIngressTopic, serviceName, event); 
  }
}
