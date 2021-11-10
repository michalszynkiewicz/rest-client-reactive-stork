package com.example;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
@RegisterRestClient(baseUri = "stork://my-service")
public interface Client {
    @GET
    String get();
}
