package team.caltech.olmago.customer.domain;

import org.junit.Before;
import org.junit.Test;
import team.caltech.olmago.customer.domain.event.MobilePhonePricePlanChangedEvent;
import team.caltech.olmago.customer.domain.event.MobilePhoneServiceLinkedEvent;
import team.caltech.olmago.customer.domain.event.MobilePhoneServiceUnlinkedEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/*
TODO
  (clear) - 고객과 이동전화 최초 연결
  (clear) - 고객과 이동전화 연결된 상태에서 또 연결 -> 오류
  (clear) - 고객과 이동전화 연결됐던 적 있는 상태(현재는 연결 없음)에서 또 연결 -> 정상
  (clear) - 고객과 이동전화 연결된 상태에서 연결 해제
  (clear) - 고객과 이동전화 연결안되어 있는데 연결 해제 -> 오류
  (clear) - 고객과 이동전화 연결됐던 적 있는 상태(현재는 연결 없음)에서 연결 해제 -> 오류
  (clear) - 이동전화 요금제 변경
  (clear) - 연결 이동전화 없는데 이동전화 요금제 변경 -> 오류
  (clear) - 연결 이동전화 있었는데 없는 상태에서 이동전화 요금제 변경 -> 오류
 */

public class CustomerMobilePhoneRelationTest {
  LocalDateTime now;
  LocalDateTime after3Days;
  Customer cust;
  MobilePhone mobilePhone;
  MobilePhone mobilePhone2;
  CustomerMobilePhoneRelationHistory cmprh;
  CustomerMobilePhoneRelationHistory cmprh2;
  
  long customerId = 1L;
  String phoneNumber = "01012345678";
  String productName = "abc";
  long svcMgmtNum = 7123112312L;
  String dcTargetUzooPassProductCode = "NMO0000001";
  MobilePhonePricePlan platinumPricePlan = MobilePhonePricePlan.PLATINUM;
  
  @Before
  public void setup() {
    now = LocalDateTime.now();
    after3Days = LocalDateTime.now().plusDays(3);
    
    cust = Customer.builder()
        .name("강인수")
        .ci("1")
        .birthday(LocalDate.of(1982,1,1))
        .build();
    cust.setId(1L);
  
    mobilePhone = MobilePhone.builder()
        .mobilePhonePricePlan(platinumPricePlan)
        .phoneNumber(phoneNumber)
        .productName(productName)
        .svcMgmtNum(svcMgmtNum)
        .dcTargetUzooPassProductCode(dcTargetUzooPassProductCode)
        .build();
  
    mobilePhone2 = MobilePhone.builder()
        .mobilePhonePricePlan(platinumPricePlan)
        .phoneNumber(phoneNumber)
        .productName(productName)
        .svcMgmtNum(svcMgmtNum)
        .build();
    
    cmprh = CustomerMobilePhoneRelationHistory.builder()
        .mobilePhone(mobilePhone)
        .customer(cust)
        .effStaDtm(now)
        .build();
  
    cmprh2 = CustomerMobilePhoneRelationHistory.builder()
        .mobilePhone(mobilePhone2)
        .customer(cust)
        .effStaDtm(after3Days)
        .build();
  }
  
  @Test // 고객과 이동전화 최초 연결
  public void givenNoRelation_whenLinkCustomerAndMobilePhone_thenFireMobilePhoneServiceLinkedEvent() {
    // given
    // when
    MobilePhoneServiceLinkedEvent actualEvent = cust.linkMobilePhone(cmprh);
    MobilePhoneServiceLinkedEvent expectedEvent = MobilePhoneServiceLinkedEvent.builder()
        .customerId(customerId)
        .mobilePhoneNumber(phoneNumber)
        .linkedDtm(now)
        .mobilePhoneSvcMgmtNum(svcMgmtNum)
        .mobilePhonePricePlan(platinumPricePlan.name())
        .dcTargetUzooPassProductCode(dcTargetUzooPassProductCode)
        .build();
    
    // then
    assertThat(cust.findActiveMobilePhone().isPresent()).isTrue();
    assertThat(cust.findActiveMobilePhone().get().getMobilePhone()).isSameAs(mobilePhone);
    assertThat(cust.getCustomerMobilePhoneRelationHistories()).contains(cmprh);
    assertThat(cust.getCustomerMobilePhoneRelationHistories()).hasSize(1);
    assertThat(actualEvent).isEqualTo(expectedEvent);
  }
  
  @Test(expected = IllegalStateException.class) // 고객과 이동전화 연결된 상태에서 또 연결
  public void givenRelation_whenLinkCustomerAndMobilePhoneAgain_thenThrowError() {
    // given
    cust.linkMobilePhone(cmprh);
    // when
    CustomerMobilePhoneRelationHistory cmprh2 = CustomerMobilePhoneRelationHistory.builder()
        .mobilePhone(mobilePhone)
        .customer(cust)
        .effStaDtm(now)
        .build();
    cust.linkMobilePhone(cmprh2);
    // then -> error
  }
  
