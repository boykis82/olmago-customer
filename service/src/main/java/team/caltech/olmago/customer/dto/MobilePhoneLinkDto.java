package team.caltech.olmago.customer.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@EqualsAndHashCode    // mock test시 객체간 동일성 확인을 위함
@NoArgsConstructor
public class MobilePhoneLinkDto {
  private long svcMgmtNum;
  private String phoneNumber;
  private String productName;
  private String mobilePhonePricePlan;
  private List<String> dcTargetUzooPassProductCodes;
  
  @Builder
  public MobilePhoneLinkDto(long svcMgmtNum, String phoneNumber, String productName, String mobilePhonePricePlan, List<String> dcTargetUzooPassProductCodes) {
    this.svcMgmtNum = svcMgmtNum;
    this.phoneNumber = phoneNumber;
    this.productName = productName;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.dcTargetUzooPassProductCodes = dcTargetUzooPassProductCodes;
  }
}
