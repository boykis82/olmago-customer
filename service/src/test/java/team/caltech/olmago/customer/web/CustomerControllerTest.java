package team.caltech.olmago.customer.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneLinkDto;
import team.caltech.olmago.customer.service.config.ObjectMapperConfig;
import team.caltech.olmago.customer.service.proxy.ReqRelMobilePhoneAndOlmagoCustDto;
import team.caltech.olmago.customer.service.proxy.SwingProxy;
import team.caltech.olmago.customer.service.service.CustomerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerTest {
  @Autowired
  WebTestClient webTestClient;
  
  @MockBean
  private CustomerService customerService;
  
  @MockBean
  private SwingProxy swingProxy;
  
  @Test
  public void givenExistedCustomer_whenFindById_thenMustBeOk() throws Exception {
    // given
    CustomerDto dummyCustomerDto = dummyCustomerDto();
    given(customerService.findById(1L))
        .willReturn(dummyCustomerDto);
    
    // when
    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
        .uri("/olmago/api/v1/customers/{id}", 1L)
        .accept(APPLICATION_JSON)
        .exchange();
    
    // then
    responseSpec.expectStatus().isOk();
    compareResponseAndCustomerDto(responseSpec, dummyCustomerDto);
  }
  
  @Test
  public void givenEmpty_whenCreateCustomer_thenMustBeOk() throws Exception {
    // given
    CreateCustomerDto createCustomerDto = CreateCustomerDto.builder()
        .birthday(LocalDate.of(1982,1,1))
        .name("강인수")
        .build();
  
    CustomerDto dummyCustomerDto = dummyCustomerDto();
    given(customerService.createCustomer(createCustomerDto))
        .willReturn(dummyCustomerDto);
    
    // when
    WebTestClient.ResponseSpec responseSpec = webTestClient.post()
        .uri("/olmago/api/v1/customers")
        .body(BodyInserters.fromValue(createCustomerDto))
        .accept(APPLICATION_JSON)
        .exchange();
    
    // then
    responseSpec.expectStatus().isOk();
  }
  
  @Test
  public void givenNotExistedCustomer_whenFindById_thenMustBe5XXServerError() throws Exception {
    // given
    CustomerDto dummyCustomerDto = dummyCustomerDto();
    given(customerService.findById(1L))
        .willThrow(IllegalArgumentException.class);
    
    // when
    WebTestClient.ResponseSpec responseSpec = webTestClient.get()
        .uri("/olmago/api/v1/customers/{id}", 1L)
        .accept(APPLICATION_JSON)
        .exchange();
    
    // then
    responseSpec.expectStatus().is5xxServerError();
  }
  
  @Test
  public void givenExistedCustomer_whenLinkMobilePhone_thenMustBeOk() throws Exception {
    // given
    CustomerDto dummyCustomerDto = dummyCustomerDto();
    MobilePhoneLinkDto dummyMobilePhoneDto = dummyMobilePhoneDto();
    given(customerService.linkMobilePhone(1L, dummyMobilePhoneDto))
        .willReturn(dummyCustomerDto);
    given(swingProxy.linkMobilePhoneAndOlmagoCustomer(any(ReqRelMobilePhoneAndOlmagoCustDto.class)))
        .willReturn(Mono.empty().then());
    
    // when
    WebTestClient.ResponseSpec responseSpec = webTestClient.put()
        .uri("/olmago/api/v1/customers/{id}/linkMobilePhone", 1L)
        .body(BodyInserters.fromValue(dummyMobilePhoneDto))
        .accept(APPLICATION_JSON)
        .exchange();
    
    // then
    responseSpec.expectStatus().isOk();
  }
  
  @Test
  public void givenExistedCustomer_whenUnlinkMobilePhone_thenMustBeOk() throws Exception {
    // given
    CustomerDto dummyCustomerDto = dummyCustomerDto();
    MobilePhoneLinkDto dummyMobilePhoneDto = dummyMobilePhoneDto();
    given(customerService.unlinkMobilePhone(1L, dummyMobilePhoneDto))
        .willReturn(dummyCustomerDto);
    given(swingProxy.unlinkMobilePhoneAndOlmagoCustomer(any(ReqRelMobilePhoneAndOlmagoCustDto.class)))
        .willReturn(Mono.empty().then());
    
    // when
    WebTestClient.ResponseSpec responseSpec = webTestClient.put()
        .uri("/olmago/api/v1/customers/{id}/unlinkMobilePhone", 1L)
        .body(BodyInserters.fromValue(dummyMobilePhoneDto))
        .accept(APPLICATION_JSON)
        .exchange();
    
    // then
    responseSpec.expectStatus().isOk();
  }
  
  @Test
  public void givenExistedCustomer_whenChangeMobilePhonePricePlan_thenMustBeOk() throws Exception {
    // given
    CustomerDto dummyCustomerDto = dummyCustomerDto();
    MobilePhoneLinkDto dummyMobilePhoneDto = dummyMobilePhoneDto();
    given(customerService.unlinkMobilePhone(1L, dummyMobilePhoneDto))
        .willReturn(dummyCustomerDto);

    // when
    WebTestClient.ResponseSpec responseSpec = webTestClient.put()
        .uri("/olmago/api/v1/customers/{id}/changeMobilePhonePricePlan", 1L)
        .body(BodyInserters.fromValue(dummyMobilePhoneDto))
        .accept(APPLICATION_JSON)
        .exchange();
    
    // then
    responseSpec.expectStatus().isOk();
  }
  
  private static CustomerDto dummyCustomerDto() {
    return CustomerDto.builder()
        .id(1L)
        .ci("123")
        .name("강인수")
        .productName("플래티넘")
        .dcTargetUzooPassProductCodes(List.of("NMO0000001"))
        .svcMgmtNum(7100000001L)
        .birthday(LocalDate.of(1982,1,1))
        .mobilePhonePricePlan("PLATINUM")
        .linkedDateTime(LocalDateTime.of(2022,11,23,1,1,1))
        .build();
  }
  
  private static MobilePhoneLinkDto dummyMobilePhoneDto() {
    return MobilePhoneLinkDto.builder()
        .phoneNumber("123")
        .svcMgmtNum(7000000001L)
        .mobilePhonePricePlan("PLATINUM")
        .productName("플래티넘")
        .dcTargetUzooPassProductCodes(List.of("NMO0000001"))
        .build();
  }
  
  private void compareResponseAndCustomerDto(WebTestClient.ResponseSpec responseSpec, CustomerDto customerDto) {
    responseSpec.expectBody()
        .jsonPath("$.id").isEqualTo(customerDto.getId())
        .jsonPath("$.ci").isEqualTo(customerDto.getCi())
        .jsonPath("$.name").isEqualTo(customerDto.getName())
        .jsonPath("$.productName").isEqualTo(customerDto.getProductName())
        //.jsonPath("$.dcTargetUzooPassProductCode").isEqualTo(customerDto.getDcTargetUzooPassProductCode())
        .jsonPath("$.svcMgmtNum").isEqualTo(customerDto.getSvcMgmtNum())
        .jsonPath("$.mobilePhonePricePlan").isEqualTo(customerDto.getMobilePhonePricePlan())
        .jsonPath("$.linkedDateTime").isEqualTo(customerDto.getLinkedDateTime().format(DateTimeFormatter.ofPattern(ObjectMapperConfig.DATETIME_FORMATTER)))
        .jsonPath("$.birthday").isEqualTo(customerDto.getBirthday().format(DateTimeFormatter.ofPattern(ObjectMapperConfig.DATE_FORMATTER)));    
  }
}
