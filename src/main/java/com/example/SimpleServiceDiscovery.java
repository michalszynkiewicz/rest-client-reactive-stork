package com.example;

import io.smallrye.mutiny.Uni;
import io.smallrye.stork.DefaultServiceInstance;
import io.smallrye.stork.ServiceDiscovery;
import io.smallrye.stork.ServiceInstance;
import io.smallrye.stork.config.ServiceConfig;
import io.smallrye.stork.config.ServiceDiscoveryConfig;
import io.smallrye.stork.spi.ServiceDiscoveryProvider;
import io.smallrye.stork.spi.ServiceInstanceIds;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleServiceDiscovery implements ServiceDiscoveryProvider {
    @Override
    public ServiceDiscovery createServiceDiscovery(ServiceDiscoveryConfig config, String serviceName, ServiceConfig serviceConfig) {
        String discoveryUrl = config.parameters().get("url");
        if (discoveryUrl == null) {
            throw new IllegalArgumentException("No URL provided for service discovery");
        }
        Client discoveryClient = RestClientBuilder.newBuilder().baseUri(URI.create(discoveryUrl))
                .build(Client.class);


        String service = config.parameters().get("service");

        return new ServiceDiscovery() {
            @Override
            public Uni<List<ServiceInstance>> getServiceInstances() {

                return discoveryClient.getServices(service)
                        .map(serviceDtos -> serviceDtos.stream().map(dto -> toStorkServiceInstance(dto)).collect(Collectors.toList()));
            }
        };
    }

    private ServiceInstance toStorkServiceInstance(ServiceDto serviceDto) {
        URI uri = URI.create(serviceDto.url);
        boolean isSecure = uri.getScheme().endsWith("s");

        Map<String, String> labels = new HashMap<>();

        for (String label : serviceDto.labels) {
            labels.put(label, label);
        }

        return new DefaultServiceInstance(ServiceInstanceIds.next(), uri.getHost(),
                uri.getPort(), isSecure, labels, Collections.emptyMap());
    }

    @Override
    public String type() {
        return "simple";
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