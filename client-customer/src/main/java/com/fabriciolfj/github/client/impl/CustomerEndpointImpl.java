package com.fabriciolfj.github.client.impl;

import com.fabriciolfj.github.client.CustomerEndpointItf;
import com.fabriciolfj.github.client.dto.CustomerDTO;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("customers")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class CustomerEndpointImpl {

    @Inject
    @RestClient
    private CustomerEndpointItf customer;

    @GET
    public List<CustomerDTO> getAll() {
        return customer.getAll();
    }

    @POST
    public Response create(final CustomerDTO c) {
        return customer.create(c);
    }

    @PUT
    public Response update(final CustomerDTO c) {
        return customer.update(c);
    }

    @DELETE
    public Response delete(final Long customerId) {
        return customer.delete(customerId);
    }
}
