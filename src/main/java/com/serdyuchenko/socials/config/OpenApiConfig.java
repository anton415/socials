package com.serdyuchenko.socials.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Value("${socials.openapi.dev-url}")
	private String devUrl;

	@Value("${socials.openapi.prod-url}")
	private String prodUrl;

	@Bean
	public OpenAPI socialsOpenAPI() {
		final Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Development server");

		final Server prodServer = new Server();
		prodServer.setUrl(prodUrl);
		prodServer.setDescription("Production server");

		final Contact contact = new Contact();
		contact.setName("Socials API");

		final Info info = new Info()
			.title("Socials API")
			.version("1.0")
			.description("Документация REST API сервиса Socials")
			.contact(contact);

		return new OpenAPI()
			.info(info)
			.servers(List.of(devServer, prodServer));
	}
}
