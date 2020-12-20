package local.ikapinos.gof.control.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ControlPanelController
{
  Logger logger = LoggerFactory.getLogger(ControlPanelController.class); 
  
  @Value("${gof.max-manual-number: 100}")
  private int maxManualNumber;
  
  @GetMapping("/")
  public String index( Model model ) 
  {
    model.addAttribute("maxManualNumber", maxManualNumber);
    return "index";
  }
  
  @PostMapping("start-game")
  @ResponseBody
  public void startGame(int gameId,
                        int playerId,
                        Integer number) 
  {
    logger.info("startGame: gameId={}, playerId={}, number={}", gameId, playerId, number);
  }
}
