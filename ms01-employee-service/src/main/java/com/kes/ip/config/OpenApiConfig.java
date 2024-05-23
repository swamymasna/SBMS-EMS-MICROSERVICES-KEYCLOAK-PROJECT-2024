package com.kes.ip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI employeeServiceAPI() {

		return new OpenAPI().info(new Info().title("Employee Service API").version("v0.0.1")
				.license(new License().name("Apache 2.0")).description("This is the REST API For Employee Service"))
				.externalDocs(
						new ExternalDocumentation().description("You can refer to employee service wiki documentation")
								.url("https://employee-service-api/docs"));
	}
}
