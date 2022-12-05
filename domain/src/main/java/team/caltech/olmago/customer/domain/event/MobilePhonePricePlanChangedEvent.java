package team.caltech.olmago.customer.domain.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class MobilePhonePricePlanChangedEvent extends CustomerEventBase {
  private final long mobilePhoneSvcMgmtNum;
  private final String mobilePhoneNumber;
  private final String mobilePhonePricePlan;
  private final List<String> dcTargetUzooPassProductCodes;
  
  @Builder
  public MobilePhonePricePlanChangedEvent(long customerId, LocalDateTime changeDtm, long mobilePhoneSvcMgmtNum, String mobilePhoneNumber, String mobilePhonePricePlan, List<String> dcTargetUzooPassProductCodes) {
    super(customerId, changeDtm);
    this.mobilePhoneSvcMgmtNum = mobilePhoneSvcMgmtNum;
    this.mobilePhoneNumber = mobilePhoneNumber;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.dcTargetUzooPassProductCodes = dcTargetUzooPassProductCodes;
  }
}