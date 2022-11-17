package team.caltech.olmago.customer.service.proxy;

import reactor.core.publisher.Mono;

public interface SwingProxy {
  Mono<Void> linkMobilePhoneAndOlmagoCustomer(ReqRelMobilePhoneAndOlmagoCustDto dto);
  Mono<Void> unlinkMobilePhoneAndOlmagoCustomer(ReqRelMobilePhoneAndOlmagoCustDto dto);
}
