package com.example;

import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.config.ServiceConfig;
import io.smallrye.stork.api.config.ServiceDiscoveryAttribute;
import io.smallrye.stork.api.config.ServiceDiscoveryType;
import io.smallrye.stork.spi.ServiceDiscoveryProvider;
import io.smallrye.stork.spi.StorkInfrastructure;

@ServiceDiscoveryType("simple")
@ServiceDiscoveryAttribute(name = "url",
        description = "Host name of the service discovery server.", required = true)
@ServiceDiscoveryAttribute(name = "service",
        description = "Host of the service discovery server.", required = false)
public class SimpleServiceDiscoveryProvider
        implements ServiceDiscoveryProvider<SimpleServiceDiscoveryProviderConfiguration> {


    @Override
    public ServiceDiscovery createServiceDiscovery(SimpleServiceDiscoveryProviderConfiguration config,
                                                   String serviceName,
                                                   ServiceConfig serviceConfig,
                                                   StorkInfrastructure storkInfrastructure) {
        return new SimpleServiceDiscovery(config);
    }
}

