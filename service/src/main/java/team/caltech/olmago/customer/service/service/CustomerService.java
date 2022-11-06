package team.caltech.olmago.customer.service.service;

import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneDto;

public interface CustomerService {
  CustomerDto findById(long id);
  CustomerDto createCustomer(CreateCustomerDto dto);
  CustomerDto linkMobilePhone(long id, MobilePhoneDto dto);
  CustomerDto unlinkMobilePhone(long id, MobilePhoneDto dto);
  CustomerDto changeMobilePhonePricePlan(long id, MobilePhoneDto dto);
}
