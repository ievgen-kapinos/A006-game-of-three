package local.ikapinos.gof.common.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public abstract class AbstractGameEvent
{
  private final int gameId;

  public AbstractGameEvent(int gameId)
  {
    this.gameId = gameId;
  }
  
  public int getGameId()
  {
    return gameId;
  }
  
  @Override
  public String toString()
  {
    return "gameId=" + gameId;
  }
}
