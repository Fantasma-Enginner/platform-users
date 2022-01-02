package org.tsir.toll.settings.users.application.presentation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tsir.common.api.RequestHandler;
import org.tsir.common.utils.PagingUtils;
import org.tsir.toll.settings.users.application.MappingFactory;
import org.tsir.toll.settings.users.application.register.UsersModule;
import org.tsir.toll.settings.users.domain.dto.AuthenticationDTO;
import org.tsir.toll.settings.users.domain.dto.ContactDTO;
import org.tsir.toll.settings.users.domain.dto.EnrollmentDTO;
import org.tsir.toll.settings.users.domain.dto.UserDTO;
import org.tsir.toll.settings.users.domain.dto.UserDetailDTO;
import org.tsir.toll.settings.users.domain.entities.User;
import org.tsir.toll.settings.users.domain.entities.legacy.Usrs;
import org.tsir.toll.settings.users.domain.services.UsersLegacyService;
import org.tsir.toll.settings.users.domain.services.UsersService;
import org.tsir.toll.settings.users.infrastructure.api.UsersApi;
import org.tsir.toll.settings.users.infrastructure.config.ApplicationConfig;

@RestController
@RequestMapping({ "/api/v1" })
public class UserApiController extends RequestHandler implements UsersApi {

	@Autowired
	private MappingFactory mapper;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UsersLegacyService legacyService;

	@Autowired
	private ApplicationConfig config;

	@PostConstruct
	private void setMapping() {
		mapper.loadUserMapping();
	}

	@Override
	@PreAuthorize("isAuthenticated() or hasAuthority('" + UsersModule.FIND_USERS_AUTHORITY + "')")
	public ResponseEntity<List<UserDTO>> findUsers(Map<String, String> filterProperties,
			Map<String, Integer> pagingProperties, Map<String, String> sortingProperties) {
		if (Objects.isNull(sortingProperties)) {
			sortingProperties = Collections.emptyMap();
		}
		ResponseEntity<List<UserDTO>> response;
		if (config.isActiveLegacy()) {
			response = getLegacyResponse(filterProperties, pagingProperties, sortingProperties);
		} else {
			response = getMigrationResponse(filterProperties, pagingProperties, sortingProperties);
		}
		return response;
	}

	private ResponseEntity<List<UserDTO>> getLegacyResponse(Map<String, String> filterProperties,
			Map<String, Integer> pagingProperties, Map<String, String> sortingProperties) {
		HttpHeaders responseHeaders = new HttpHeaders();
		List<Usrs> results;
		List<UserDTO> body;
		if (ObjectUtils.isNotEmpty(pagingProperties)) {
			Page<Usrs> page = legacyService.findUsersPage(filterProperties, pagingProperties, sortingProperties);
			responseHeaders = PagingUtils.buildPaginationHeaders(page.getTotalPages(), page.getTotalElements());
			results = page.getContent();
		} else {
			results = legacyService.findUsersList(filterProperties, sortingProperties);
		}
		body = results.stream().map(mapper::mapLegacyUserDTO).collect(Collectors.toList());
		return ResponseEntity.ok().headers(responseHeaders).body(body);
	}

	private ResponseEntity<List<UserDTO>> getMigrationResponse(Map<String, String> filterProperties,
			Map<String, Integer> pagingProperties, Map<String, String> sortingProperties) {
		HttpHeaders responseHeaders = new HttpHeaders();
		List<User> results;
		List<UserDTO> body;
		if (ObjectUtils.isNotEmpty(pagingProperties)) {
			Page<User> page = usersService.findUsersPage(filterProperties, pagingProperties, sortingProperties);
			responseHeaders = PagingUtils.buildPaginationHeaders(page.getTotalPages(), page.getTotalElements());
			results = page.getContent();
		} else {
			results = usersService.findUsersList(filterProperties, sortingProperties);
		}
		body = results.stream().map(mapper::mapUserDTO).collect(Collectors.toList());
		return ResponseEntity.ok().headers(responseHeaders).body(body);
	}

	@Override
	@PreAuthorize("#id.toString() == authentication.name or hasAuthority('" + UsersModule.GET_USER_AUTHORITY + "')")
	public ResponseEntity<UserDetailDTO> getUser(Long id) {
		UserDetailDTO body;
		if (config.isActiveLegacy()) {
			body = mapper.mapUserDetailDTO(legacyService.getUserByCode(id));
		} else {
			body = mapper.mapUserDetailDTO(usersService.getUserById(id));
		}
		return ResponseEntity.ok(body);
	}

	@Override
	@PreAuthorize("hasAuthority('" + UsersModule.REGISTER_USER_AUTHORITY + "')")
	public ResponseEntity<Void> registerUser(@Valid UserDTO body) {
		usersService.registerUser(mapper.mapUser(body), body.getPhoto());
		return RESPONSE_VOID_CREATED;
	}

	@Override
	@PreAuthorize("hasAuthority('" + UsersModule.AUTHENTICATION_AUTHORITY + "')")
	public ResponseEntity<Void> updateAuthentication(Long id, @Valid AuthenticationDTO body) {
		usersService.updateAuthentication(id, mapper.mapCredential(body));
		return RESPONSE_VOID_OK;
	}

	@Override
	@PreAuthorize("hasAuthority('" + UsersModule.ENROLLMENT_AUTHORITY + "')")
	public ResponseEntity<Void> updateEnrollment(Long id, @Valid EnrollmentDTO body) {
		usersService.updateEnrollment(id, body.getProfile(), body.getToll());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	@PreAuthorize("hasAuthority('" + UsersModule.UPDATE_USER_AUTHORITY + "')")
	public ResponseEntity<Void> updateUser(Long id, @Valid UserDTO body) {
		usersService.updateUser(mapper.mapUser(body), body.getPhoto());
		return RESPONSE_VOID_OK;
	}

	@Override
	public ResponseEntity<Void> updateContact(Long code, @Valid ContactDTO body) {
		usersService.updateContact(code, mapper.mapContact(body));
		return RESPONSE_VOID_OK;
	}

	@Override
	@PreAuthorize("permitAll")
	public ResponseEntity<Void> login(@Valid AuthenticationDTO body) {
		/*
		 * the login operation is realized into AuthenticationFilter the method is for
		 * documentation purposes.
		 */
		return ResponseEntity.noContent().build();
	}

	@Override
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> logout() {
		/*
		 * Should notify to queue system to disable the token.
		 */
		return ResponseEntity.noContent().build();
	}
}
