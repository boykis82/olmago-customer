package team.caltech.olmago.customer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MobilePhoneDto {
  private long svcMgmtNum;
  private String phoneNumber;
  private String productName;
  private String mobilePhonePricePlan;
  private String dcTargetUzooPassProductCode;
  
  @Builder
  public MobilePhoneDto(long svcMgmtNum, String phoneNumber, String productName, String mobilePhonePricePlan, String dcTargetUzooPassProductCode) {
    this.svcMgmtNum = svcMgmtNum;
    this.phoneNumber = phoneNumber;
    this.productName = productName;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.dcTargetUzooPassProductCode = dcTargetUzooPassProductCode;
  }
}
