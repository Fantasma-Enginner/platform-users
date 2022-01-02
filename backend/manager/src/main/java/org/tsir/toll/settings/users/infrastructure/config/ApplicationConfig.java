package org.tsir.toll.settings.users.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.tsir.common.modules.TaskRegistrable;
import org.tsir.common.services.CommunicationsService;
import org.tsir.common.services.RegisterService;
import org.tsir.common.services.ResourcesService;
import org.tsir.common.utils.ObjectUtilsVP;
import org.tsir.toll.settings.users.application.register.RegisterProductionTask;

@Configuration
@SuppressWarnings("deprecation")
public class ApplicationConfig {

	public static final String API_VERSION = "1.1.0";
	public static final String IMPLEMENTATION_VERSION = "1.0.1";
	public static final String MODULE_DESCRIPTION = "Servicios para la gestión del sistema de peajes.";
	public static final String MODULE_NAME = "Operation-Users";

	@Value("${vialplus.settings.users.legacy}")
	private boolean activeLegacy;

	@Value("${vialplus.settings.users.password.validity}")
	private int passwordValidityDays;
	
	@Value("${vialplus.settings.users.register}")
	private boolean activeRegister;
	

	public boolean isActiveLegacy() {
		return activeLegacy;
	}

	public int getPasswordValidityDays() {
		return passwordValidityDays;
	}
	
	public boolean isActiveRegister() {
		return activeRegister;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PasswordEncoder createPasswordEncoder() {
		MessageDigestPasswordEncoder defaultEncoder = new MessageDigestPasswordEncoder("MD5");
		defaultEncoder.setEncodeHashAsBase64(true);
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		DelegatingPasswordEncoder encodeDelegator = new DelegatingPasswordEncoder("bcrypt", encoders);
		encodeDelegator.setDefaultPasswordEncoderForMatches(defaultEncoder);
		return encodeDelegator;
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder getWebClient() {
		return ObjectUtilsVP.getClient();
	}

	@Bean()
	public CommunicationsService getCommunicationsService() {
		CommunicationsService commService = new CommunicationsService();
		commService.setHost("communications-integration");
		return commService;
	}

	@Bean()
	public RegisterService getRegisterServiceDev() {
		RegisterService service = new RegisterService();
		service.setHost("platform-manager");
		return service;
	}

	@Bean()
	public ResourcesService getResourcesServiceDev() {
		ResourcesService service = new ResourcesService();
		service.setHost("platform-manager");
		return service;
	}

	@Bean
	public TaskRegistrable getRegisterProductionTask() {
		return new RegisterProductionTask();
	}



}
