package local.ikapinos.gof;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController
{
  Logger logger = LoggerFactory.getLogger(UserController.class); 
  
  @PostMapping("start-game")
  @ResponseBody
  public void startGame(int gameId,
                        int playerId,
                        Integer number) 
  {
    logger.info("startGame: gameId={}, playerId={}, number={}", gameId, playerId, number);
  }
}