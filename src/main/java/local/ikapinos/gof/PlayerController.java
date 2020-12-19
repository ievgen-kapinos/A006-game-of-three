package local.ikapinos.gof;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController
{
  @Autowired
  private Environment environment;
  
  @GetMapping("/PlayerController")
  public String index() 
  {
    String activeProfile = environment.getActiveProfiles()[0]; // TODO danger zone
    
    return "Greetings from Spring Boot!<br>This is " + activeProfile;
  }
}
