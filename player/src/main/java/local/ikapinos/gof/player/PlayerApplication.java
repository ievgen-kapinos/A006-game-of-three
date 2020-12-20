package local.ikapinos.gof.player;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class PlayerApplication
{
    public static void main(String[] args )
    {
      SpringApplication.run(PlayerApplication.class, args);
    }
    
    @Bean
    public Player player(@Value("${gof.max-automatic-number:100}") int maxAutomaticNumber)
    { 
      return new Player(new Random(), maxAutomaticNumber);
    }
}

