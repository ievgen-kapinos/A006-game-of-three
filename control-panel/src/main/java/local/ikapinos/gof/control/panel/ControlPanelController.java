package local.ikapinos.gof.control.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import local.ikapinos.gof.common.AbstractGameEvent;
import local.ikapinos.gof.common.StartGameEvent;

@Controller
public class ControlPanelController
{
  private static final Logger logger = LoggerFactory.getLogger(ControlPanelController.class); 
  
  @Value("${gof.max-number: 100}")
  private int maxNumber;
  
  @Value("${gof.kafka.player1-ingress-topic}") 
  private String player1IngressTopic;
  
  @Value("${gof.kafka.player2-ingress-topic}") 
  private String player2IngressTopic;
  
  @Autowired
  private KafkaTemplate<String, AbstractGameEvent> kafkaTemplate;
  
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
    
    String playerIngressTopic;
    if (playerId == 1)
    {
      playerIngressTopic = player1IngressTopic;
    }
    else if (playerId == 2)
    {
      playerIngressTopic = player1IngressTopic;
    }
    else 
    {
      throw new IllegalArgumentException("Player Id should be 1 or 2, but got " + playerId);
    }
    
    AbstractGameEvent message = new StartGameEvent(gameId, number);
    
    kafkaTemplate.send(playerIngressTopic, 
                       null, // No need to key since single partition is used 
                       message);
    
    logger.info("Produced: topic={}, message={}", playerIngressTopic, message);
  }
}
