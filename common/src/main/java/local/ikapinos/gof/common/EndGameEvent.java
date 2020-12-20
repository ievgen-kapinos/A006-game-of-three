package local.ikapinos.gof.common;

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
  public String toString()
  {
    return "EndGameEvent [" + super.toString() + 
        ", added=" + added + 
        "]";
  }
}
