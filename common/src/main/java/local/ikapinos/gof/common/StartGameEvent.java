package local.ikapinos.gof.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("start-game")
public class StartGameEvent extends AbstractGameEvent
{
  private final Integer number; // Null, if to be auto-generated

  @JsonCreator
  public StartGameEvent(@JsonProperty("gameId") int gameId,
                        @JsonProperty("number") Integer number)
  {
    super(gameId);
    this.number = number;
  }
  
  public Integer getNumber()
  {
    return number;
  }

  @Override
  public String toString()
  {
    return "StartGameEvent [" + super.toString() + 
        ", number=" + number + 
        "]";
  }
}
