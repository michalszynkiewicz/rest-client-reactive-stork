package com.example;

import io.smallrye.stork.LoadBalancer;
import io.smallrye.stork.ServiceDiscovery;
import io.smallrye.stork.ServiceInstance;
import io.smallrye.stork.config.LoadBalancerConfig;
import io.smallrye.stork.spi.LoadBalancerProvider;

import java.util.Collection;

public class LabelLoadBalancer implements LoadBalancerProvider {
    @Override
    public LoadBalancer createLoadBalancer(LoadBalancerConfig config, ServiceDiscovery serviceDiscovery) {

        String label = config.parameters().get("label");
        if (label == null) {
            throw new IllegalArgumentException("Label based load balancer requires label to be configured");
        }

        return new LoadBalancer() {
            @Override
            public ServiceInstance selectServiceInstance(Collection<ServiceInstance> serviceInstances) {
                return serviceInstances.stream().filter(i -> i.getLabels().containsKey(label))
                        .findFirst().orElseThrow(() -> new IllegalStateException("No instances found for label " + label));
            }
        };
    }

    @Override
    public String type() {
        return "label";
    }
}
