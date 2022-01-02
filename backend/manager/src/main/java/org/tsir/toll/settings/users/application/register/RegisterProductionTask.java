package org.tsir.toll.settings.users.application.register;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.tsir.common.modules.Module;
import org.tsir.common.modules.TaskRegistrable;
import org.tsir.common.services.RegisterService;
import org.tsir.toll.settings.users.infrastructure.config.ApplicationConfig;

public class RegisterProductionTask implements TaskRegistrable {

	Logger logger = LoggerFactory.getLogger(RegisterProductionTask.class);

	@Autowired
	private RegisterService service;

	@Autowired
	private ApplicationConfig config;

	public void register() {
		List<String> results;
		String location = "/settings-users-app/main.js";
		if (!config.isActiveRegister()) {
			Module.USERS.unplubishFront();
		}
		if (config.isActiveLegacy()) {
			results = service.registerModule(
					config.isActiveRegister() ? UsersModule.INSTANCE : UsersLegacyModule.INSTANCE, location);
		} else {
			results = service.registerModule(UsersModule.INSTANCE, location);

		}
		results.addAll(service.registerModule(ProfilesModule.INSTANCE, location));
		logger.info("Registration Module Platform Home : \n{}", results);
	}

}
