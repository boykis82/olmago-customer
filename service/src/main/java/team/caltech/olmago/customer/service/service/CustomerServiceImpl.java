package team.caltech.olmago.customer.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.caltech.olmago.common.message.MessageEnvelope;
import team.caltech.olmago.customer.domain.*;
import team.caltech.olmago.customer.domain.event.CustomerEventBase;
import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneDto;
import team.caltech.olmago.customer.service.message.out.MessageStore;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
  private final CustomerRepository customerRepository;
  private final MobilePhoneRepository mobilePhoneRepository;
  
  private final MessageStore messageStore;
  private final ObjectMapper objectMapper;
  
  public static final String CUSTOMER_AGGREGATE_TYPE = "CUSTOMER";
  public static final String CUSTOMER_EVENT_BINDING = "customer-event-0";
  
  @Override
  public CustomerDto findById(long id) {
    return new CustomerDto(
        customerRepository.findById(id).orElseThrow(IllegalArgumentException::new)
    );
  }
  
  @Override
  @Transactional
  public CustomerDto createCustomer(CreateCustomerDto dto) {
    Customer customer = Customer.builder()
        .ci(UUID.randomUUID().toString())
        .name(dto.getName())
        .birthday(dto.getBirthday())
        .build();
    return new CustomerDto(customerRepository.save(customer));
  }
  
  @Override
  @Transactional
  public CustomerDto linkMobilePhone(long id, MobilePhoneDto dto) {
    Customer customer = customerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    if (customer.findActiveMobilePhone().isPresent()) {
      throw new IllegalStateException();
    }
    MobilePhone mobilePhone = createMobilePhone(dto);
    mobilePhoneRepository.save(mobilePhone);
    
    messageStore.saveMessage(
        wrapEvent(
            customer.linkMobilePhone(
                createCustomerMobilePhoneRelation(customer, mobilePhone)
            )
        )
    );
    return new CustomerDto(customer);
  }
  
  private MobilePhone createMobilePhone(MobilePhoneDto dto) {
    return MobilePhone.builder()
        .svcMgmtNum(dto.getSvcMgmtNum())
        .phoneNumber(dto.getPhoneNumber())
        .productName(dto.getProductName())
        .mobilePhonePricePlan(MobilePhonePricePlan.valueOf(dto.getMobilePhonePricePlan()))
        .dcTargetUzooPassProductCode(dto.getDcTargetUzooPassProductCode())
        .build();
  }
  
  private CustomerMobilePhoneRelationHistory createCustomerMobilePhoneRelation(Customer customer, MobilePhone mobilePhone) {
    return CustomerMobilePhoneRelationHistory.builder()
        .customer(customer)
        .mobilePhone(mobilePhone)
        .effStaDtm(LocalDateTime.now())
        .build();
  }
  
  @Override
  @Transactional
  public CustomerDto unlinkMobilePhone(long id, MobilePhoneDto dto) {
    LocalDateTime now = LocalDateTime.now();
    Customer customer = customerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    validateCustomerMobilePhone(customer, dto.getSvcMgmtNum());
    messageStore.saveMessage(
        wrapEvent(customer.unlinkMobilePhone(now))
    );
    
    return new CustomerDto(customer);
  }
  
  @Override
  @Transactional
  public CustomerDto changeMobilePhonePricePlan(long id, MobilePhoneDto dto) {
    LocalDateTime now = LocalDateTime.now();
    Customer customer = customerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    validateCustomerMobilePhone(customer, dto.getSvcMgmtNum());
    messageStore.saveMessage(
      wrapEvent(customer.changeMobilePhonePricePlan(MobilePhonePricePlan.valueOf(dto.getMobilePhonePricePlan()), dto.getProductName(), dto.getDcTargetUzooPassProductCode(), now))
    );
    return new CustomerDto(customer);
  }
  
  private void validateCustomerMobilePhone(Customer customer, long recvSvcMgmtNum) {
    long svcMgmtNum = customer.findActiveMobilePhone()
        .orElseThrow(IllegalStateException::new)
        .getMobilePhone()
        .getSvcMgmtNum();
    if (svcMgmtNum != recvSvcMgmtNum) {
      throw new IllegalArgumentException();
    }
  }
  
  private MessageEnvelope wrapEvent(CustomerEventBase e) {
    try {
      return MessageEnvelope.wrapEvent(
          CUSTOMER_AGGREGATE_TYPE,
          String.valueOf(e.getCustomerId()),
          CUSTOMER_EVENT_BINDING,
          e.getClass().getSimpleName(),
          objectMapper.writeValueAsString(e)
      );
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}
