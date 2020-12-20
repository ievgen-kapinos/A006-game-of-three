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
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + gameId;
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractGameEvent other = (AbstractGameEvent)obj;
    if (gameId != other.gameId)
      return false;
    return true;
  }
  
  @Override
  public String toString()
  {
    return "gameId=" + gameId;
  }
}
