
package local.ikapinos.gof.control.panel;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import local.ikapinos.gof.common.AbstractGameEvent;

@SpringBootApplication
public class ControlPanelApplication
{
  public static void main(String[] args)
  {
    SpringApplication.run(ControlPanelApplication.class, args);
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
