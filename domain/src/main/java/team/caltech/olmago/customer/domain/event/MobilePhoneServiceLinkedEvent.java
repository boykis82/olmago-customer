package team.caltech.olmago.customer.domain.event;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class MobilePhoneServiceLinkedEvent extends CustomerEventBase  {
  private final long mobilePhoneSvcMgmtNum;
  private final String mobilePhoneNumber;
  private final String mobilePhonePricePlan;
  private final String dcTargetUzooPassProductCode;
  
  @Builder
  public MobilePhoneServiceLinkedEvent(long customerId, LocalDateTime linkedDtm, long mobilePhoneSvcMgmtNum, String mobilePhoneNumber, String mobilePhonePricePlan, String dcTargetUzooPassProductCode) {
    super(customerId, linkedDtm);
    this.mobilePhoneSvcMgmtNum = mobilePhoneSvcMgmtNum;
    this.mobilePhoneNumber = mobilePhoneNumber;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.dcTargetUzooPassProductCode = dcTargetUzooPassProductCode;
  }
}