package local.ikapinos.gof.player;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import local.ikapinos.gof.common.CommonProperties;
import local.ikapinos.gof.common.KafkaConfiguration;

@SpringBootApplication
@Import({ KafkaConfiguration.class, CommonProperties.class })
public class PlayerApplication
{
    public static void main(String[] args )
    {
      SpringApplication.run(PlayerApplication.class, args);
    }
    
    @Bean
    public Random random()
    {
      return new Random(); // Allows testability
    }
}
