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
  
  @Column(name = "dc_uzoo_pass_prod_cd1")
  private String dcTargetUzooPassProductCode1;
  
  @Column(name = "dc_uzoo_pass_prod_cd2")
  private String dcTargetUzooPassProductCode2;
  
  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "mobilePhone")
  private List<CustomerMobilePhoneRelationHistory> customerMobilePhoneRelationHistories = new ArrayList<>();

  // todo : 할인대상우주패스 여러개 수용할수 있게 수정!
  @Builder
  public MobilePhone(long svcMgmtNum, String phoneNumber, String productName, MobilePhonePricePlan mobilePhonePricePlan, List<String> dcTargetUzooPassProductCodes) {
    this.svcMgmtNum = svcMgmtNum;
    this.phoneNumber = phoneNumber;
    this.productName = productName;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    setDcTargetUzooPassProductCodes(dcTargetUzooPassProductCodes);
  }
  
  public void changeMobilePhonePricePlan(MobilePhonePricePlan mobilePhonePricePlan, String productName, List<String> dcTargetUzooPassProductCodes) {
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.productName = productName;
    setDcTargetUzooPassProductCodes(dcTargetUzooPassProductCodes);
  }
  
  private void setDcTargetUzooPassProductCodes(List<String> dcTargetUzooPassProductCodes) {
    if (dcTargetUzooPassProductCodes.size() > 1) {
      this.dcTargetUzooPassProductCode1 = dcTargetUzooPassProductCodes.get(0);
      this.dcTargetUzooPassProductCode2 = dcTargetUzooPassProductCodes.get(1);
    } else if (dcTargetUzooPassProductCodes.size() == 1) {
      this.dcTargetUzooPassProductCode1 = dcTargetUzooPassProductCodes.get(0);
      this.dcTargetUzooPassProductCode2 = null;
    } else {
      this.dcTargetUzooPassProductCode1 = null;
      this.dcTargetUzooPassProductCode2 = null;
    }
  }
  
  public List<String> getDcTargetUzooPassProductCodes() {
    List<String> result = new ArrayList<>();
    if (dcTargetUzooPassProductCode1 != null) result.add(dcTargetUzooPassProductCode1);
    if (dcTargetUzooPassProductCode2 != null) result.add(dcTargetUzooPassProductCode2);
    return result;
  }
}
