package com.microservices.services;

import java.net.URI;
import java.util.List;
import java.util.Optional; 
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.microservices.model.CafeInfo;
import com.microservices.model.CafeReview;
import com.microservices.repository.CafeInfoRepository; 
  


@Service
@EnableAutoConfiguration
public class CafeInfoService {
    @Autowired
    CafeInfoRepository cafeInfoRepository;

    @Autowired
    RestTemplate restTemplate;
  

    public CafeInfo addCafe(CafeInfo cafeInfo)    {
        System.out.println("new cafeInfo details inside addCafe service is:"+cafeInfo.getCafeName()+" "+cafeInfo.getCafeDesc());
        System.out.println("repo object:"+cafeInfoRepository);
        return cafeInfoRepository.save(cafeInfo);
    }

    public List<CafeInfo> getCafes() {
        return cafeInfoRepository.findAll();
    } 
  
    public Optional<CafeInfo> getCafe(Integer cafeId) {
        return cafeInfoRepository.findById(cafeId);
    }


    //use below 2 lines while using just eureka
    //@Autowired
    //private DiscoveryClient discoveryClient;

    //use below 2 lines while using Spring Cloud Load Balancer
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    //use below 2 lines if using feign instead of restTemplate
    @Autowired
    private CafeInfoProxy cafeInfoProxy;

    @Value("${pivotal.cafereviewservice.name}")
     protected String cafereviewservice;

    //@SuppressWarnings("unchecked")

    public ModelAndView getAll(Integer cafeId)
    {
        ModelAndView mv=new ModelAndView();
        mv.addObject("cafeInfo",this.getCafe(cafeId).get());

        //below set of code might be required only if using RestTemplate
        //but while using FeignClient, only using the proxy line is enough

        //use 2 lines below if using eureka
        //List<ServiceInstance> instances=  discoveryClient.getInstances(cafereviewservice);
        //URI uri=instances.get(0).getUri();    

        //use 2 lines below if using Spring Cloud LoadBalancer
        ServiceInstance instance=loadBalancerClient.choose(cafereviewservice);
        //URI uri=instance.getUri();
        URI uri=URI.create(String.format("http://%s:%s",
                instance.getHost(),instance.getPort()));

        System.out.println("CafeReview-Service.URI========="+uri);
        String url=uri.toString()+"/getReview/"+cafeId;
        System.out.println("========================================");
        System.out.println("CafeReview-Service.URI========="+url);

        //final String uri = "http://localhost:8096/getReview/"+""+cafeId;

        //below code is when you use RestTemplate to call methods of
        //another microservice

        //List<CafeReview> result = restTemplate.getForObject(url, List.class);
        //System.out.println("details of results:"+result.toString());

        //use below line if using feign
        List<CafeReview> result=cafeInfoProxy.getReview(cafeId);
        System.out.println("details of results:"+result.toString());

        mv.addObject("cafeReview",result);

        mv.setViewName("cafeAll");
        return mv;


    } 
  
     
  
}
