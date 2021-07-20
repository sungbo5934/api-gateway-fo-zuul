package com.ssb.apigateway.filter.post;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ssb.comm.helper.JwtHelper;
import com.ssb.comm.util.HttpServletUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RefreshTokenFilter extends ZuulFilter{
	
	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private HttpServletUtil httpServletUtil;
	
	@Value("${header.authorization}")
	private String authHeader;
	
	private String filterType = "post";
	
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
	
	// ctx.getZuulResponseHeaders() - Service에서 보내준 Response Header
	// ctx.getOriginResponseHeaders() - Client에게 보낼 최종 Response Header
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		List<Pair<String, String>>  test = ctx.getZuulResponseHeaders();
		List<Pair<String, String>>  test2 = ctx.getOriginResponseHeaders();
		String authToken = ctx.getResponse().getHeader(authHeader);
		for(String name : ctx.getResponse().getHeaderNames()) {
			log.info(ctx.getResponse().getHeader(name));
		}
		if(StringUtils.isEmpty(authToken)) {
			httpServletUtil.setResLoginToken(ctx.getResponse(), jwtHelper.getTokenClaims(ctx.getRequest()));
		}
		return null;
	}

}
