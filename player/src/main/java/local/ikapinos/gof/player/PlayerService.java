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
  
  @Value("${gof.peer-service-name}")
  private String peerServiceName;

  @Value("${gof.max-number: 100}")
  private int maxNumber;
  
  @Value("${gof.kafka.events-topic}") 
  private String eventsTopic;
  
  @Autowired
  private Random random;
  
  @Autowired
  private KafkaTemplate<Integer, AbstractGameEvent> kafkaTemplate;

  @KafkaListener(topics = "${gof.kafka.events-topic}")
  public void handleEvent(ConsumerRecord<Integer, AbstractGameEvent> record) 
  {
    logger.info("Consumed: {}", record);
    
    AbstractGameEvent event = record.value();
    
    if (!event.getDestination().equals(serviceName))
    {
      return; // Ignore own messages
    }
    
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
      // We want generate number in interval [2 .. maxNumber] to avoid trivial cases
      number = random.nextInt(maxNumber - NEXT_RANDOM_OFFSET) + NEXT_RANDOM_OFFSET;
    }
        
    AbstractGameEvent event = new ContinueGameEvent(startGameEvent.getGameId(),
                                                    serviceName,
                                                    peerServiceName, 
                                                    null, // First move
                                                    number);   
    fireEvent(event);
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
      event = new EndGameEvent(continueGameEvent.getGameId(), 
                               serviceName,
                               peerServiceName,
                               added);
    }
    else
    {
      event = new ContinueGameEvent(continueGameEvent.getGameId(), 
                                    serviceName,
                                    peerServiceName,
                                    added, 
                                    newNumber);
    }
    
    fireEvent(event);
  }
  
  private void fireEvent(AbstractGameEvent event)
  {
    int key = event.getGameId();
    kafkaTemplate.send(eventsTopic,
                       key, // We need ordered processing within Game
                       event);

    logger.info("Produced: topic={}, key={}, message={}", eventsTopic, key, event);
  }
}
