package local.ikapinos.gof.player;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.record.TimestampType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final int gameId = 888;
  @MockBean
  private Random random;

  @MockBean
  private KafkaTemplate<String, AbstractGameEvent> kafkaTemplate;
  
  @Autowired
  private PlayerService player;
    
  @Test
  public void testStartNewGameWithAutomaticInput()
  {
    when(random.nextInt(anyInt())).thenReturn(77 - PlayerService.NEXT_RANDOM_OFFSET);
    
    player.handleEvent(createConsumerRecord(new StartGameEvent(gameId, null)));
    
    verify(kafkaTemplate).send(eq("peer-ingress"), 
                               eq("player"), 
                               eq(new ContinueGameEvent(gameId, null, 77)));
  }

  @Test
  public void testStartNewGameWithManualInput()
  {    
    player.handleEvent(createConsumerRecord(new StartGameEvent(gameId, 56)));
    
    verify(kafkaTemplate).send(eq("peer-ingress"), 
                               eq("player"), 
                               eq(new ContinueGameEvent(gameId, null, 56)));
  }

  @Test
  public void testContinueGameAddOne()
  {
    player.handleEvent(createConsumerRecord(new ContinueGameEvent(gameId, null, 56)));
    
    verify(kafkaTemplate).send(eq("peer-ingress"), 
                               eq("player"), 
                               eq(new ContinueGameEvent(gameId, 1, 19))); // (56+1)/3 = 19
  }

  @Test
  public void testContinueGameMinusOne()
  {
    player.handleEvent(createConsumerRecord(new ContinueGameEvent(gameId, 1, 19)));
    
    verify(kafkaTemplate).send(eq("peer-ingress"), 
                               eq("player"), 
                               eq(new ContinueGameEvent(gameId, -1, 6))); // (19-1)/3 = 6
  }
  
  @Test
  public void testContinueGamePlusZero()
  {
    player.handleEvent(createConsumerRecord(new ContinueGameEvent(gameId, -1, 6)));
    
    verify(kafkaTemplate).send(eq("peer-ingress"), 
                               eq("player"), 
                               eq(new ContinueGameEvent(gameId, 0, 2))); // (6+0)/3 = 2
  }
  
  @Test
  public void testContinueGameAndWin()
  {
    player.handleEvent(createConsumerRecord(new ContinueGameEvent(gameId, 0, 3)));
    
    verify(kafkaTemplate).send(eq("peer-ingress"), 
                               eq("player"), 
                               eq(new EndGameEvent(gameId, 0))); // (2+1)/3 = 1

  }
 
  private ConsumerRecord<String, AbstractGameEvent> createConsumerRecord(AbstractGameEvent event)
  {
    return new ConsumerRecord<>(
        "ingress", 
        1,      // partition
        0,      // offset
        1000L,  // timestamp
        TimestampType.CREATE_TIME,
        555,    // checksum
        34,     // serializedKeySize
        43,     // serializedValueSize
        "peer-player", 
        event); // value
  }
}
