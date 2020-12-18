package local.kapinos.ievgen;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController
{
  @RequestMapping
  public String index() 
  {
    return "Greetings from Spring Boot!";
  }
}