  @Test // 고객과 이동전화 연결됐던 적 있는 상태(현재는 연결 없음)에서 다시 연결
  public void givenTerminatedRelation_whenLinkCustomerAndMobilePhoneAgain_thenFireMobilePhoneServiceLinkedEvent() {
    // given
    cust.linkMobilePhone(cmprh);
    cust.unlinkMobilePhone(LocalDateTime.now());
    // when
    MobilePhoneServiceLinkedEvent actualEvent = cust.linkMobilePhone(cmprh2);
    MobilePhoneServiceLinkedEvent expectedEvent = MobilePhoneServiceLinkedEvent.builder()
        .customerId(customerId)
        .mobilePhoneNumber(phoneNumber)
        .linkedDtm(after3Days)
        .mobilePhoneSvcMgmtNum(svcMgmtNum)
        .mobilePhonePricePlan(platinumPricePlan.name())
        .build();
    
    // then
    assertThat(cust.findActiveMobilePhone().isPresent()).isTrue();
    assertThat(cust.findActiveMobilePhone().get().getMobilePhone()).isSameAs(mobilePhone2);
    assertThat(cust.getCustomerMobilePhoneRelationHistories()).contains(cmprh, cmprh2);
    assertThat(cust.getCustomerMobilePhoneRelationHistories()).hasSize(2);
    assertThat(actualEvent).isEqualTo(expectedEvent);
  }
  
  @Test // 고객과 이동전화 연결된 상태에서 연결 해제
  public void givenRelation_whenUnlinkCustomerAndMobilePhone_thenFireMobilePhoneServiceUnlinkedEvent() {
    // given
    cust.linkMobilePhone(cmprh);
    // when
    MobilePhoneServiceUnlinkedEvent actualEvent = cust.unlinkMobilePhone(now);
    MobilePhoneServiceUnlinkedEvent expectedEvent = MobilePhoneServiceUnlinkedEvent.builder()
        .customerId(customerId)
        .mobilePhoneNumber(phoneNumber)
        .unlinkedDtm(now)
        .mobilePhoneSvcMgmtNum(svcMgmtNum)
        .mobilePhonePricePlan(platinumPricePlan.name())
        .dcTargetUzooPassProductCode(dcTargetUzooPassProductCode)
        .build();
    // then
    assertThat(cust.findActiveMobilePhone().isPresent()).isFalse();
    assertThat(cust.getCustomerMobilePhoneRelationHistories()).contains(cmprh);
    assertThat(cmprh.getEffEndDtm()).isEqualTo(now);
    assertThat(cust.getCustomerMobilePhoneRelationHistories()).hasSize(1);
    assertThat(actualEvent).isEqualTo(expectedEvent);
  }
  
  @Test(expected = IllegalStateException.class) // 고객과 이동전화 연결안되어 있는데 연결 해제
  public void givenNoRelation_whenUnlinkCustomerAndMobilePhone_thenThrowError() {
    // given

    // when
    cust.unlinkMobilePhone(now);
    // then -> error
  }
  
  @Test(expected = IllegalStateException.class) // 고객과 이동전화 연결됐던 적 있는 상태(현재는 연결 없음)에서 연결 해제
  public void givenTerminatedRelation_whenUnlinkCustomerAndMobilePhone_thenThrowError() {
    // given
    cust.linkMobilePhone(cmprh);
    cust.unlinkMobilePhone(LocalDateTime.now());
    // when
    cust.unlinkMobilePhone(now);
    // then -> error
  }
  
  @Test // 이동전화 요금제 변경
  public void givenRelation_whenChangePricePlan_thenFireMobilePhonePricePlanChangedEvent() {
    // given
    cust.linkMobilePhone(cmprh);
    MobilePhonePricePlan afterPricePlan = MobilePhonePricePlan.SPECIAL;
    String afterDcTargetUzooPassProductCode = "NMO0000002";
    
    // when
    MobilePhonePricePlanChangedEvent actualEvent =
        cust.changeMobilePhonePricePlan(afterPricePlan, "스페셜", "NMO0000002", now);
    MobilePhonePricePlanChangedEvent expectedEvent = MobilePhonePricePlanChangedEvent.builder()
        .customerId(customerId)
        .mobilePhoneNumber(phoneNumber)
        .changeDtm(now)
        .mobilePhoneSvcMgmtNum(svcMgmtNum)
        .mobilePhonePricePlan(afterPricePlan.name())
        .dcTargetUzooPassProductCode(afterDcTargetUzooPassProductCode)
        .build();
    
    // then
    assertThat(actualEvent).isEqualTo(expectedEvent);
  }
  
  @Test(expected = IllegalStateException.class) // 연결 이동전화 없는데 이동전화 요금제 변경
  public void givenNoRelation_whenChangePricePlan_thenThrowError() {
    // given
    // when
    cust.changeMobilePhonePricePlan(MobilePhonePricePlan.SPECIAL, "스페셜", "NMO0000002", now);
    // then -> error
  }
  
  @Test(expected = IllegalStateException.class) // 연결 이동전화 있었는데 없는 상태에서 이동전화 요금제 변경
  public void givenTerminatedRelation_whenChangePricePlan_thenThrowError() {
    // given
    cust.linkMobilePhone(cmprh);
    cust.unlinkMobilePhone(after3Days);
    // when
    cust.changeMobilePhonePricePlan(MobilePhonePricePlan.SPECIAL, "스페셜", "NMO0000002", now);
    // then -> error
  }
}
