package local.ikapinos.gof.common.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
public abstract class AbstractGameEvent
{
  private final String serviceName;
  private final int gameId;

  public AbstractGameEvent(String serviceName, 
                           int gameId)
  {
    this.serviceName = serviceName;
    this.gameId = gameId;
  }
  
  public String getServiceName()
  {
    return serviceName;
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
    result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
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
    if (serviceName == null)
    {
      if (other.serviceName != null)
        return false;
    }
    else if (!serviceName.equals(other.serviceName))
      return false;
    return true;
  }
  
  @Override
  public String toString()
  {
    return "serviceName=" + serviceName + 
        ", gameId=" + gameId;
  }
}
