package team.caltech.olmago.customer.domain.event;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class MobilePhoneServiceUnlinkedEvent extends CustomerEventBase {
  private final long mobilePhoneSvcMgmtNum;
  
  @Builder
  public MobilePhoneServiceUnlinkedEvent(long customerId, LocalDateTime unlinkedDtm, long mobilePhoneSvcMgmtNum) {
    super(customerId, unlinkedDtm);
    this.mobilePhoneSvcMgmtNum = mobilePhoneSvcMgmtNum;
  }
}