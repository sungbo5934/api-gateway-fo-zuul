package com.ssb.apigateway.comm.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssb.apigateway.comm.constant.ApiGatewayConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	
	@Value("${jwt.apikey}")
	private String apiKey;
	
	@Value("${jwt.login.memberKey}")
	private String memberId;
	
	@Value("${jwt.login.valid.minute}")
	private int loginTokenValidMin;
	
	@Value("${jwt.access.valid.minute}")
	private int accessTokenValidMin;
	
	@Value("${jwt.refresh.valid.day}")
	private int refreshTokenValidDay;
	
	@Value("${header.authorization}")
	private String authHeader;
	
	// token header ( 알고리즘 , type ) 
	private Map<String, Object> getHeader() {
		
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", ApiGatewayConstant.TOKEN_HEADER_TYP.getValue());
        headers.put("alg", ApiGatewayConstant.TOKEN_HEADER_ALG.getValue());
        
	    return headers;
	}
	
	// token sign key 인코딩 셋팅
	private Key getSigninKey(String secretKey) {
	    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
	    return Keys.hmacShaKeyFor(keyBytes);
	}

	// access token 시간
	public long getLoginTokenTime() {
		return 1000l * 60 * loginTokenValidMin;
	}
	
	// access token 시간
	public long getAccessTokenTime() {
		return 1000l * 60 * accessTokenValidMin;
	}

	// refresh token 시간
	public long getRefreshTokenTime() {
		return 1000l * 60 *  60 * 24 * refreshTokenValidDay;
	}
	
	//검증
		@SuppressWarnings("unchecked")
		public boolean requestTokenChk(HttpServletRequest request) {
			
			boolean tokenValid = false;
			String token = request.getHeader(authHeader);
			
			try {
				
				Claims claims= Jwts.parserBuilder()
						.setSigningKey(getSigninKey(apiKey))
						.build()
						.parseClaimsJwt(token)
						.getBody();
		
				//null 이아니면 새로운 토큰을 발급해준다. 로그인 연장
				if(claims.get(memberId) != null) {
					String newToken = doGenerateToken(ApiGatewayConstant.TOKEN_LOGIN_TYPE.getValue()
														, (Map<String, Object>)claims.get(memberId));
				}
				
			}catch(Exception e) {
				log.info("Token is Not Valide");
			}
			
			return tokenValid;
		}
	
	
	//토큰 생성
	public String doGenerateToken(String type, Map<String, Object> claim) {
		/**
			iss: 토큰 발급자(issuer)
			sub: 토큰 제목(subject)
			aud: 토큰 대상자(audience)
			exp: 토큰 만료 시간(expiration), NumericDate 형식으로 되어 있어야 함 ex) 1480849147370
			nbf: 토큰 활성 날짜(not before), 이 날이 지나기 전의 토큰은 활성화되지 않음
			iat: 토큰 발급 시간(issued at), 토큰 발급 이후의 경과 시간을 알 수 있음
			jti: JWT 토큰 식별자(JWT ID), 중복 방지를 위해 사용하며, 일회용 토큰(Access Token) 등에 사용
		 **/
        
		Date expireTime = new Date();
		
		if(StringUtils.equals(type, ApiGatewayConstant.TOKEN_LOGIN_TYPE.getValue())) {
			expireTime.setTime(expireTime.getTime() + getLoginTokenTime());
		}
		
		if(StringUtils.equals(type, ApiGatewayConstant.TOKEN_ACCESS_TYPE.getValue())) {
			expireTime.setTime(expireTime.getTime() + getAccessTokenTime());
		}

		if(StringUtils.equals(type, ApiGatewayConstant.TOKEN_REFRESH_TYPE.getValue())) {
			expireTime.setTime(expireTime.getTime() + getRefreshTokenTime());
		}
		
		String token = Jwts.builder()
						.setHeader(getHeader())
						.setIssuer("ssb")
						.setSubject("ssbToken")
						.setExpiration(expireTime)
						.setNotBefore(new Date())
						.setClaims(Optional.ofNullable(claim).orElseGet(() -> {
							return new HashMap<String, Object>();
							}))
						.signWith(getSigninKey(apiKey), SignatureAlgorithm.HS256)
						.compact();
		
		return token;
	}
	
}
