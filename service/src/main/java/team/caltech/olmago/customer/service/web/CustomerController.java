package team.caltech.olmago.customer.service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.caltech.olmago.common.message.MessageEnvelope;
import team.caltech.olmago.common.message.MessageEnvelopeRepository;
import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneDto;
import team.caltech.olmago.customer.service.service.CustomerService;

import java.util.List;

@RequiredArgsConstructor
@SuppressWarnings("unused")
@RestController
@RequestMapping("/olmago/api/v1/customers")
public class CustomerController {
  private final MessageEnvelopeRepository messageEnvelopeRepository;
  private final CustomerService customerService;
  
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
    return ResponseEntity.ok(customerService.linkMobilePhone(id, dto));
  }
  
  @PutMapping("/{id}/unlinkMobilePhone")
  public ResponseEntity<CustomerDto> unlinkMobilePhone(@PathVariable("id") long id, @RequestBody MobilePhoneDto dto) {
    return ResponseEntity.ok(customerService.unlinkMobilePhone(id, dto));
  }
  
  @PutMapping("/{id}/changeMobilePhonePricePlan")
  public ResponseEntity<CustomerDto> changeMobilePhonePrlcePlan(@PathVariable("id") long id, @RequestBody MobilePhoneDto dto) {
    return ResponseEntity.ok(customerService.changeMobilePhonePricePlan(id, dto));
  }

  @GetMapping("/messages")
  public ResponseEntity<List<MessageEnvelope>> findAllMessageEnvelope() {
    return ResponseEntity.ok(messageEnvelopeRepository.findAll());
  }

}
