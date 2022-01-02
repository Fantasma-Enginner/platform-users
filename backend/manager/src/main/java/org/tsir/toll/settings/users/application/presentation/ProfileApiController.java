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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tsir.common.api.RequestHandler;
import org.tsir.common.utils.PagingUtils;
import org.tsir.toll.settings.users.application.MappingFactory;
import org.tsir.toll.settings.users.application.register.ProfilesModule;
import org.tsir.toll.settings.users.domain.dto.ProfileDTO;
import org.tsir.toll.settings.users.domain.entities.Authorization;
import org.tsir.toll.settings.users.domain.entities.Profile;
import org.tsir.toll.settings.users.domain.entities.legacy.GrupUsrs;
import org.tsir.toll.settings.users.domain.services.GroupsService;
import org.tsir.toll.settings.users.domain.services.ProfilesService;
import org.tsir.toll.settings.users.infrastructure.api.ProfilesApi;
import org.tsir.toll.settings.users.infrastructure.config.ApplicationConfig;

@RestController
@RequestMapping({ "/api/v1" })
public class ProfileApiController extends RequestHandler implements ProfilesApi {

	@Autowired
	private ApplicationConfig config;

	@Autowired
	private MappingFactory mapper;

	@Autowired
	private ProfilesService profilesService;

	@Autowired
	private GroupsService groupsService;

	@PostConstruct
	private void setMapping() {
		mapper.loadProfileMapping();
	}

	@Override
	@PreAuthorize("hasAuthority('" + ProfilesModule.FIND_PROFILES_AUTHORITY + "')")
	public ResponseEntity<List<ProfileDTO>> findProfiles(Map<String, String> filterProperties,
			Map<String, Integer> pagingProperties, Map<String, String> sortingProperties) {
		ResponseEntity<List<ProfileDTO>> response;
		if (Objects.isNull(sortingProperties)) {
			sortingProperties = Collections.emptyMap();
		}
		if (config.isActiveLegacy()) {
			response = getLegacyResponse(filterProperties, pagingProperties, sortingProperties);
		} else {
			response = getMigrationResponse(filterProperties, pagingProperties, sortingProperties);
		}
		return response;
	}

	private ResponseEntity<List<ProfileDTO>> getLegacyResponse(Map<String, String> filterProperties,
			Map<String, Integer> pagingProperties, Map<String, String> sortingProperties) {
		HttpHeaders responseHeaders = new HttpHeaders();
		List<GrupUsrs> results;
		List<ProfileDTO> body;
		if (ObjectUtils.isNotEmpty(pagingProperties)) {
			Page<GrupUsrs> page = groupsService.findProfilesPage(filterProperties, pagingProperties, sortingProperties);
			responseHeaders = PagingUtils.buildPaginationHeaders(page.getTotalPages(), page.getTotalElements());
			results = page.getContent();
		} else {
			results = groupsService.findProfilesList(filterProperties, sortingProperties);
		}
		body = results.stream().map(mapper::mapProfileDTO).collect(Collectors.toList());
		return ResponseEntity.ok().headers(responseHeaders).body(body);
	}

	private ResponseEntity<List<ProfileDTO>> getMigrationResponse(Map<String, String> filterProperties,
			Map<String, Integer> pagingProperties, Map<String, String> sortingProperties) {
		HttpHeaders responseHeaders = new HttpHeaders();
		List<Profile> results;
		List<ProfileDTO> body;
		if (ObjectUtils.isNotEmpty(pagingProperties)) {
			Page<Profile> page = profilesService.findProfilesPage(filterProperties, pagingProperties,
					sortingProperties);
			responseHeaders = PagingUtils.buildPaginationHeaders(page.getTotalPages(), page.getTotalElements());
			results = page.getContent();
		} else {
			results = profilesService.findProfilesList(filterProperties, sortingProperties);
		}
		body = results.stream().map(mapper::mapProfileDTO).collect(Collectors.toList());
		return ResponseEntity.ok().headers(responseHeaders).body(body);
	}

	@Override
	@PreAuthorize("isAuthenticated() or hasAuthority('" + ProfilesModule.GET_PROFILE_AUTHORITY + "')")
	public ResponseEntity<ProfileDTO> getProfile(Long id) {
		ProfileDTO body;
		if (config.isActiveLegacy()) {
			body = mapper.mapProfileDTO(groupsService.getProfileById(id));
		} else {
			body = mapper.mapProfileDTO(profilesService.getProfileById(id));
		}
		return ResponseEntity.ok(body);
	}

	@Override
	@PreAuthorize("hasAuthority('" + ProfilesModule.GET_AUTHORIZATIONS_AUTHORITY + "')")
	public ResponseEntity<List<Long>> getProfileResources(Long id) {
		List<Authorization> authorizations;
		if (config.isActiveLegacy()) {
			authorizations = groupsService.getProfileAuthorizations(id);
		} else {
			authorizations = profilesService.getProfileAuthorizations(id);
		}
		List<Long> body = authorizations.stream().map(Authorization::getResource).collect(Collectors.toList());
		return ResponseEntity.ok(body);
	}

	@Override
	@PreAuthorize("hasAuthority('" + ProfilesModule.GRANT_AUTHORITY + "')")
	public ResponseEntity<Void> grantResource(Long id, @Valid List<Long> body) {
		if (config.isActiveLegacy()) {
			profilesService.grantResources(id, body);
		} else {
			groupsService.grantResources(id, body);
		}
		return RESPONSE_VOID_CREATED;
	}

	@Override
	@PreAuthorize("hasAuthority('" + ProfilesModule.REGISTER_PROFILE_AUTHORITY + "')")
	public ResponseEntity<Void> registerProfile(@Valid ProfileDTO body) {
		if (config.isActiveLegacy()) {
			groupsService.createProfile(mapper.mapGrupUsrs(body));
		} else {
			profilesService.createProfile(mapper.mapProfile(body));
		}
		return RESPONSE_VOID_OK;
	}

	@Override
	@PreAuthorize("hasAuthority('" + ProfilesModule.REVOKE_AUTHORITY + "')")
	public ResponseEntity<Void> revokeResource(Long id, Long resourceId) {
		if (config.isActiveLegacy()) {
			groupsService.revokeResource(id, resourceId);
		} else {
			profilesService.revokeResource(id, resourceId);
		}

		return RESPONSE_VOID_OK;
	}

	@Override
	@PreAuthorize("hasAuthority('" + ProfilesModule.UPDATE_PROFILE_AUTHORITY + "')")
	public ResponseEntity<ProfileDTO> updateProfile(Long id, @Valid ProfileDTO body) {
		if (config.isActiveLegacy()) {
			GrupUsrs group = mapper.mapGrupUsrs(body);
			groupsService.updateProfile(id, group);
		} else {
			Profile profile = mapper.mapProfile(body);
			profilesService.updateProfile(id, profile);
		}
		return ResponseEntity.ok(body);
	}

}
