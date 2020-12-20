package local.ikapinos.gof.player;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import local.ikapinos.gof.common.AbstractGameEvent;

@Service
public class PlayerService
{
  private static final Logger logger = LoggerFactory.getLogger(PlayerService.class); 
  
  public static final int NEXT_RANDOM_OFFSET = 2; 
  
  private final int maxAutomaticNumber;
  private final Random random;
  
  @Autowired
  public PlayerService(Random random, 
                       @Value("${gof.max-automatic-number:100}") int maxAutomaticNumber)
  {
    this.random = random;
    this.maxAutomaticNumber = maxAutomaticNumber;
  }
  
  @KafkaListener(topics = "${gof.kafka.ingress-topic}")
  public void handleIngressEvent(AbstractGameEvent gameEvent) 
  {
    logger.info("Recv: {}", gameEvent);
  }
  
  public GameState startNewGameWithManualInput(int number)
  {
    return new GameState(number);
  }
  
  public GameState startNewGameWithAutomaticInput()
  {
    // we want generate number in interval [2 .. MAX_AUTOMATIC_INPUT]
    // to avoid trivial cases
    int number = random.nextInt(maxAutomaticNumber - NEXT_RANDOM_OFFSET) + NEXT_RANDOM_OFFSET;
    
    return new GameState(number); 
  }
  
  public Optional<GameState> continueGame(GameState gameState)
  {
    int number = gameState.getNumber();
    
    // Selects correct number to be added {-1, 0, 1}
    // To have result divisible by 3 without remainder   
    int added = 1 - (number + 1) % 3;  
    
    int newNumber = (number + added) / 3;
    
    if (newNumber == 1)
    {
      return Optional.empty(); // Game ended
    }
    else
    {
      return Optional.of(new GameState(gameState, newNumber)); // Game continuing
    }
  }
}
