package org.tsir.toll.settings.users.infrastructure.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.tsir.common.api.security.JWTAuthorizationFilter;
import org.tsir.toll.settings.users.infrastructure.config.ApplicationConfig;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = { "/api-docs/**", "/swagger-resources", "/swagger-resources/**",
			"/swagger-ui/**", "/configuration/ui", "/configuration/security", "/webjars/**", "/actuator",
			"/actuator/**", "/wvp-resources/**", "/test/**", "/registration" };

	private UserDetailsService userDetailsService;

	@Autowired
	private ApplicationConfig appConfig;

	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		/*
		 * 1. Se desactiva el uso de cookies 2. Se activa la configuración CORS con los
		 * valores por defecto 3. Se desactiva el filtro CSRF 4. Se indica que el login
		 * no requiere autenticación 5. Se indica que el resto de URLs esten securizadas
		 */
		httpSecurity

				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				.cors().and()

				.csrf().disable()

				.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()

				.anyRequest().authenticated().and()

				.exceptionHandling().authenticationEntryPoint(forbiddenEntryPoint()).and()

				.addFilter(new JWTAuthenticationFilter(authenticationManager()))

				.addFilter(new JWTAuthorizationFilter(authenticationManager()));

//				.requiresChannel().anyRequest().requiresSecure();

	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Se define la clase que recupera los usuarios y el algoritmo para procesar las
		// passwords
		auth.userDetailsService(userDetailsService).passwordEncoder(appConfig.createPasswordEncoder());
	}

	@Bean
	AuthenticationEntryPoint forbiddenEntryPoint() {
		return new AuthenticationEntryPoint() {

			@Autowired
			@Qualifier("handlerExceptionResolver")
			private HandlerExceptionResolver resolver;

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				resolver.resolveException(request, response, null, authException);
			}
		};
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE"));
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}
}
