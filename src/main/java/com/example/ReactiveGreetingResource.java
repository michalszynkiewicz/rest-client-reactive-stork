package com.example;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/trigger")
public class ReactiveGreetingResource {

    @RestClient
    Client client;

    @GET
    public String hello(@QueryParam("requestCount") @DefaultValue("100") int requestCount) {
        for (int i = 0; i < requestCount; i++) {
            client.get();
        }
        return "All done!";
    }
}