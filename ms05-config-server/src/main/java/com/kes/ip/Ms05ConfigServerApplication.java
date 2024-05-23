package com.kes.ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class Ms05ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ms05ConfigServerApplication.class, args);
	}

}
