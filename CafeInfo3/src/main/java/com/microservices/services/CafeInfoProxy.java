package com.microservices.services;

import java.util.List; 

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservices.model.CafeReview;
  
@FeignClient(value = "CafeReview-Service") // the application name which we have to make call

@LoadBalancerClient(name="CafeReview-Service",configuration=CloudProviderConfiguration.class) 
// load balancer configuration class  it an optional 

public interface CafeInfoProxy {

    @GetMapping("/getReview/{cafeId}")
    public List<CafeReview> getReview(@PathVariable("cafeId")Integer cafeId);
}


//Open feign he is alternative for rest template 