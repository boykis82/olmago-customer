package team.caltech.olmago.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team.caltech.olmago.customer.domain.Customer;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CustomerDto {
  private long id;
  private String name;
  private LocalDate birthday;
  private String ci;
  
  private Long svcMgmtNum;
  private String productName;
  private String mobilePhonePricePlan;
  private LocalDateTime linkedDateTime;
  
  public CustomerDto(Customer entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.birthday = entity.getBirthday();
    this.ci = entity.getCi();
    
    entity.findActiveMobilePhone().ifPresent(
        mp -> {
          this.linkedDateTime = mp.getEffStaDtm();
          this.svcMgmtNum = mp.getMobilePhone().getSvcMgmtNum();
          this.productName = mp.getMobilePhone().getProductName();
          this.mobilePhonePricePlan = mp.getMobilePhone().getMobilePhonePricePlan().name();
        }
    );
  }
}
