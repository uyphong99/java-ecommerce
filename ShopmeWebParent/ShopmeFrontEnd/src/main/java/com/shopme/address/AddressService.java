package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findAllByCustomer(Customer customer) {
        return addressRepository.findAllByCustomer(customer);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Address findById(Integer id) {
        return addressRepository.findById(id).get();
    }

    public void clearDefault(List<Address> addresses) {
        for (Address address: addresses) {
            if (address.getDefaultAddress()) {
                address.setDefaultAddress(false);
                save(address);
            }
        }
    }

    public void delete(Address address) {
        addressRepository.delete(address);
    }

    public Address findCustomerDefaultAddress(Customer customer) {
        Address defaultAddress = addressRepository.findByCustomerAndDefaultAddress(customer, true);

        if (defaultAddress == null) {
            defaultAddress = Address.builder()
                    .country(customer.getCountry())
                    .state(customer.getState())
                    .addressLine1(customer.getAddressLine1())
                    .addressLine2(customer.getAddressLine2())
                    .city(customer.getCity())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .postalCode(customer.getPostalCode())
                    .phoneNumber(customer.getPhoneNumber())
                    .city(customer.getCity())
                    .build();
        }

        return defaultAddress;
    }
}
