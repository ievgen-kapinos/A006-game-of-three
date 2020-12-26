package local.ikapinos.gof.control.panel;

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

import local.ikapinos.gof.common.CommonProperties;
import local.ikapinos.gof.common.event.AbstractGameEvent;
import local.ikapinos.gof.common.event.StartGameEvent;

@Controller
public class ControlPanelController
{
  private static final Logger logger = LoggerFactory.getLogger(ControlPanelController.class); 
  
  @Autowired
  private CommonProperties commonProperties;
  
  @Value("${gof.player1-service-name}")
  private String player1ServiceName;  
  
  @Value("${gof.player2-service-name}")
  private String player2ServiceName;
  
  @Autowired
  private KafkaTemplate<Integer, AbstractGameEvent> kafkaTemplate;
  
  @Autowired
  private SimpMessagingTemplate simpTemplate; 
  
  @GetMapping("/")
  public String index( Model model ) 
  {
    model.addAttribute("maxNumber", commonProperties.getMaxNumber());
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
                                                 commonProperties.getServiceName(), // source
                                                 destination,
                                                 number);
    
    kafkaTemplate.send(commonProperties.getKafkaEventsTopic(), 
                       gameId, // We need ordered processing within Game
                       event);
    
    logger.info("Produced: {}", event);
  }
  
  @KafkaListener(topics = "#{commonProperties.kafkaEventsTopic}")
  public void handleEvent(AbstractGameEvent event) 
  {
    logger.info("Consumed: {}", event);
    
    simpTemplate.convertAndSend("/topic/" + event.getGameId(), event);
  }
}
