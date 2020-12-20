package local.ikapinos.gof.player;

import java.util.Map;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

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
      return new Random(); // Allows testability
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
    
    @Bean
    public KafkaTemplate<String, AbstractGameEvent> kafkaTemplate(KafkaProperties properties)
    {
      Map<String, Object> props = properties.buildProducerProperties();

      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

      return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }
}

