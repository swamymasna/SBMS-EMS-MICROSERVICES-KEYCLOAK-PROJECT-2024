package com.kes.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class Ms03ServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ms03ServiceRegistryApplication.class, args);
	}

}
