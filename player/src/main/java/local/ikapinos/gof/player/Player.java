package local.ikapinos.gof.player;

import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class Player
{
  @Autowired
  private KafkaTemplate<Integer, Integer> template;
  
  // TODO add comment
  public static final int NEXT_RANDOM_OFFSET = 2; 
  
  private final int maxAutomaticNumber;
  private final Random random;
  
  public Player(Random random, int maxAutomaticNumber)
  {
    this.random = random;
    this.maxAutomaticNumber = maxAutomaticNumber;
  }
  
  @PostConstruct
  private void postConstruct() 
  {
    template.send("topic1", 12, 21);
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
