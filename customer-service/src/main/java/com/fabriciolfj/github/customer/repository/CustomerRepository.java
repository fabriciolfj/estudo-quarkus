package com.fabriciolfj.github.customer.repository;

import com.fabriciolfj.github.customer.entity.Customer;
import com.fabriciolfj.github.customer.exceptions.CustomerNotfoundException;
import io.quarkus.panache.common.Sort;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class CustomerRepository {

    @Timeout(1000)
    @Fallback(fallbackMethod = "findAllStatic")
    @Retry(maxRetries = 3, retryOn = {RuntimeException.class, TimeoutException.class})
    @CircuitBreaker(failOn = {RuntimeException.class}, successThreshold = 5,requestVolumeThreshold = 4, failureRatio = 0.75, delay = 100)
    @Counted(description = "Customer list count", absolute = true)
    @Timed(name = "timeCheck", description = "Tempo para listar os clientes", unit = MetricUnits.MILLISECONDS)
    public List<Customer> findAll() {
        //randomSleep();
        possibleFailure();
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

    public List<Customer> findAllStatic() {
        return List.of(Customer.builder()
                .id(99L)
                .name("fallback")
                .surname("fallback")
                .build());
    }

    private void randomSleep() {
        try {
            Thread.sleep(new Random().nextLong());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void possibleFailure() {
        if (new Random().nextFloat() <  0.5f) {
            throw new RuntimeException("Resource failure");
        }
    }
}
