package com.ssb.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ApiGatewayFoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayFoApplication.class, args);
	}

}
