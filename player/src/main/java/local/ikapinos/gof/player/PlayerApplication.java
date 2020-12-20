package local.ikapinos.gof.player;

import java.util.Map;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import local.ikapinos.gof.common.AbstractGameEvent;
import local.ikapinos.gof.common.StartGameEvent;

@SpringBootApplication
@EnableKafka
public class PlayerApplication
{
    public static void main(String[] args )
    {
      SpringApplication.run(PlayerApplication.class, args);
    }
    
    @Bean
    public Random random()
    {
      return new Random();
    }
    
    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory(KafkaProperties properties) {
      
      Map<String, Object> props = properties.buildConsumerProperties();

      props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
     
      ConcurrentKafkaListenerContainerFactory<String, StartGameEvent> factory = 
          new ConcurrentKafkaListenerContainerFactory<>();
      
      factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
          props, 
          new StringDeserializer(),  
          new JsonDeserializer<>(AbstractGameEvent.class)));
      
      return factory;
    }
}

