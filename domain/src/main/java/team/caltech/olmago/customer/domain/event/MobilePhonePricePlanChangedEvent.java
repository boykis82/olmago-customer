package team.caltech.olmago.customer.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class MobilePhonePricePlanChangedEvent extends CustomerEventBase {
  private final long mobilePhoneSvcMgmtNum;
  private final String mobilePhoneNumber;
  private final String mobilePhonePricePlan;
  private final String dcTargetUzooPassProductCode;
  
  @Builder
  public MobilePhonePricePlanChangedEvent(long customerId, LocalDateTime changeDtm, long mobilePhoneSvcMgmtNum, String mobilePhoneNumber, String mobilePhonePricePlan, String dcTargetUzooPassProductCode) {
    super(customerId, changeDtm);
    this.mobilePhoneSvcMgmtNum = mobilePhoneSvcMgmtNum;
    this.mobilePhoneNumber = mobilePhoneNumber;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.dcTargetUzooPassProductCode = dcTargetUzooPassProductCode;
  }
}