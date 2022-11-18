package team.caltech.olmago.customer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CreateCustomerDto {
  private String name;
  private LocalDate birthday;
  
  @Builder
  public CreateCustomerDto(String name, LocalDate birthday) {
    this.name = name;
    this.birthday = birthday;
  }
}
