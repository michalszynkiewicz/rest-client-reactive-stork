package com.example;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient(baseUri = "stork://greeting-service/greeting")
public interface GreetingClient {

    @GET
    @Path("/{name}")
    String get(@PathParam("name") String name);
}
