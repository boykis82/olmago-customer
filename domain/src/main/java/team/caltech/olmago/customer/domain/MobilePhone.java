package team.caltech.olmago.customer.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(
    name = "mbl_phone",
    indexes = {
        @Index(name = "mbl_phone_n1", columnList = "svc_mgmt_num", unique = true)
    }
)
public class MobilePhone {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Version
  private int version;
  
  @Column(name = "svc_mgmt_num", nullable = false)
  private long svcMgmtNum;
  
  @Column(name = "phone_num", length = 20, nullable = false)
  private String phoneNumber;
  
  @Column(name = "prod_nm", length = 80, nullable = false)
  private String productName;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "mbl_phone_prcpln", nullable = false)
  private MobilePhonePricePlan mobilePhonePricePlan;
  
  @Column(name = "dc_uzoo_pass_prod_cd")
  private String dcTargetUzooPassProductCode;
  
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "mobilePhone")
  private List<CustomerMobilePhoneRelationHistory> customerMobilePhoneRelationHistories = new ArrayList<>();
  
  @Builder
  public MobilePhone(long svcMgmtNum, String phoneNumber, String productName, MobilePhonePricePlan mobilePhonePricePlan, String dcTargetUzooPassProductCode) {
    this.svcMgmtNum = svcMgmtNum;
    this.phoneNumber = phoneNumber;
    this.productName = productName;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.dcTargetUzooPassProductCode = dcTargetUzooPassProductCode;
  }
  
  public void changeMobilePhonePricePlan(MobilePhonePricePlan mobilePhonePricePlan, String productName, String dcTargetUzooPassProductCode) {
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.productName = productName;
    this.dcTargetUzooPassProductCode = dcTargetUzooPassProductCode;
  }
}
