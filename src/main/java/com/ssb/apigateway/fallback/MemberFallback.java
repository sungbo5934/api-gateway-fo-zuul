package com.ssb.apigateway.fallback;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssb.apigateway.comm.response.GatewayClientResponse;
import com.ssb.apigateway.comm.response.GatewayCommResponse;
import com.ssb.comm.constant.CommResponseConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		GatewayCommResponse commResponse = new GatewayCommResponse(CommResponseConstant.ERROR.getResultCode(), cause.getLocalizedMessage());
		ObjectMapper mapper = new ObjectMapper();
		String resultStr = StringUtils.EMPTY;
		try {
			resultStr = mapper.writeValueAsString(commResponse);
		} catch (JsonProcessingException e) {
			log.error("Memer Fallback : "+ e);
		}
		//return new GatewayClientResponse(HttpStatus.BAD_GATEWAY, messageSource.getMessage("hystrix.fallback.member.error"));
		return new GatewayClientResponse(HttpStatus.BAD_GATEWAY, resultStr);
	}

}
