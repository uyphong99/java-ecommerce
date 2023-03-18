package com.shopme.customer;

import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @InjectMocks
    private CustomerService service;

    @Mock
    private CustomerRepository repository;

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = Customer.builder()
                .id(1)
                .email("lehoanganhvn@gmail.com")
                .password("45343543543")
                .firstName("Hoang")
                .lastName("Anh")
                .build();
    }
    @Test
    void givenExistsCustomerWithSameIdAndName_whenCheckUnique_shouldReturnTrue() {
        String email = "lehoanganhvn@gmail.com";
        //given(repository.existsByEmail(email)).willReturn(true);
        given(repository.findEmailById(1)).willReturn(email);

        Customer testCustomer = Customer.builder().id(1)
                .email("lehoanganhvn@gmail.com")
                .password("gfdddfss")
                .firstName("difpsif")
                .lastName("dyifudsf")
                .build();


        Boolean result = service.isUniqueEmail(testCustomer);

        assertThat(result).isTrue();
    }

    @Test
    public void testIsUniqueEmail_newCustomer() {
        // Arrange
        String email = "test@example.com";
        Customer customer = Customer.builder()
                .id(null)
                .email(email)
                .build();
        when(repository.existsByEmail(email)).thenReturn(false);

        // Act
        Boolean result = service.isUniqueEmail(customer);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsUniqueEmail_existingCustomer_sameEmail() {
        // Arrange
        Integer id = 1;
        String email = "test@example.com";
        Customer customer = Customer.builder()
                .id(id)
                .email(email)
                .build();
        when(repository.findEmailById(id)).thenReturn(email);

        // Act
        Boolean result = service.isUniqueEmail(customer);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsUniqueEmail_existingCustomer_differentEmail() {
        // Arrange
        Integer id = 1;
        String oldEmail = "old@example.com";
        String newEmail = "new@example.com";
        Customer customer = Customer.builder()
                .email(newEmail)
                .id(id)
                .build();
        when(repository.findEmailById(id)).thenReturn(oldEmail);
        when(repository.existsByEmail(newEmail)).thenReturn(true);

        // Act
        Boolean result = service.isUniqueEmail(customer);

        // Assert
        assertFalse(result);
    }
}
