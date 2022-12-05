package team.caltech.olmago.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.caltech.olmago.customer.domain.Customer;
import team.caltech.olmago.customer.service.config.ObjectMapperConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CustomerDto {
  private long id;
  private String name;
  
  @JsonFormat(pattern = ObjectMapperConfig.DATE_FORMATTER)
  private LocalDate birthday;
  private String ci;
  
  private Long svcMgmtNum;
  private String productName;
  private String mobilePhonePricePlan;
  
  @JsonFormat(pattern = ObjectMapperConfig.DATETIME_FORMATTER)
  private LocalDateTime linkedDateTime;
  private List<String> dcTargetUzooPassProductCodes;
  
  @Builder
  CustomerDto(long id, String name, LocalDate birthday, String ci, Long svcMgmtNum, String productName, String mobilePhonePricePlan, LocalDateTime linkedDateTime, List<String> dcTargetUzooPassProductCodes) {
    this.id = id;
    this.name = name;
    this.birthday = birthday;
    this.ci = ci;
    this.svcMgmtNum = svcMgmtNum;
    this.productName = productName;
    this.mobilePhonePricePlan = mobilePhonePricePlan;
    this.linkedDateTime = linkedDateTime;
    this.dcTargetUzooPassProductCodes = dcTargetUzooPassProductCodes;
  }
  
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
          this.dcTargetUzooPassProductCodes = mp.getMobilePhone().getDcTargetUzooPassProductCodes();
        }
    );
  }

}
