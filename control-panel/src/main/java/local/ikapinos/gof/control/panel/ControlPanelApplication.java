
package local.ikapinos.gof.control.panel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import local.ikapinos.gof.common.KafkaConfiguration;

@SpringBootApplication
@Import(KafkaConfiguration.class)
@EnableWebSocketMessageBroker
public class ControlPanelApplication implements WebSocketMessageBrokerConfigurer
{
  public static void main(String[] args)
  {
    SpringApplication.run(ControlPanelApplication.class, args);
  }
  
  // WebSocketMessageBrokerConfigurer with STOMP
  
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) 
  {
    config.enableSimpleBroker("/topic");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) 
  {
    registry.addEndpoint("/player-events").withSockJS();
  }
}
