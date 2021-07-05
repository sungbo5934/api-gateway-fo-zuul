package com.ssb.apigateway.comm.constant;


public enum ApiGatewayConstant{
	HEADER_BEARER_SPLIT("Bearer ");

	private final String value; //
	
	ApiGatewayConstant(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
