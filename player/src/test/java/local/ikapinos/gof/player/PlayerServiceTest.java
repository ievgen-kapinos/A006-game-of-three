package local.ikapinos.gof.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;

/**
 * UTests sample
 */
@SpringBootTest 
@EmbeddedKafka(topics = { "control-panel-ingress", "peer-ingress", "ingress" })
@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest
{
  @MockBean
  private Random random;

  @Autowired 
  private PlayerService player;
    
  @Test
  public void testStartNewGameWithManualInput()
  {
    GameState gameState = player.startNewGameWithManualInput(56);
    
    assertNotNull(gameState.getGameToken());
    assertEquals(56, gameState.getNumber()); 
  }
  
  @Test
  public void testStartNewGameWithAutomaticInput()
  {
    when(random.nextInt(anyInt())).thenReturn(77 - PlayerService.NEXT_RANDOM_OFFSET);
    
    GameState gameState = player.startNewGameWithAutomaticInput();
    
    assertNotNull(gameState.getGameToken());
    assertEquals(77, gameState.getNumber()); 
  }
  
  @Test
  public void testContinueGameAddOne()
  {
    Optional<GameState> gameStateOptional = player.continueGame(new GameState(56));
    
    assertTrue(gameStateOptional.isPresent());
    assertEquals(19, gameStateOptional.get().getNumber()); // (56+1)/3 = 19
  }

  @Test
  public void testContinueGameMinusOne()
  {
    Optional<GameState> gameStateOptional = player.continueGame(new GameState(19));
    
    assertTrue(gameStateOptional.isPresent());
    assertEquals(6, gameStateOptional.get().getNumber()); // (19-1)/3 = 6
  }
  
  @Test
  public void testContinueGamePlusZero()
  {
    Optional<GameState> gameStateOptional = player.continueGame(new GameState(6));
    
    assertTrue(gameStateOptional.isPresent());
    assertEquals(2, gameStateOptional.get().getNumber()); // (6+0)/3 = 2
  }
  
  @Test
  public void testContinueGameAndWin()
  {
    Optional<GameState> gameStateOptional = player.continueGame(new GameState(2));
    
    assertFalse(gameStateOptional.isPresent()); // (2+1)/3 = 1
  }
  
  @Test
  public void testContinueGameIllegalInput()
  {
    assertThrows(IllegalArgumentException.class, () -> 
    {
      player.continueGame(new GameState(1));
    });
  }
}