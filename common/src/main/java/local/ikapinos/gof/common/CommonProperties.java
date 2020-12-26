package local.ikapinos.gof.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Need to make it 'component' for refer from @KafkaListener
// https://stackoverflow.com/questions/53999391/how-to-access-configurationproperties-bean-by-its-name-from-xml-configuration
@Component("commonProperties")
@ConfigurationProperties("gof")
public class CommonProperties
{
  private String serviceName;  

  private int maxNumber = 100;
  
  private String kafkaEventsTopic = "gof-events";

  public String getServiceName()
  {
    return serviceName;
  }

  public void setServiceName(String serviceName)
  {
    this.serviceName = serviceName;
  }

  public int getMaxNumber()
  {
    return maxNumber;
  }

  public void setMaxNumber(int maxNumber)
  {
    this.maxNumber = maxNumber;
  }

  public String getKafkaEventsTopic()
  {
    return kafkaEventsTopic;
  }

  public void setKafkaEventsTopic(String kafkaEventsTopic)
  {
    this.kafkaEventsTopic = kafkaEventsTopic;
  }
}
