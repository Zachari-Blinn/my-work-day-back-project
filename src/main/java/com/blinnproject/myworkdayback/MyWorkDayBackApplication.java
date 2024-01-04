package com.blinnproject.myworkdayback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyWorkDayBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyWorkDayBackApplication.class, args);
	}

}
