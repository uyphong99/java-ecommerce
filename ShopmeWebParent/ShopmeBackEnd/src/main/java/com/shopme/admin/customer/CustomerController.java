package com.shopme.admin.customer;

import com.shopme.admin.setting.country.CountryService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@AllArgsConstructor
@Controller
public class CustomerController {

    private CustomerService service;

    private CountryService countryService;

    @GetMapping("/customers")
    public String customersManagement(Model model) {
        return customerPage(null, 1, "asc", "firstName", model);
    }

    @GetMapping("/customers/page/{pageNumber}")
    public String customerPage(@Param("keyword") String keyword,
                               @PathVariable("pageNumber") Integer pageNumber,
                               @Param("sortDir") String sortDir,
                               @Param("sortField") String sortField,
                               Model model) {
        Page<Customer> pageCustomer = service.listCustomers(pageNumber, sortField, sortDir, keyword);
        List<Customer> listCustomer = pageCustomer.getContent();

        long startCount = (pageNumber - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
        long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;

        if (endCount > pageCustomer.getTotalElements()) {
            endCount = pageCustomer.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageCustomer.getTotalPages());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageCustomer.getTotalElements());
        model.addAttribute("listCustomers", listCustomer);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "customers/customers";
    }

    @GetMapping("/customers/{customerId}/enabled/{enabled}")
    public String enableCustomer(@PathVariable("customerId") Integer customerId,
                                 @PathVariable("enabled") String enabledString) {
        Customer customer = service.findById(customerId);

        //boolean enabled = enabledString == "true" ? true : false;
        boolean enabled = !customer.getEnabled();
        customer.setEnabled(enabled);
        service.saveCustomer(customer);

        return "redirect:/customers";
    }

    @GetMapping("/customers/edit/{customerId}")
    public String editCustomer(@PathVariable("customerId") Integer id, Model model) {

        List<Country> listCountries = countryService.findAllCountry();
        Customer customer = service.findById(id);

        model.addAttribute("listCountries", listCountries);
        model.addAttribute("customer", customer);

        return "customers/customer_form";
    }

    @GetMapping("/customers/delete/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") Integer id) {
        Customer customer = service.findById(id);

        service.deleteCustomer(customer);

        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String getCustomerDetail(@PathVariable("id") Integer id,
                                    Model model) {
        Customer customer = service.findById(id);
        model.addAttribute("customer", customer);

        return "customers/customer_detail_modal";
    }
}
