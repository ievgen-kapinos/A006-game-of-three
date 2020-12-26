package local.ikapinos.gof.common;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import local.ikapinos.gof.common.event.AbstractGameEvent;
import local.ikapinos.gof.common.event.StartGameEvent;

@Configuration
@EnableKafka
public class KafkaConfiguration
{
  @Bean
  public KafkaTemplate<Integer, AbstractGameEvent> kafkaTemplate(KafkaProperties properties)
  {
    Map<String, Object> props = properties.buildProducerProperties();

    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class); // gameId
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);  // events

    return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
  }

  @Bean
  public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory(KafkaProperties properties)
  {
    Map<String, Object> props = properties.buildConsumerProperties();

    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    
    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, IntegerDeserializer.class);          // gameId
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName()); // events
    props.put(JsonDeserializer.TRUSTED_PACKAGES, AbstractGameEvent.class.getPackage().getName());

    ConcurrentKafkaListenerContainerFactory<String, StartGameEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
    factory.setErrorHandler(new LoggingErrorHandler());

    return factory;
  }
}
