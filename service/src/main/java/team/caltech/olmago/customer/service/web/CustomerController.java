package team.caltech.olmago.customer.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.caltech.olmago.common.message.MessageEnvelope;
import team.caltech.olmago.common.message.MessageEnvelopeRepository;
import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneDto;
import team.caltech.olmago.customer.service.proxy.ReqRelMobilePhoneAndOlmagoCustDto;
import team.caltech.olmago.customer.service.proxy.SwingProxy;
import team.caltech.olmago.customer.service.service.CustomerService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("unused")
@RestController
@RequestMapping("/olmago/api/v1/customers")
public class CustomerController {
  private final MessageEnvelopeRepository messageEnvelopeRepository;
  private final CustomerService customerService;
  private final SwingProxy swingProxy;
  
  @GetMapping("/{id}")
  public ResponseEntity<CustomerDto> findById(@PathVariable("id") long id) {
    return ResponseEntity.ok(customerService.findById(id));
  }
  
  @PostMapping
  public ResponseEntity<CustomerDto> createCustomer(@RequestBody CreateCustomerDto dto) {
    return ResponseEntity.ok(customerService.createCustomer(dto));
  }
  
  @PutMapping("/{id}/linkMobilePhone")
  public ResponseEntity<CustomerDto> linkMobilePhone(@PathVariable("id") long id, @RequestBody MobilePhoneDto dto) {
    CustomerDto response = customerService.linkMobilePhone(id, dto);
    swingProxy.linkMobilePhoneAndOlmagoCustomer(toReqRelMobilePhoneAndOlmagoCustDto(response)).block();
    return ResponseEntity.ok(response);
  }
  
  @PutMapping("/{id}/unlinkMobilePhone")
  public ResponseEntity<CustomerDto> unlinkMobilePhone(@PathVariable("id") long id, @RequestBody MobilePhoneDto dto) {
    CustomerDto response = customerService.unlinkMobilePhone(id, dto);
    swingProxy.unlinkMobilePhoneAndOlmagoCustomer(toReqRelMobilePhoneAndOlmagoCustDto(response)).block();
    return ResponseEntity.ok(response);
  }
  
  @PutMapping("/{id}/changeMobilePhonePricePlan")
  public ResponseEntity<CustomerDto> changeMobilePhonePricePlan(@PathVariable("id") long id, @RequestBody MobilePhoneDto dto) {
    return ResponseEntity.ok(customerService.changeMobilePhonePricePlan(id, dto));
  }

  @GetMapping("/messages")
  public ResponseEntity<List<MessageEnvelope>> findAllMessageEnvelope() {
    return ResponseEntity.ok(messageEnvelopeRepository.findAll());
  }

  private ReqRelMobilePhoneAndOlmagoCustDto toReqRelMobilePhoneAndOlmagoCustDto(CustomerDto customerDto) {
    return ReqRelMobilePhoneAndOlmagoCustDto.builder()
        .olmagoCustomerId(customerDto.getId())
        .svcMgmtNum(customerDto.getSvcMgmtNum())
        .eventDateTime(LocalDateTime.now())
        .build();
  }
}
