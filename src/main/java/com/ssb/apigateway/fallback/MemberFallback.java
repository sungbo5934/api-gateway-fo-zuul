package com.ssb.apigateway.fallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.ssb.apigateway.comm.response.GatewayClientResponse;

@Component
public class MemberFallback implements FallbackProvider{
	
	@Autowired
	private MessageSourceAccessor messageSource;

	@Override
	public String getRoute() {
		return "member";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		 return new GatewayClientResponse(HttpStatus.BAD_GATEWAY, messageSource.getMessage("hystrix.fallback.member.error"));
	}

}
