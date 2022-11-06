package team.caltech.olmago.customer.dto;

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
}
