package com.fabriciolfj.github.customer.repository;

import com.fabriciolfj.github.customer.entity.Customer;
import com.fabriciolfj.github.customer.exceptions.CustomerNotfoundException;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CustomerRepository {

    private List<Customer> customers = new ArrayList<>();

    public List<Customer> findAll() {
        return customers;
    }

    public void add(final Customer customer) {
        this.customers.add(customer);
    }

    public Customer findById(final Long id) {
        return customers.stream().filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CustomerNotfoundException("Customer not found. Id: " + id));
    }

    public void updateCustomer(final Customer customer, final Long id) {
        customers.stream().filter(p -> p.getId().equals(id))
                .map(c -> {
                    c.setName(customer.getName());
                    return c;
                })
                .findFirst()
                .orElseThrow(() -> new CustomerNotfoundException("Customer not found. Id: " + id));
    }

    public void deleteCustomer(final Long id) {
        var customer = findById(id);
        customers.remove(customer);
    }
}
