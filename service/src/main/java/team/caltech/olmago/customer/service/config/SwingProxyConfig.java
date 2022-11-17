package team.caltech.olmago.customer.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import team.caltech.olmago.customer.service.proxy.SwingProxy;
import team.caltech.olmago.customer.service.proxy.SwingProxyImpl;

@Configuration
public class SwingProxyConfig {
  @Bean
  public WebClient swingWebClient(WebClient.Builder webClientBuilder,
                                  @Value("${app.swing-service.host}") String host,
                                  @Value("${app.swing-service.port}") int port) {
    return webClientBuilder.baseUrl("http://" + host + ":" + port + "/swing/api/v1").build();
  }
  
  @Bean
  public SwingProxy swingProxy(WebClient swingWebClient) {
    return new SwingProxyImpl(swingWebClient);
  }
}
