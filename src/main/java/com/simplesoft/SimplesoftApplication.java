package com.simplesoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.simplesoft"})
@SpringBootApplication
public class SimplesoftApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SimplesoftApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SimplesoftApplication.class);
	}
}
