package com.ssb.apigateway.comm.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayCommResponse {
	
	private int resultCode;
	
	private String resultMsg;
	
	private Object data;
	
	public GatewayCommResponse(int resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

}
