package com.ssb.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@ComponentScan({"com.ssb.apigateway.*","com.ssb.comm.*"})
public class ApiGatewayFoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayFoApplication.class, args);
	}

}
