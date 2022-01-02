package org.tsir.toll.settings.users.infrastructure.security;

import static org.tsir.common.api.security.Constants.HEADER_AUTHORIZACION_KEY;
import static org.tsir.common.api.security.Constants.ISSUER_INFO;
import static org.tsir.common.api.security.Constants.SUPER_SECRET_KEY;
import static org.tsir.common.api.security.Constants.TOKEN_BEARER_PREFIX;
import static org.tsir.common.api.security.Constants.TOKEN_EXPIRATION_TIME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.tsir.toll.settings.users.domain.dto.AuthenticationDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl("/api/v1/users/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			AuthenticationDTO credenciales = new ObjectMapper().readValue(request.getInputStream(),
					AuthenticationDTO.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credenciales.getUser(),
					credenciales.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new AuthenticationCredentialsNotFoundException("Error al obtener credenciales");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String authorizations = auth.getAuthorities().stream().map(GrantedAuthority::toString)
				.collect(Collectors.joining(","));
		String token = Jwts.builder().setIssuedAt(new Date()).setIssuer(ISSUER_INFO)
				.setSubject(((User) auth.getPrincipal()).getUsername()).claim("aut", authorizations).setAudience("node")
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY).compact();
		response.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + " " + token);
		response.addHeader("Access-Control-Expose-Headers", HEADER_AUTHORIZACION_KEY);
	}
}
