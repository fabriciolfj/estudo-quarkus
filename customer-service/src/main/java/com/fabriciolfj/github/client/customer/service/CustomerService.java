package com.fabriciolfj.github.client.customer.service;

import com.fabriciolfj.github.client.customer.entity.Customer;
import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerService {

    @ConsumeEvent("callcustomer")
    public String reply(final Customer customer) {
        return "Hello! I am " + customer.name;
    }
}
