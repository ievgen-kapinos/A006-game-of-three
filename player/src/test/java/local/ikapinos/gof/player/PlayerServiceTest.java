package local.ikapinos.gof.player;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import local.ikapinos.gof.common.event.AbstractGameEvent;
import local.ikapinos.gof.common.event.ContinueGameEvent;
import local.ikapinos.gof.common.event.EndGameEvent;
import local.ikapinos.gof.common.event.StartGameEvent;

/**
 * UTests sample
 */
@SpringBootTest 
@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest
{
  @Value("${gof.service-name}") 
  private String serviceName;
  
  @Value("${gof.peer-service-name}") 
  private String peerServiceName;
  
  @Value("${gof.kafka-events-topic}") 
  private String eventsTopic;
  
  private final int gameId = 888;
  
  @MockBean
  private Random random;

  @MockBean
  private KafkaTemplate<Integer, AbstractGameEvent> kafkaTemplate;
  
  @Autowired
  private PlayerService player;
    
  @Test
  public void testStartNewGameWithAutomaticInput()
  {
    when(random.nextInt(anyInt())).thenReturn(77 - PlayerService.NEXT_RANDOM_OFFSET);
    
    player.handleEvent(new StartGameEvent(gameId, 
                                          peerServiceName, 
                                          serviceName, 
                                          null));
    
    verify(kafkaTemplate).send(eq(eventsTopic), 
                               eq(gameId), 
                               eq(new ContinueGameEvent(gameId, 
                                                        serviceName,
                                                        peerServiceName, 
                                                        null, 
                                                        77)));
  }

  @Test
  public void testStartNewGameWithManualInput()
  {    
    player.handleEvent(new StartGameEvent(gameId, 
                                          peerServiceName, 
                                          serviceName,
                                          56));
    
    verify(kafkaTemplate).send(eq(eventsTopic), 
                               eq(gameId), 
                               eq(new ContinueGameEvent(gameId, 
                                                        serviceName,
                                                        peerServiceName, 
                                                        null, 
                                                        56)));
  }

  @Test
  public void testContinueGameAddOne()
  {
    player.handleEvent(new ContinueGameEvent(gameId, 
                                             peerServiceName, 
                                             serviceName,
                                             null, 
                                             56));
    
    verify(kafkaTemplate).send(eq(eventsTopic), 
                               eq(gameId), 
                               eq(new ContinueGameEvent(gameId, 
                                                        serviceName,
                                                        peerServiceName, 
                                                        1, 
                                                        19))); // (56+1)/3 = 19
  }

  @Test
  public void testContinueGameMinusOne()
  {
    player.handleEvent(new ContinueGameEvent(gameId, 
                                             peerServiceName, 
                                             serviceName,
                                             1, 
                                             19));
    
    verify(kafkaTemplate).send(eq(eventsTopic), 
                               eq(gameId), 
                               eq(new ContinueGameEvent(gameId, 
                                                        serviceName,
                                                        peerServiceName, 
                                                        -1, 
                                                        6))); // (19-1)/3 = 6
  }
  
  @Test
  public void testContinueGamePlusZero()
  {
    player.handleEvent(new ContinueGameEvent(gameId, 
                                             peerServiceName, 
                                             serviceName,
                                             -1, 
                                             6));
    
    verify(kafkaTemplate).send(eq(eventsTopic), 
                               eq(gameId), 
                               eq(new ContinueGameEvent(gameId, 
                                                        serviceName,
                                                        peerServiceName, 
                                                        0, 
                                                        2))); // (6+0)/3 = 2
  }
  
  @Test
  public void testContinueGameAndWin()
  {
    player.handleEvent(new ContinueGameEvent(gameId, 
                                             peerServiceName, 
                                             serviceName,
                                             0, 
                                             3));
    
    verify(kafkaTemplate).send(eq(eventsTopic), 
                               eq(gameId), 
                               eq(new EndGameEvent(gameId, 
                                                   serviceName,
                                                   peerServiceName, 
                                                   0))); // (2+1)/3 = 1

  }
}
