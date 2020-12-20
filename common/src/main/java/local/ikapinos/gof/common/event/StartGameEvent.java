package local.ikapinos.gof.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("start-game")
public class StartGameEvent extends AbstractGameEvent
{
  private final Integer number; // Null, if to be auto-generated bu Player

  @JsonCreator
  public StartGameEvent(@JsonProperty("gameId") int gameId,
                        @JsonProperty("number") Integer number)
  {
    super(gameId);
    
    if (number != null && number < 2)
    {
      throw new IllegalArgumentException("Inital number should be greater or equal to 2");
    }
    this.number = number;
  }
  
  public Integer getNumber()
  {
    return number;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((number == null) ? 0 : number.hashCode());
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
    StartGameEvent other = (StartGameEvent)obj;
    if (number == null)
    {
      if (other.number != null)
        return false;
    }
    else if (!number.equals(other.number))
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "StartGameEvent [" + super.toString() + 
        ", number=" + number + 
        "]";
  }
}