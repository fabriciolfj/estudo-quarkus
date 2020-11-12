package com.fabriciolfj.github.client;

import com.fabriciolfj.github.client.dto.CustomerDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@RegisterRestClient
@Path("customers")
@Produces("application/json")
@Consumes("appplication/json")
public interface CustomerEndpointItf {

    @GET
    List<CustomerDTO> getAll();

    @POST
    Response create(final CustomerDTO customerDTO);

    @PUT
    Response update(final CustomerDTO customerDTO);

    @DELETE
    Response delete(final Long customerId);
}
