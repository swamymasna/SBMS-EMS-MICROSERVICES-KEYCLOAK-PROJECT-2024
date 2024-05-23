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
	public OpenAPI organizationServiceAPI() {

		return new OpenAPI().info(new Info().title("Organization Service API").version("v0.0.1")
				.license(new License().name("Apache 2.0")).description("This is the REST API For Organization Service"))
				.externalDocs(new ExternalDocumentation()
						.description("You can refer to organization service wiki documentation")
						.url("https://organization-service-api/docs"));
	}
}
