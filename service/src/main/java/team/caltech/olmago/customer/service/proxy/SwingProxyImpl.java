package team.caltech.olmago.customer.service.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class SwingProxyImpl implements SwingProxy {
  private final WebClient webClient;
  
  @Autowired
  public SwingProxyImpl(WebClient webClient) {
    this.webClient = webClient;
  }
  
  @Override
  public Mono<Void> linkMobilePhoneAndOlmagoCustomer(ReqRelMobilePhoneAndOlmagoCustDto dto) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/mobile-phones/{svc-mgmt-num}/linked-olmago-customer")
            .build(dto.getSvcMgmtNum()))
        .body(BodyInserters.fromValue(dto))
        .retrieve()
        .bodyToMono(Void.class);
  }
  
  @Override
  public Mono<Void> unlinkMobilePhoneAndOlmagoCustomer(ReqRelMobilePhoneAndOlmagoCustDto dto) {
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/mobile-phones/{svc-mgmt-num}/linked-olmago-customer/{olmago-customer-id}")
            .build(dto.getSvcMgmtNum(), dto.getOlmagoCustomerId()))
        .body(BodyInserters.fromValue(dto))
        .retrieve()
        .bodyToMono(Void.class);
  }
}
