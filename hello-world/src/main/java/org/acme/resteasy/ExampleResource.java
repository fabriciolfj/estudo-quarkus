package org.acme.resteasy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/helloworld")
public class ExampleResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
        return "hello";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public String hello(@PathParam("name") final String name) {
        log.info("Called with " + name);
        return "hello " + name;
    }
}