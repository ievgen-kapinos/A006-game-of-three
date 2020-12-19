package local.ikapinos.gof;

import java.util.Optional;
import java.util.Random;

public class Player
{
  // TODO add comment
  public static final int NEXT_RANDOM_OFFSET = 2; 
  
  private final int maxAutomaticInput;
  private final Random random;
  
  public Player(Random random, int maxAutomaticInput)
  {
    this.random = random;
    this.maxAutomaticInput = maxAutomaticInput;
  }
  
  public GameState startNewGameWithManualInput(int number)
  {
    return new GameState(number);
  }
  
  public GameState startNewGameWithAutomaticInput()
  {
    // we want generate number in interval [2 .. MAX_AUTOMATIC_INPUT]
    // to avoid trivial cases
    int number = random.nextInt(maxAutomaticInput - NEXT_RANDOM_OFFSET) + NEXT_RANDOM_OFFSET;
    
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
