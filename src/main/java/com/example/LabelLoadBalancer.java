package com.example;

import io.smallrye.stork.api.LoadBalancer;
import io.smallrye.stork.api.ServiceDiscovery;
import io.smallrye.stork.api.ServiceInstance;
import io.smallrye.stork.api.config.LoadBalancerConfig;
import io.smallrye.stork.spi.LoadBalancerProvider;

import java.util.Collection;

public class LabelLoadBalancer implements LoadBalancer {

    private final String label;
//    @Override
//    public LoadBalancer createLoadBalancer(LoadBalancerConfig config, ServiceDiscovery serviceDiscovery) {
//
//        String label = config.parameters().get("label");
//        if (label == null) {
//            throw new IllegalArgumentException("Label based load balancer requires label to be configured");
//        }
//
//        return new LoadBalancer() {
//            @Override
//            public ServiceInstance selectServiceInstance(Collection<ServiceInstance> serviceInstances) {
//                return serviceInstances.stream().filter(i -> i.getLabels().containsKey(label))
//                        .findFirst().orElseThrow(() -> new IllegalStateException("No instances found for label " + label));
//            }
//        };
//    }

    protected LabelLoadBalancer(String label) {
        this.label=label;
    }

    @Override
    public ServiceInstance selectServiceInstance(Collection<ServiceInstance> serviceInstances) {
        return serviceInstances.stream().filter(i -> i.getLabels().containsKey(label))
                .findFirst().orElseThrow(() -> new IllegalStateException("No instances found for label " + label));
    }

    @Override
    public boolean requiresStrictRecording() {
        return false;
    }
}
