package com.fabriciolfj.github.client.customer.controller;

import com.fabriciolfj.github.client.customer.entity.Customer;
import com.fabriciolfj.github.client.customer.repository.CustomerRepository;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("customers")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
@Tag(name = "OpenAPI Example", description = "Quarkus CRUD Example")
public class CustomerController {

    @Inject
    private CustomerRepository customerRepository;

    @GET
    //@RolesAllowed("user")
    @Operation(operationId = "all", description = "Getting All customers")
    @APIResponse(responseCode = "200", description = "Successful response.")
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GET
    @Path("{id}")
    public Customer findBydId(@PathParam("id") final Long id) {
        return customerRepository.findById(id);
    }

    @POST
    //@RolesAllowed("admin")
    public Response create(@Parameter(description = "The new customer", required = true) final Customer customer) {
        customerRepository.add(customer);
        return Response.status(201).build();
    }

    @PUT
    @Path("{id}")
    //@RolesAllowed("admin")
    public Response update(@Parameter(description = "The customer to update", required = true) final Customer customer, @PathParam("id") final Long id) {
        customerRepository.updateCustomer(customer, id);
        return Response.status(201).build();
    }

    @DELETE
    //@RolesAllowed("admin")
    public Response delete(@Parameter(description = "then customer to delete", required = true) @QueryParam("id") final Long id) {
        customerRepository.deleteCustomer(id);
        return Response.status(204).build();
    }
}