package org.tsir.toll.settings.users.domain.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.tsir.toll.settings.users.domain.entities.legacy.Usrs;

public interface UsersLegacyService {

	Usrs getUserByCode(Long code);

	void registerUser(Usrs usrs);

	void updateUser(Long code, Usrs usrs);

	boolean isUserRegistered(Long code);

	List<Usrs> findUsersList(Map<String, String> mapFilter, Map<String, String> sortingProperties);

	Page<Usrs> findUsersPage(Map<String, String> mapFilter, Map<String, Integer> pagingProperties,
			Map<String, String> sortingProperties);

}
