package com.example;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
@RegisterRestClient(baseUri = "http://localhost:8406")
public interface Client {
    @GET
    @Path("/")
    Uni<String> get();
}
