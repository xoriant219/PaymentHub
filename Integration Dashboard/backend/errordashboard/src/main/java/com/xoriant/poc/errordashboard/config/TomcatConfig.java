package com.xoriant.poc.errordashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

	@Value("${server.http.port}")
	private int httpPort;

	@Value("${server.http.interface}")
	private String httpInterface;

	@Bean
	public WebServerFactoryCustomizer<UndertowServletWebServerFactory> containerCustomizer() {
	    return (WebServerFactoryCustomizer) factory -> {
	        UndertowServletWebServerFactory undertowFactory = (UndertowServletWebServerFactory) factory;
	        undertowFactory.getBuilderCustomizers().add(builder -> {
	            builder.addHttpListener(httpPort, httpInterface);
	        });
	    };
	}
}
