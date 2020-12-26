package local.ikapinos.gof.control.panel;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import local.ikapinos.gof.common.event.AbstractGameEvent;
import local.ikapinos.gof.common.event.StartGameEvent;

@Controller
public class ControlPanelController
{
  private static final Logger logger = LoggerFactory.getLogger(ControlPanelController.class); 
  
  @Value("${gof.service-name}")
  private String serviceName;  
  
  @Value("${gof.player1-service-name}")
  private String player1ServiceName;  
  
  @Value("${gof.player2-service-name}")
  private String player2ServiceName;

  @Value("${gof.max-number: 100}")
  private int maxNumber;
  
  @Value("${gof.kafka.events-topic}") 
  private String eventsTopic;
  
  @Autowired
  private KafkaTemplate<Integer, AbstractGameEvent> kafkaTemplate;
  
  @Autowired
  private SimpMessagingTemplate simpTemplate; 
  
  @GetMapping("/")
  public String index( Model model ) 
  {
    model.addAttribute("maxNumber", maxNumber);
    return "index";
  }
  
  @PostMapping("start-game")
  @ResponseBody
  public void startGame(int playerId,
                        int gameId,
                        Integer number) 
  {
    logger.info("User request: start-game[playerId={}, gameId={}, number={}]", playerId, gameId, number);
    
    String destination;
    if (playerId == 1)
    {
      destination = player1ServiceName;
    }
    else if (playerId == 2)
    {
      destination = player2ServiceName;
    }
    else 
    {
      throw new IllegalArgumentException("Player Id should be 1 or 2, but got " + playerId);
    }
    
    AbstractGameEvent event = new StartGameEvent(gameId,
                                                 serviceName, // source
                                                 destination,
                                                 number);
    
    kafkaTemplate.send(eventsTopic, 
                       gameId, // We need ordered processing within Game
                       event);
    
    logger.info("Produced: topic={}, key={}, message={}", eventsTopic, gameId, event);
  }
  
  @KafkaListener(topics = {"${gof.kafka.events-topic}"})
  public void handleEvent(ConsumerRecord<Integer, AbstractGameEvent> record) 
  {
    logger.info("Consumed: {}", record);
    
    simpTemplate.convertAndSend("/topic/" + record.key(), record.value());
  }
}
