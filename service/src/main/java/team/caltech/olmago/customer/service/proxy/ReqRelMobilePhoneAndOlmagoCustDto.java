package team.caltech.olmago.customer.service.proxy;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReqRelMobilePhoneAndOlmagoCustDto {
  private long svcMgmtNum;
  private long olmagoCustomerId;
  private LocalDateTime eventDateTime;
  
  @Builder
  public ReqRelMobilePhoneAndOlmagoCustDto(long svcMgmtNum, long olmagoCustomerId, LocalDateTime eventDateTime) {
    this.svcMgmtNum = svcMgmtNum;
    this.olmagoCustomerId = olmagoCustomerId;
    this.eventDateTime = eventDateTime;
  }
}
