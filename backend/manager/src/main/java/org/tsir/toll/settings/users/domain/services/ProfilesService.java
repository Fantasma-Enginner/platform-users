package org.tsir.toll.settings.users.domain.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.tsir.toll.settings.users.domain.entities.Authorization;
import org.tsir.toll.settings.users.domain.entities.Profile;

public interface ProfilesService {

	void createProfile(Profile body);

	List<Profile> findProfilesList(Map<String, String> mapFilter, Map<String, String> sortingProperties);

	Page<Profile> findProfilesPage(Map<String, String> mapFilter, Map<String, Integer> pagingProperties,
			Map<String, String> sortingProperties);

	Profile getProfileById(Long id);

	void updateProfile(Long id, Profile profile);

	List<Authorization> getProfileAuthorizations(Long id);

	void grantResources(Long profile, List<Long> resources);

	void revokeResource(Long profile, Long resource);
}
