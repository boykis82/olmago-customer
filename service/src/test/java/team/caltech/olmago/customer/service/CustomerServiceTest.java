package team.caltech.olmago.customer.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.caltech.olmago.customer.domain.*;
import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneDto;
import team.caltech.olmago.customer.service.service.CustomerService;

import java.time.LocalDate;

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
  }
  
  @Test
  public void givenCustomerAndMobilePhone_whenLinkMobilePhone_thenSuccess() {
    // given
    // when
    MobilePhoneDto mobilePhoneDto = MobilePhoneDto.builder()
        .svcMgmtNum(7000000001L)
        .phoneNumber("01012345678")
        .mobilePhonePricePlan("PLATINUM")
        .productName("플래티넘")
        .dcTargetUzooPassProductCode("NMO0000001")
        .build();
    customerDto = customerService.linkMobilePhone(customerDto.getId(), mobilePhoneDto);
    
    // then
    assertThat(customerDto.getMobilePhonePricePlan()).isEqualTo("PLATINUM");
    assertThat(customerDto.getSvcMgmtNum()).isEqualTo(7000000001L);
    assertThat(customerDto.getDcTargetUzooPassProductCode()).isEqualTo("NMO0000001");
  }
  
  @Test
  public void givenCustomerAndMobilePhone_whenUnlinkMobilePhone_thenSuccess() {
    // given
    MobilePhoneDto mobilePhoneDto = MobilePhoneDto.builder()
        .svcMgmtNum(7000000001L)
        .phoneNumber("01012345678")
        .mobilePhonePricePlan("PLATINUM")
        .productName("플래티넘")
        .dcTargetUzooPassProductCode("NMO0000001")
        .build();
    customerService.linkMobilePhone(customerDto.getId(), mobilePhoneDto);
    
    // when
    customerDto = customerService.unlinkMobilePhone(customerDto.getId(), mobilePhoneDto);
    
    // then
    assertThat(customerDto.getMobilePhonePricePlan()).isNull();
    assertThat(customerDto.getSvcMgmtNum()).isNull();
  }
  
  @Test
  public void givenCustomerAndMobilePhone_whenChangeMobilePhonePricePlan_thenSuccess() {
    // given
    MobilePhoneDto mobilePhoneDto = MobilePhoneDto.builder()
        .svcMgmtNum(7000000001L)
        .phoneNumber("01012345678")
        .mobilePhonePricePlan("PLATINUM")
        .productName("플래티넘")
        .dcTargetUzooPassProductCode("NMO0000001")
        .build();
    customerService.linkMobilePhone(customerDto.getId(), mobilePhoneDto);
    
    // when
    mobilePhoneDto = MobilePhoneDto.builder()
        .svcMgmtNum(7000000001L)
        .phoneNumber("01012345678")
        .mobilePhonePricePlan("SPECIAL")
        .productName("스페셜")
        .build();
    customerDto = customerService.changeMobilePhonePricePlan(customerDto.getId(), mobilePhoneDto);
    
    // then
    assertThat(customerDto.getMobilePhonePricePlan()).isEqualTo("SPECIAL");
    assertThat(customerDto.getDcTargetUzooPassProductCode()).isNull();
  }
}
