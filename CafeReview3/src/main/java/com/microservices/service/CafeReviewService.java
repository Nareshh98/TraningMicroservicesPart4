package com.microservices.service;

import java.util.ArrayList;
import java.util.List; 
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservices.model.CafeReview;
import com.microservices.repository.CafeReviewRepository; 
  

  
@Service
public class CafeReviewService {

    @Autowired
    CafeReviewRepository cafeReviewRepository;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory; 
  

    public CafeReview addReview(CafeReview cafeReview) {
        // TODO Auto-generated method stub
        return cafeReviewRepository.save(cafeReview);
    } 
  
    public List<CafeReview> getReviews() {
        // TODO Auto-generated method stub
        return cafeReviewRepository.findAll();
    } 
  
    public List<CafeReview> getReview(Integer cafeId) {
        System.out.println("Inside the getReview Service method");
        //return cafeReviewRepository.findAllByCafeId(cafeId);     
  
        RestTemplate restTemplate=new RestTemplate();

        //return restTemplate.getForObject(url, List.class) ;

        String url="https://api.zomato.com/v1/reviews.json/";


        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("GetReviewCircuitBreaker");
        return (List<CafeReview>) circuitBreaker.run(() -> restTemplate.getForObject(url, List.class), 
                throwable -> getDefaultReviews());

    } 
  
    private List<CafeReview> getDefaultReviews() {
        List<CafeReview> deftReviews=new ArrayList<CafeReview>();
        deftReviews.add(new CafeReview(1,5,"Good Vegan Cafe"));
        deftReviews.add(new CafeReview(1,3,"Limited choice"));
        deftReviews.add(new CafeReview(1,4,"Great Ambience"));
        return deftReviews;
    } 
  
}

