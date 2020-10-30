package com.fabriciolfj.github.customer.repository;

import com.fabriciolfj.github.customer.entity.Customer;
import com.fabriciolfj.github.customer.exceptions.CustomerNotfoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CustomerRepository {

    @Inject
    EntityManager entityManager;

    private List<Customer> customers = new ArrayList<>();

    public List<Customer> findAll() {
        return entityManager.createNamedQuery("Customers.findAll", Customer.class).getResultList();
    }

    @Transactional
    public void add(final Customer customer) {
        entityManager.persist(customer);
    }

    public Customer findById(final Long id) {
        final var customer = entityManager.find(Customer.class, id);

        if (customer == null) {
            throw new CustomerNotfoundException("Customer not found. Id: " + id);
        }

        return customer;
    }

    @Transactional
    public void updateCustomer(final Customer customer, final Long id) {
        final var customerUpdate = findById(id);
        customerUpdate.setName(customer.getName());
        customerUpdate.setSurname(customer.getSurname());
    }

    @Transactional
    public void deleteCustomer(final Long id) {
        var customer = findById(id);
        entityManager.remove(customer);
    }
}
