package com.example;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/greeting")
public class GreetingResource {
    @RestClient
    GreetingClient greetingClient;

    @GET
    @Path("/{name}")
    public String get(@PathParam("name") String name) {
        return greetingClient.get(name);
    }


}
