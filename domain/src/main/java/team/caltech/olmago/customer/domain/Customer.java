package team.caltech.olmago.customer.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.caltech.olmago.customer.domain.event.MobilePhonePricePlanChangedEvent;
import team.caltech.olmago.customer.domain.event.MobilePhoneServiceLinkedEvent;
import team.caltech.olmago.customer.domain.event.MobilePhoneServiceUnlinkedEvent;
import team.caltech.olmago.customer.util.LocalDateTimeUtil;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Entity
@Table(
    name = "cust",
    indexes = {
        @Index(name = "cust_n1", columnList = "ci", unique = true)
    }
)
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  void setId(long id) { this.id = id; };
  
  @Version
  private int version;
  
  @Column(name = "name", length = 40, nullable = false)
  private String name;
  
  @Column(name = "birthday", nullable = false)
  private LocalDate birthday;
  
  @Column(name = "ci", length = 256, nullable = false)
  private String ci;
  
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
  private List<CustomerMobilePhoneRelationHistory> customerMobilePhoneRelationHistories = new ArrayList<>();
  
  @Builder
  public Customer(String name, LocalDate birthday, String ci) {
    this.name = name;
    this.birthday = birthday;
    this.ci = ci;
  }
  
  public MobilePhoneServiceLinkedEvent linkMobilePhone(CustomerMobilePhoneRelationHistory cmprh) {
    if (findActiveMobilePhone().isPresent()) {
      throw new IllegalStateException();
    }
    customerMobilePhoneRelationHistories.add(cmprh);
    MobilePhone mp = cmprh.getMobilePhone();
    return MobilePhoneServiceLinkedEvent.builder()
        .customerId(id)
        .dcTargetUzooPassProductCode(mp.getDcTargetUzooPassProductCode())
        .mobilePhoneNumber(mp.getPhoneNumber())
        .linkedDtm(cmprh.getEffStaDtm())
        .mobilePhonePricePlan(mp.getMobilePhonePricePlan().name())
        .mobilePhoneSvcMgmtNum(mp.getSvcMgmtNum())
        .build();
  }
  
  public MobilePhoneServiceUnlinkedEvent unlinkMobilePhone(LocalDateTime unlinkedDateTime) {
    CustomerMobilePhoneRelationHistory cmprh = findActiveMobilePhone()
        .orElseThrow(IllegalStateException::new);
    
    MobilePhone mp = cmprh.getMobilePhone();
    cmprh.terminate(unlinkedDateTime);
    return MobilePhoneServiceUnlinkedEvent.builder()
        .customerId(id)
        .dcTargetUzooPassProductCode(mp.getDcTargetUzooPassProductCode())
        .mobilePhoneNumber(mp.getPhoneNumber())
        .unlinkedDtm(unlinkedDateTime)
        .mobilePhonePricePlan(mp.getMobilePhonePricePlan().name())
        .mobilePhoneSvcMgmtNum(mp.getSvcMgmtNum())
        .build();
  }
  
  public Optional<CustomerMobilePhoneRelationHistory> findActiveMobilePhone() {
    return customerMobilePhoneRelationHistories.stream()
        .filter(cmprh -> cmprh.getEffEndDtm().equals(LocalDateTimeUtil.MAX_LOCAL_DATE_TIME))
        .findAny();
  }
  
  public MobilePhonePricePlanChangedEvent changeMobilePhonePricePlan(MobilePhonePricePlan mobilePhonePricePlan, String productName, String dcTargetUzooPassProductCode, LocalDateTime chgDtm) {
    CustomerMobilePhoneRelationHistory cmprh = findActiveMobilePhone()
        .orElseThrow(IllegalStateException::new);
    MobilePhone mp = cmprh.getMobilePhone();
    mp.changeMobilePhonePricePlan(mobilePhonePricePlan, productName, dcTargetUzooPassProductCode);
    return MobilePhonePricePlanChangedEvent.builder()
        .customerId(id)
        .dcTargetUzooPassProductCode(mp.getDcTargetUzooPassProductCode())
        .mobilePhoneNumber(mp.getPhoneNumber())
        .changeDtm(chgDtm)
        .mobilePhonePricePlan(mp.getMobilePhonePricePlan().name())
        .mobilePhoneSvcMgmtNum(mp.getSvcMgmtNum())
        .build();
  }
}
