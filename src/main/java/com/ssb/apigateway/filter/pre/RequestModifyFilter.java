package com.ssb.apigateway.filter.pre;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
	
import javax.servlet.ServletInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import com.ssb.comm.helper.JwtHelper;
import com.ssb.comm.util.HttpServletUtil;

@Component
public class RequestModifyFilter extends ZuulFilter{
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private HttpServletUtil httpServletUtil;
	
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
		
		if(claims != null) {

			byte[] commFieldBytes = httpServletUtil.getCommField(ctx.getRequest(), claims);
			
			if(commFieldBytes != null) {
				ctx.setRequest(new HttpServletRequestWrapper(ctx.getRequest()) {
					@Override
					public ServletInputStream getInputStream() throws IOException {
						return new ServletInputStreamWrapper(commFieldBytes);
					}
	
					@Override
					public int getContentLength() {
						return commFieldBytes.length;
					}
	
					@Override
					public long getContentLengthLong() {
						return commFieldBytes.length;
					}
				});
			}
			
		}
		
		
		return null;
	}

}
