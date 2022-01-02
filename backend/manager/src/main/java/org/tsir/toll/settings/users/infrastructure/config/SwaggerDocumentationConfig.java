package org.tsir.toll.settings.users.infrastructure.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.models.auth.In;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-08-19T22:34:18.669Z[GMT]")
@Configuration
public class SwaggerDocumentationConfig {

	@Bean
	public Docket customImplementation() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("org.tsir.toll.settings.users.application.presentation"))
				.build().directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
				.directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class).apiInfo(apiInfo())
				.securitySchemes(
						Collections.singletonList(new ApiKey("bearerAuth", "Authorization", In.HEADER.name())));
	}

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Users API")
				.description("Servicios para la administracion de  usuarios del sistema de peajes.").license("")
				.licenseUrl("http://unlicense.org").termsOfServiceUrl("").version("1.1.0")
				.contact(new Contact("", "", "")).build();
	}

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI().info(new Info().title("Users API")
				.description("Servicios para la administracion de  usuarios del sistema de peajes.").termsOfService("")
				.version("1.1.0").license(new License().name("").url("http://unlicense.org"))
				.contact(new io.swagger.v3.oas.models.info.Contact().email("")));
	}

}
