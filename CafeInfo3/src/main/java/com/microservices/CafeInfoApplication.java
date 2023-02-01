package com.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients("com.microservices") //the main class of feign class package or basePackage

public class CafeInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeInfoApplication.class, args);
	}
	
	@Bean 
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
