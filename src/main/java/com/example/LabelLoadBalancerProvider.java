package com.example;

import io.smallrye.stork.api.LoadBalancer;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.config.LoadBalancerAttribute;
import io.smallrye.stork.api.config.LoadBalancerType;
import io.smallrye.stork.spi.LoadBalancerProvider;

@LoadBalancerType("label")
@LoadBalancerAttribute(name = "label",
        description = "Attribute that alters the behavior of the LoadBalancer")
public class LabelLoadBalancerProvider implements LoadBalancerProvider<LabelLoadBalancerProviderConfiguration> {
    @Override
    public LoadBalancer createLoadBalancer(LabelLoadBalancerProviderConfiguration config, ServiceDiscovery serviceDiscovery) {
        String label = config.getLabel();
        if (label == null) {
            throw new IllegalArgumentException("Label based load balancer requires label to be configured");
        }
        return new LabelLoadBalancer(label);
    }
}
