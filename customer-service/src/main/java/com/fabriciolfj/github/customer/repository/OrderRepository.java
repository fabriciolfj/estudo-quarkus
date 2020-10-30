package com.fabriciolfj.github.customer.repository;

import com.fabriciolfj.github.customer.entity.Customer;
import com.fabriciolfj.github.customer.entity.Orders;
import com.fabriciolfj.github.customer.exceptions.OrderNotfoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class OrderRepository {

    @Inject
    EntityManager entityManager;

    public List<Orders> findAll(final Long customerId) {
        return entityManager.createNamedQuery("Orders.findAll")
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public Orders findOrderById(final Long id) {
        var order = entityManager.find(Orders.class, id);

        if (order == null) {
            throw new OrderNotfoundException("Order with id of " + id);
        }

        return order;
    }

    @Transactional
    public void updateOrder(final Orders order) {
        var orderUpdate = findOrderById(order.getId());
        orderUpdate.setItem(order.getItem());
        orderUpdate.setPrice(order.getPrice());
    }

    @Transactional
    public void createOrder(final Orders order, final Customer customer) {
        order.setCustomer(customer);
        entityManager.persist(order);
    }

    @Transactional
    public void deleteOrder(final Long orderId) {
        var o = findOrderById(orderId);
        entityManager.remove(o);
    }
}
