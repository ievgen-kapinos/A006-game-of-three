package local.ikapinos.gof.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("continue-game")
public class ContinueGameEvent extends AbstractGameEvent
{
  private final Integer added; // Null, if first move
  private final int number;

  @JsonCreator
  public ContinueGameEvent(@JsonProperty("serviceName") String serviceName,
                           @JsonProperty("gameId") int gameId,
                           @JsonProperty("added") Integer added,
                           @JsonProperty("number") int number)
  {
    super(serviceName, gameId);
    
    this.added = added;
    this.number = number;
  }
  
  public Integer getAdded()
  {
    return added;
  }
  
  public int getNumber()
  {
    return number;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((added == null) ? 0 : added.hashCode());
    result = prime * result + number;
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    ContinueGameEvent other = (ContinueGameEvent)obj;
    if (added == null)
    {
      if (other.added != null)
        return false;
    }
    else if (!added.equals(other.added))
      return false;
    if (number != other.number)
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "ContinueGameEvent [" + super.toString() + 
        ", added=" + added + 
        ", number=" + number + 
        "]";
  }
}
