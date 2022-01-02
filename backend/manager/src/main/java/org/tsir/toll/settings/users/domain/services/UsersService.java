package org.tsir.toll.settings.users.domain.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.tsir.toll.settings.users.domain.entities.Contact;
import org.tsir.toll.settings.users.domain.entities.Credential;
import org.tsir.toll.settings.users.domain.entities.User;

public interface UsersService {

	User getUserById(long code);

	boolean isUserRegistered(long code);

	void registerUser(User user, byte[] image);

	void updateUser(User data, byte[] image);

	List<User> findUsersList(Map<String, String> filter, Map<String, String> sortingProperties);

	Page<User> findUsersPage(Map<String, String> mapFilter, Map<String, Integer> pagingProperties,
			Map<String, String> sortingProperties);

	void inactivateUser(long code);

	void updateEnrollment(long code, Long profile, Integer toll);

	void updateAuthentication(long id, Credential body);

	void updateContact(long id, Contact contact);

}
