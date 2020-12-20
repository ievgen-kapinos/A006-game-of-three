package local.ikapinos.gof.common.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("end-game")
public class EndGameEvent extends AbstractGameEvent
{
  private final int added;

  @JsonCreator
  public EndGameEvent(@JsonProperty("gameId") int gameId,
                      @JsonProperty("added") int added)
  {
    super(gameId);
    this.added = added;
  }
  
  public int getAdded()
  {
    return added;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + added;
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
    EndGameEvent other = (EndGameEvent)obj;
    if (added != other.added)
      return false;
    return true;
  }

  @Override
  public String toString()
  {
    return "EndGameEvent [" + super.toString() + 
        ", added=" + added + 
        "]";
  }
}
