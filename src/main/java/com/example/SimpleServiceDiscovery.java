package com.example;

import io.smallrye.mutiny.Uni;
import io.smallrye.stork.api.Metadata;
import io.smallrye.stork.api.Service;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.ServiceInstance;
import io.smallrye.stork.impl.DefaultServiceInstance;
import io.smallrye.stork.utils.ServiceInstanceIds;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleServiceDiscovery implements ServiceDiscovery {

    private final String url;
    private final String service;


    public SimpleServiceDiscovery(SimpleServiceDiscoveryProviderConfiguration configuration) {
        this.url = configuration.getUrl();
        this.service =configuration.getService();
    }

    private ServiceInstance toStorkServiceInstance(ServiceDto serviceDto) {
        URI uri = URI.create(serviceDto.url);
        boolean isSecure = uri.getScheme().endsWith("s");

        Map<String, String> labels = new HashMap<>();

        for (String label : serviceDto.labels) {
            labels.put(label, label);
        }

        return new DefaultServiceInstance(ServiceInstanceIds.next(), uri.getHost(),
                uri.getPort(), isSecure, labels, Metadata.empty());
    }


    @Override
    public Uni<List<ServiceInstance>> getServiceInstances() {
        Client discoveryClient = RestClientBuilder.newBuilder().baseUri(URI.create(this.url))
                .build(Client.class);
        return discoveryClient.getServices(this.service)
                .map(serviceDtos -> serviceDtos.stream().map(dto -> toStorkServiceInstance(dto)).collect(Collectors.toList()));
    }

    @Override
    public void initialize(Map<String, Service> services) {
        ServiceDiscovery.super.initialize(services);
    }

    @Path("/services")
    public interface Client {
        @GET
        @Path("/{serviceName}")
        Uni<List<ServiceDto>> getServices(@PathParam("serviceName") String serviceName);
    }

    public static class ServiceDto {
        public String serviceName;
        public String url;
        public List<String> labels;
    }
}
