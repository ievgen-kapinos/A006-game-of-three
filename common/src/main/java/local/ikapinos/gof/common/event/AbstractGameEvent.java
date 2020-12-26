package local.ikapinos.gof.common.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public abstract class AbstractGameEvent
{
  private final int gameId;
  
  private final String source; // service name
  private final String destination;

  public AbstractGameEvent(int gameId,
                           String source, 
                           String destination)
  {
    this.gameId = gameId;

    this.source = source;
    this.destination = destination;
  }
  
  public int getGameId()
  {
    return gameId;
  }
  
  public String getSource()
  {
    return source;
  }

  public String getDestination()
  {
    return destination;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((destination == null) ? 0 : destination.hashCode());
    result = prime * result + gameId;
    result = prime * result + ((source == null) ? 0 : source.hashCode());
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
    if (destination == null)
    {
      if (other.destination != null)
        return false;
    }
    else if (!destination.equals(other.destination))
      return false;
    if (gameId != other.gameId)
      return false;
    if (source == null)
    {
      if (other.source != null)
        return false;
    }
    else if (!source.equals(other.source))
      return false;
    return true;
  }
  
  @Override
  public String toString()
  {
    return "gameId=" + gameId + 
        ", source=" + source + 
        ", destination=" + destination;
  }
}
