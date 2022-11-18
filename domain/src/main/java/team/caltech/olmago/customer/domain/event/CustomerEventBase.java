package team.caltech.olmago.customer.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
public class CustomerEventBase {
  protected final long customerId;
  protected final LocalDateTime eventOccurDtm;
  
}
