package team.caltech.olmago.customer.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.caltech.olmago.customer.util.LocalDateTimeUtil;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(
    name = "cust_mbl_phone_rel_hst",
    indexes = {
        @Index(name = "cust_mbl_phone_rel_hst_n1", columnList = "cust_id, eff_end_dtm desc, mbl_phone_id", unique = true)
    }
)
public class CustomerMobilePhoneRelationHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Version
  private int version;
  
  @ManyToOne
  @JoinColumn(name = "cust_id")
  private Customer customer;
  
  @ManyToOne
  @JoinColumn(name = "mbl_phone_id")
  private MobilePhone mobilePhone;
  
  @Column(name = "eff_sta_dtm")
  private LocalDateTime effStaDtm;
  
  @Column(name = "eff_end_dtm")
  private LocalDateTime effEndDtm;
  
  @Builder
  public CustomerMobilePhoneRelationHistory(Customer customer, MobilePhone mobilePhone, LocalDateTime effStaDtm) {
    this.customer = customer;
    this.mobilePhone = mobilePhone;
    this.effStaDtm = effStaDtm;
    this.effEndDtm = LocalDateTimeUtil.MAX_LOCAL_DATE_TIME;
  }
  
  public void terminate(LocalDateTime unlinkedDateTime) {
    this.effEndDtm = unlinkedDateTime;
  }
}
