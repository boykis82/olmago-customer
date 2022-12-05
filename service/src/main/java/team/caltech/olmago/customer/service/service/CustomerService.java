package team.caltech.olmago.customer.service.service;

import team.caltech.olmago.customer.dto.CreateCustomerDto;
import team.caltech.olmago.customer.dto.CustomerDto;
import team.caltech.olmago.customer.dto.MobilePhoneLinkDto;

public interface CustomerService {
  CustomerDto findById(long id);
  CustomerDto createCustomer(CreateCustomerDto dto);
  CustomerDto linkMobilePhone(long id, MobilePhoneLinkDto dto);
  CustomerDto unlinkMobilePhone(long id, MobilePhoneLinkDto dto);
  CustomerDto changeMobilePhonePricePlan(long id, MobilePhoneLinkDto dto);
}
