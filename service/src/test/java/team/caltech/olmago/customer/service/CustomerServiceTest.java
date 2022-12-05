package team.caltech.olmago.customer.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import team.caltech.olmago.common.message.MessageEnvelope;
import team.caltech.olmago.common.message.MessageEnvelopeRepository;
import team.caltech.olmago.customer.domain.*;
import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneLinkDto;
import team.caltech.olmago.customer.service.service.CustomerService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerServiceTest {
  @Autowired
  CustomerService customerService;
  
  @Autowired
  CustomerRepository customerRepository;
  
  @Autowired
  MobilePhoneRepository mobilePhoneRepository;
  
  @Autowired
  CustomerMobilePhoneRelationHistoryRepository customerMobilePhoneRelationHistoryRepository;
  
  @Autowired
  MessageEnvelopeRepository messageEnvelopeRepository;
  
  CustomerDto customerDto;
  
  @Before
  public void setup() {
    CreateCustomerDto req = CreateCustomerDto.builder().name("강인수").birthday(LocalDate.of(1982,1,1)).build();
    customerDto = customerService.createCustomer(req);
  }
  
  @After
  public void teardown() {
    customerMobilePhoneRelationHistoryRepository.deleteAll();
    customerRepository.deleteAll();
    mobilePhoneRepository.deleteAll();
    messageEnvelopeRepository.deleteAll();
  }
  
  @Test
  public void givenCustomerAndMobilePhone_whenLinkMobilePhone_thenSuccess() {
    // given
    // when
    CustomerDto result = customerService.linkMobilePhone(customerDto.getId(), createMobilePhoneLinkDto());
    
    // then
    assertThat(result.getMobilePhonePricePlan()).isEqualTo("PLATINUM");
    assertThat(result.getSvcMgmtNum()).isEqualTo(7000000001L);
    assertThat(result.getDcTargetUzooPassProductCodes().get(0)).isEqualTo("NMO0000001");
  
    List<MessageEnvelope> msgs = findAllMessageEnvelopesOrderById();
    assertThat(msgs).hasSize(1);
    assertThat(msgs.get(0).getMessageName()).isEqualTo("mobilePhoneServiceLinkedEvent");
  }
  
  @Test
  public void givenCustomerAndMobilePhone_whenUnlinkMobilePhone_thenSuccess() {
    // given
    customerService.linkMobilePhone(customerDto.getId(), createMobilePhoneLinkDto());
    
    // when
    CustomerDto result = customerService.unlinkMobilePhone(customerDto.getId(), createMobilePhoneUnlinkDto());
    
    // then
    assertThat(result.getMobilePhonePricePlan()).isNull();
    assertThat(result.getSvcMgmtNum()).isNull();
  
    List<MessageEnvelope> msgs = findAllMessageEnvelopesOrderById();
    assertThat(msgs).hasSize(2);
    assertThat(msgs.get(0).getMessageName()).isEqualTo("mobilePhoneServiceLinkedEvent");
    assertThat(msgs.get(1).getMessageName()).isEqualTo("mobilePhoneServiceUnlinkedEvent");
  }
  
  @Test
  public void givenCustomerAndMobilePhone_whenChangeMobilePhonePricePlan_thenSuccess() {
    // given
    MobilePhoneLinkDto mobilePhoneDto = createMobilePhoneLinkDto();
    customerService.linkMobilePhone(customerDto.getId(), createMobilePhoneLinkDto());
    
    // when
    CustomerDto result = customerService.changeMobilePhonePricePlan(customerDto.getId(), createMobilePhonePricePlanChangeDto());
    
    // then
    assertThat(result.getMobilePhonePricePlan()).isEqualTo("SPECIAL");
    assertThat(result.getDcTargetUzooPassProductCodes()).isEmpty();
  
    List<MessageEnvelope> msgs = findAllMessageEnvelopesOrderById();
    assertThat(msgs).hasSize(2);
    assertThat(msgs.get(0).getMessageName()).isEqualTo("mobilePhoneServiceLinkedEvent");
    assertThat(msgs.get(1).getMessageName()).isEqualTo("mobilePhonePricePlanChangedEvent");
  }
  
  @Test
  public void givenCustomerAndMobilePhone_whenMultipleStep_thenSuccess() {
    // given
    customerService.linkMobilePhone(customerDto.getId(), createMobilePhoneLinkDto());
    customerService.changeMobilePhonePricePlan(customerDto.getId(), createMobilePhonePricePlanChangeDto());
    customerService.unlinkMobilePhone(customerDto.getId(), createMobilePhoneUnlinkDto());
  
    // when
    CustomerDto result = customerService.linkMobilePhone(customerDto.getId(), createMobilePhoneNewLinkDto());
    
    // then
    assertThat(result.getSvcMgmtNum()).isEqualTo(7000000002L);
    assertThat(result.getMobilePhonePricePlan()).isEqualTo("PLATINUM");
    assertThat(result.getDcTargetUzooPassProductCodes().get(0)).isEqualTo("NMO0000002");
  
    List<MessageEnvelope> msgs = findAllMessageEnvelopesOrderById();
    assertThat(msgs).hasSize(4);
    assertThat(msgs.get(0).getMessageName()).isEqualTo("mobilePhoneServiceLinkedEvent");
    assertThat(msgs.get(1).getMessageName()).isEqualTo("mobilePhonePricePlanChangedEvent");
    assertThat(msgs.get(2).getMessageName()).isEqualTo("mobilePhoneServiceUnlinkedEvent");
    assertThat(msgs.get(3).getMessageName()).isEqualTo("mobilePhoneServiceLinkedEvent");
  }
  
  private static MobilePhoneLinkDto createMobilePhoneLinkDto() {
    return MobilePhoneLinkDto.builder()
        .svcMgmtNum(7000000001L)
        .phoneNumber("01012345678")
        .mobilePhonePricePlan("PLATINUM")
        .productName("플래티넘")
        .dcTargetUzooPassProductCodes(List.of("NMO0000001"))
        .build();
  }
  
  private static MobilePhoneLinkDto createMobilePhoneUnlinkDto() {
    return createMobilePhoneLinkDto();
  }
  
  private static MobilePhoneLinkDto createMobilePhonePricePlanChangeDto() {
    return MobilePhoneLinkDto.builder()
        .svcMgmtNum(7000000001L)
        .phoneNumber("01012345678")
        .mobilePhonePricePlan("SPECIAL")
        .dcTargetUzooPassProductCodes(Collections.emptyList())
        .productName("스페셜")
        .build();
  }
  
  private static MobilePhoneLinkDto createMobilePhoneNewLinkDto() {
    return MobilePhoneLinkDto.builder()
        .svcMgmtNum(7000000002L)
        .phoneNumber("01012345679")
        .mobilePhonePricePlan("PLATINUM")
        .productName("플래티넘")
        .dcTargetUzooPassProductCodes(List.of("NMO0000002"))
        .build();
  }
  
  private List<MessageEnvelope> findAllMessageEnvelopesOrderById() {
    return messageEnvelopeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }
}
