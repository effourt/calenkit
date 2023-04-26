package com.effourt.calenkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.effourt.calenkit.client"})
public class CalenkitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalenkitApplication.class, args);
	}

}
