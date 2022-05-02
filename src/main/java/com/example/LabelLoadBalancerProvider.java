package com.example;

import io.smallrye.stork.api.LoadBalancer;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.config.LoadBalancerConfig;
import io.smallrye.stork.spi.LoadBalancerProvider;

public class LabelLoadBalancerProvider implements LoadBalancerProvider<LoadBalancerConfig> {
    @Override
    public LoadBalancer createLoadBalancer(LoadBalancerConfig config, ServiceDiscovery serviceDiscovery) {
        String label = config.parameters().get("label");
        if (label == null) {
            throw new IllegalArgumentException("Label based load balancer requires label to be configured");
        }
        return new LabelLoadBalancer(label);
    }
}
