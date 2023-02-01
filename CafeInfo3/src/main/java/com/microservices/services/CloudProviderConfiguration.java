package com.microservices.services;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean; 
  
class CloudProviderConfiguration {
    
	
	@Bean
    public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {
          return ServiceInstanceListSupplier.builder()
                 .withBlockingDiscoveryClient()             
                 .withZonePreference()
                 //.withSameInstancePreference()                             
                .build(context);
        }
    }

