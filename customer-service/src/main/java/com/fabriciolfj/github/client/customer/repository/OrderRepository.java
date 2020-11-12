package com.fabriciolfj.github.client.customer.repository;

import com.fabriciolfj.github.client.customer.entity.Customer;
import com.fabriciolfj.github.client.customer.entity.Orders;
import com.fabriciolfj.github.client.customer.exceptions.OrderNotfoundException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Bulkhead;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@ApplicationScoped
@Slf4j
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
        writeSomeLogging(order.item);
    }

    @Transactional
    public void deleteOrder(final Long orderId) {
        Orders.deleteById(orderId);
    }

    @Asynchronous
    @Bulkhead(value = 5, waitingTaskQueue = 10) //maximo 5 requisições concorrentes, e máximo 10 solicitações permitidas na fila de espera.
    private Future writeSomeLogging(final String item){
        log.info("New customer order at: {}", new Date());
        return CompletableFuture.completedFuture("ok");
    }
}
