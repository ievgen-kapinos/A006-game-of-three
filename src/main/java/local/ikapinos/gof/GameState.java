package local.ikapinos.gof;

import java.util.Objects;
import java.util.UUID;

public class GameState
{
  private final UUID gameId;
  private final int number;
  
  public GameState(int number)
  {
    this(UUID.randomUUID(), number);
  }
  
  public GameState(GameState gameState, int number)
  {
    this(gameState.gameId, number);
  }
  
  private GameState(UUID gameId, int number)
  {
    Objects.requireNonNull(gameId);
    
    if (number <= 1)
    {
      throw new IllegalArgumentException("Game state number should be greater than 1");
    }
    
    this.gameId = gameId;
    this.number = number;
  }

  public UUID getGameToken()
  {
    return gameId;
  }

  public int getNumber()
  {
    return number;
  }
}
