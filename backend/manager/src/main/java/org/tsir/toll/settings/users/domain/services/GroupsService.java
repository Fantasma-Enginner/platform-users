package org.tsir.toll.settings.users.domain.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.tsir.toll.settings.users.domain.entities.Authorization;
import org.tsir.toll.settings.users.domain.entities.legacy.GrupUsrs;

public interface GroupsService {

	void createProfile(GrupUsrs body);

	List<GrupUsrs> findProfilesList(Map<String, String> mapFilters, Map<String, String> sortingProperties);

	Page<GrupUsrs> findProfilesPage(Map<String, String> mapFilters, Map<String, Integer> pagingProperties,
			Map<String, String> sortingProperties);

	GrupUsrs getProfileById(Long id);

	void updateProfile(Long id, GrupUsrs profile);

	List<Authorization> getProfileAuthorizations(Long id);

	void grantResources(Long profileId, List<Long> resources);

	void revokeResource(Long profileId, Long resource);
}
