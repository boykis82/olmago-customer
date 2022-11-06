package team.caltech.olmago.customer.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CustomerEventBase {
  protected final long customerId;
  protected final LocalDateTime eventOccurDtm;
  
}
