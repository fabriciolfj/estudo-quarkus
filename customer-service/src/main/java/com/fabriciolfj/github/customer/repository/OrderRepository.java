package com.fabriciolfj.github.customer.repository;

import com.fabriciolfj.github.customer.entity.Customer;
import com.fabriciolfj.github.customer.entity.Orders;
import com.fabriciolfj.github.customer.exceptions.OrderNotfoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class OrderRepository {
    public List<Orders> findAll(final Long customerId) {
        return Customer.list("id", customerId);
    }

    public Orders findOrderById(final Long id) {
         final Orders order = Orders.findById(id);

        if (order == null) {
            throw new OrderNotfoundException("Order with id of " + id);
        }

        return order;
    }

    @Transactional
    public void updateOrder(final Orders order) {
        final Orders orderUpdate = Orders.findById(order.id);
        orderUpdate.item = order.item;
        orderUpdate.price = order.price;
    }

    @Transactional
    public void createOrder(final Orders order, final Customer customer) {
        order.customer = customer;
        order.persist();
    }

    @Transactional
    public void deleteOrder(final Long orderId) {
        Orders.deleteById(orderId);
    }
}
