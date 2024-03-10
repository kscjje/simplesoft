package com.simplesoft;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.simplesoft"})
@SpringBootApplication
public class SimplesoftApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplesoftApplication.class, args);
	}

}
