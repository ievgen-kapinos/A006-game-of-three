package local.ikapinos.gof.player;

import java.util.Random;

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
  
  @Value("${gof.max-number: 100}")
  private int maxNumber;
  
  @Autowired
  private Random random;
  
  @Value("${gof.kafka.peer-ingress-topic}") 
  private String peerIngressTopic;
  
  @Autowired
  private KafkaTemplate<String, AbstractGameEvent> kafkaTemplate;

  @KafkaListener(topics = "${gof.kafka.ingress-topic}")
  public void handleIngressEvent(AbstractGameEvent gameEvent) 
  {
    logger.info("Consumed: {}", gameEvent);
    
    if (gameEvent instanceof StartGameEvent) // Java 8. Need explicit casting
    {
      startGame((StartGameEvent) gameEvent);
    }
    else if (gameEvent instanceof ContinueGameEvent)
    {
      continueGame((ContinueGameEvent) gameEvent);
    }
    else if (gameEvent instanceof EndGameEvent)
    {
      // Nothing to do. To be handled on control-panel
    }
    else
    {
      new IllegalArgumentException("Unknown game event: " + gameEvent);
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
        
    AbstractGameEvent message = new ContinueGameEvent(startGameEvent.getGameId(), 
                                                      null, // First move
                                                      number);   
    producedPeerEvent(message);
  }
  
  private void continueGame(ContinueGameEvent continueGameEvent)
  {
    int number = continueGameEvent.getNumber();
    
    // Selects correct number to be added {-1, 0, 1}
    // To have result divisible by 3 without remainder   
    int added = 1 - (number + 1) % 3;  
    
    int newNumber = (number + added) / 3;
    
    AbstractGameEvent message;
    if (newNumber == 1)
    { 
      // Game ended 
      message = new EndGameEvent(continueGameEvent.getGameId(),
                                 added);
    }
    else
    {
      message = new ContinueGameEvent(continueGameEvent.getGameId(), 
                                      added,
                                      newNumber);
    }
    
    producedPeerEvent(message);
  }
  
  private void producedPeerEvent(AbstractGameEvent message)
  {
    kafkaTemplate.send(peerIngressTopic, 
                       null, // No need to key since single partition is used 
                       message);

    logger.info("Produced: topic={}, message={}", peerIngressTopic, message); 
  }
}
