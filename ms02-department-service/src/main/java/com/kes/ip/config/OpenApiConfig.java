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
	public OpenAPI departmentServiceAPI() {

		return new OpenAPI().info(new Info().title("Department Service API").version("v0.0.1")
				.license(new License().name("Apache 2.0")).description("This is the REST API For Department Service"))
				.externalDocs(
						new ExternalDocumentation().description("You can refer to department service wiki documentation")
								.url("https://department-service-api/docs"));
	}
}
