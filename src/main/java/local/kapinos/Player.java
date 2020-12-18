package local.kapinos;

import java.util.Optional;
import java.util.Random;

public class Player
{
  private final int maxAutomatixInput;
  private final Random random;
  
  public Player(Random random, int maxAutomatixInput)
  {
    this.random = random;
    this.maxAutomatixInput = maxAutomatixInput;
  }
  
  public GameState startNewGameWithManualInput(int number)
  {
    return new GameState(number);
  }
  
  public GameState startNewGameWithAutomaticInput()
  {
    // we want generate number in interval [2 .. MAX_AUTOMATIC_INPUT]
    // to avoid trivial cases
    int number = random.nextInt(maxAutomatixInput - 2) + 2;
    
    return new GameState(number); 
  }
  
  public Optional<GameState> continueGame(GameState gameState)
  {
    int number = gameState.getNumber();
    
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
