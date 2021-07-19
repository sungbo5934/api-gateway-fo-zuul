package com.ssb.apigateway.filter.pre;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ssb.comm.helper.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorizeFilter extends ZuulFilter{
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private MessageSourceAccessor messageSource;
	
	private String filterType = "pre";
	
	private boolean shouldFilter = true;
	
	private int filterOrder = 1;

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
		Map<String, Object> claims = jwtHelper.getTokenClaims(ctx.getRequest());
		
		if(claims == null) {
			
			ctx.setSendZuulResponse(false);
			ctx.setResponseBody(messageSource.getMessage("zuul.pre.unauthor"));
			ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
			
		}else {
			
			jwtHelper.setResLoginToken(ctx.getResponse(), claims);
			
		}
		
		return null;
	}

}
