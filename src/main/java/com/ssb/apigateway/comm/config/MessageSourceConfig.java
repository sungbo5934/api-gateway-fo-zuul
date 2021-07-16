package com.ssb.apigateway.comm.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {
	
	@Value("${message.cacheSecond}")
	private int messageCacheSecond;
	
	@Bean
	public MessageSource messageSource() {
		
		var messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/messages/message");
		messageSource.setDefaultLocale(Locale.KOREA);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(messageCacheSecond);
		
		return messageSource;
	}
	
	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		var messageSource = messageSource();
		return new MessageSourceAccessor(messageSource);
	}

}
