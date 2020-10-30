package com.fabriciolfj.github.customer.repository;

import com.fabriciolfj.github.customer.entity.Customer;
import com.fabriciolfj.github.customer.exceptions.CustomerNotfoundException;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CustomerRepository {

    public List<Customer> findAll() {
        return Customer.listAll(Sort.by("id"));
    }

    @Transactional
    public void add(final Customer customer) {
        customer.persist();
    }

    public Customer findById(final Long id) {
        final Customer customer = Customer.findById(id);

        if (customer == null) {
            throw new CustomerNotfoundException("Customer not found. Id: " + id);
        }

        return customer;
    }

    @Transactional
    public void updateCustomer(final Customer customer, final Long id) {
        final Customer customerUpdate = findById(id);
        customerUpdate.name = customer.name;
        customerUpdate.surname = customer.surname;
    }

    @Transactional
    public void deleteCustomer(final Long id) {
        Customer.deleteById(id);
    }
}
