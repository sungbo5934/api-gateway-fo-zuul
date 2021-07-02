package com.ssb.apigateway.filter.pre;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class AuthorizeFilter extends ZuulFilter{
	
	@Value("${header.authorization}")
	private String authHeader;
	
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
		HttpServletRequest request =  ctx.getRequest();
		String authToken = request.getHeader(authHeader);
		
		if(!validateToken(authToken)) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseBody("Api Key is not Validate");
			ctx.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
			
		}
		
		return null;
	}
	
	private boolean validateToken(String tokenHeader) {
		boolean tokenValide = false;
		Optional.ofNullable(tokenHeader);
        return tokenValide;
    }

}
