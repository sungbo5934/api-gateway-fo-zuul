package com.ssb.apigateway.comm.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogAspect {

	@Around("execution(* com.ssb.apigateway..*(..))")
	public Object logging(ProceedingJoinPoint pjp) throws Throwable {
		log.info("Api Gateway start : " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
		Object result = pjp.proceed();
		log.info("Api Gateway end : " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
		return result;
	}
}
