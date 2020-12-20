package local.ikapinos.gof.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("continue-game")
public class ContinueGameEvent extends AbstractGameEvent
{
  private final Integer added; // Null, if first move
  private final int number;

  @JsonCreator
  public ContinueGameEvent(@JsonProperty("gameId") int gameId,
                           @JsonProperty("added") Integer added,
                           @JsonProperty("number") int number)
  {
    super(gameId);
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
  public String toString()
  {
    return "ContinueGameEvent [" + super.toString() + 
        ", added=" + added + 
        ", number=" + number + 
        "]";
  }
}
