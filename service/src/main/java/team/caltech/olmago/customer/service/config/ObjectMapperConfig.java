package team.caltech.olmago.customer.service.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
  public static final String DATE_FORMATTER = "yyyy-MM-dd";
  public static final String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    mapper.registerModule(javaTimeModule);
    
    return mapper;
  }
}
