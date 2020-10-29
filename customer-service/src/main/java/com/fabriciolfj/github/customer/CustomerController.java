package com.fabriciolfj.github.customer;

import com.fabriciolfj.github.customer.entity.Customer;
import com.fabriciolfj.github.customer.repository.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("customers")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class CustomerController {

    @Inject
    private CustomerRepository customerRepository;

    @GET
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GET
    @Path("{id}")
    public Customer findBydId(@PathParam("id") final Long id) {
        return customerRepository.findById(id);
    }

    @POST
    public Response create(final Customer customer) {
        customerRepository.add(customer);
        return Response.status(201).build();
    }

    @PUT
    @Path("{id}")
    public Response update(final Customer customer, @PathParam("id") final Long id) {
        customerRepository.updateCustomer(customer, id);
        return Response.status(201).build();
    }

    @DELETE
    public Response delete(@QueryParam("id") final Long id) {
        customerRepository.deleteCustomer(id);
        return Response.status(204).build();
    }
}