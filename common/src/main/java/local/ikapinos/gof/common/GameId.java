package local.ikapinos.gof.common;

/**
 * To avoid confusion with primitives and wrappers we need separate
 * class even if it has only single primitive in it
 */
public class GameId
{
  public final int id;

  public GameId(int id)
  {
    this.id = id;
  }

  public int getId()
  {
    return id;
  }
}
