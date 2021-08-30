package com.ssb.apigateway.filter.pre;

import java.util.Collections;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ssb.apigateway.comm.response.GatewayCommResponse;
import com.ssb.comm.constant.CommJwtConstant;
import com.ssb.comm.constant.CommResponseConstant;
import com.ssb.comm.helper.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorizeFilter extends ZuulFilter{
	
	@Autowired
	private JwtHelper jwtHelper;
	
	private String filterType = "pre";
	
	private boolean shouldFilter = true;
	
	private int filterOrder = 0;

	@Override
	public boolean shouldFilter() {
		return shouldFilter;
	}

	@Override
	public String filterType() {
		return filterType;
	}

	@Override
	public int filterOrder() {
		return filterOrder;
	}
	
	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		
		if(!jwtHelper.getTokenValid(ctx.getRequest())) {
			
			ctx.setSendZuulResponse(false);
			ctx.setResponseBody(new Gson().toJson(new GatewayCommResponse(CommResponseConstant.AUTHORIZE.getResultCode(), CommResponseConstant.AUTHORIZE.getResultMsg())));
			ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
		
		}
		
		return null;
	}

}
