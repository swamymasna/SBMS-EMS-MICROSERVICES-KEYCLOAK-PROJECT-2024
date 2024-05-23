package com.kes.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Ms01EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ms01EmployeeServiceApplication.class, args);
	}

}
