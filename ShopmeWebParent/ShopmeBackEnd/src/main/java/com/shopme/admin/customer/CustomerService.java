package com.shopme.admin.customer;

import com.shopme.common.entity.Customer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;

    public static final int CUSTOMERS_PER_PAGE = 10;

    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    public List<Customer> findAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    public Customer findById(Integer id) {
        return customerRepository.findById(id).get();
    }

    public Page<Customer> listCustomers(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, CUSTOMERS_PER_PAGE, sort);

        if (keyword == null) {
            return customerRepository.findAll(pageable);
        }

        return customerRepository.search(keyword, pageable);
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
