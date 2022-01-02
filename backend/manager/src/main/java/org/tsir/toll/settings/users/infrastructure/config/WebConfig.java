package org.tsir.toll.settings.users.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.tsir.common.utils.MapIntegerConverter;
import org.tsir.common.utils.MapStringConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new MapStringConverter());
		registry.addConverter(new MapIntegerConverter());
	}
}