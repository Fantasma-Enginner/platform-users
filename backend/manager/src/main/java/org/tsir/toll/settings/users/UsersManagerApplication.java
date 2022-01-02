package org.tsir.toll.settings.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.tsir.common.api.exceptions.handler.rest.RestHandler;
import org.tsir.common.api.exceptions.handler.security.SecuredRestHandler;

import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableDiscoveryClient
@ComponentScan(basePackageClasses = { UsersManagerApplication.class, RestHandler.class, SecuredRestHandler.class})
@EnableJpaRepositories
public class UsersManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersManagerApplication.class, args);
	}

}
