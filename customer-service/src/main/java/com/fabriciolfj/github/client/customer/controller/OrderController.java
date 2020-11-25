package com.fabriciolfj.github.client.customer.controller;

import com.fabriciolfj.github.client.customer.entity.Orders;
import com.fabriciolfj.github.client.customer.repository.CustomerRepository;
import com.fabriciolfj.github.client.customer.repository.OrderRepository;
import io.vertx.axle.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("orders")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class OrderController {

    @Inject
    OrderRepository orderRepository;

    @Inject
    CustomerRepository customerRepository;

    @GET
    public List<Orders> getAll(@QueryParam("customerId") final Long customerId) {
        return orderRepository.findAll(customerId);
    }


    @POST
    @Path("/{customer}")
    public Response create(final Orders order, @PathParam("customer") final Long customerId) {
        final var c = customerRepository.findById(customerId);
        orderRepository.createOrder(order, c);
        return Response.status(201).build();
    }

    @PUT
    public Response update(final Orders order) {
        orderRepository.updateOrder(order);
        return Response.status(204).build();
    }

    @DELETE
    @Path("/{order}")
    public Response delete(@PathParam("order") final Long orderId) {
        orderRepository.deleteOrder(orderId);
        return Response.status(204).build();
    }
}
