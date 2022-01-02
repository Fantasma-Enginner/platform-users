package org.tsir.toll.settings.users.domain.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tsir.common.api.exceptions.DataTypeCriteriaException;
import org.tsir.common.api.exceptions.NotFoundException;
import org.tsir.common.api.exceptions.NotSupportCriteriaException;
import org.tsir.common.utils.ObjectUtilsVP;
import org.tsir.common.utils.PagingUtils;
import org.tsir.common.utils.SortingKey;
import org.tsir.toll.settings.users.domain.entities.Contact;
import org.tsir.toll.settings.users.domain.entities.Credential;
import org.tsir.toll.settings.users.domain.entities.Profile;
import org.tsir.toll.settings.users.domain.entities.QUser;
import org.tsir.toll.settings.users.domain.entities.User;
import org.tsir.toll.settings.users.domain.services.ImageService;
import org.tsir.toll.settings.users.domain.services.ProfilesService;
import org.tsir.toll.settings.users.domain.services.UsersService;
import org.tsir.toll.settings.users.domain.values.State;
import org.tsir.toll.settings.users.domain.values.UsersCriteria;
import org.tsir.toll.settings.users.infrastructure.persistence.UserRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

@Service
public class UserServiceImpl implements UsersService {

	private static final String NAME_RESOURCE = "Usuario";
	private static final String NAME_KEY = "Código";

	@Autowired
	private UserRepository userDAO;

	@Autowired
	private ProfilesService profiles;

	@Autowired
	private ImageService imageService;

	@Autowired
	private PasswordEncoder passworEnc;

	Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	public User getUserById(long code) {
		return userDAO.findById(code)
				.orElseThrow(() -> new NotFoundException(NAME_RESOURCE, NAME_KEY, String.valueOf(code)));
	}

	public boolean isUserRegistered(long code) {
		try {
			return userDAO.existsById(code);
		} catch (Exception e) {
			log.error("Fail user consult", e);
		}
		return false;
	}

	@Transactional
	public void registerUser(User data, byte[] image) {
		if (data.getState() == null) {
			data.setState(State.NUEVO.ordinal());
		}
		userDAO.save(data);
		imageService.saveImage(data.getUserId(), image);
	}

	public void updateUser(User data, byte[] image) {
		User user = findUser(data.getUserId());
		user.setFirstName(data.getFirstName());
		user.setLastName(data.getLastName());
		userDAO.save(user);
		this.updatePhoto(data.getUserId(), image);
	}

	public void updateContact(long code, Contact data) {
		User user = findUser(code);
		Contact contact = user.getContact();
		if (contact == null) {
			user.setContact(contact);
		} else {
			BeanUtils.copyProperties(data, contact);
		}
	}

	private void updatePhoto(long code, byte[] content) {
		if (content != null) {
			imageService.saveImage(code, content);
		}
	}

	@Override
	public List<User> findUsersList(Map<String, String> filterProperties, Map<String, String> sortingProperties) {
		BooleanBuilder builder = new BooleanBuilder();
		QUser qUsrs = QUser.user;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), UsersCriteria.CODE_KEY.getKey()), qUsrs);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		if (ObjectUtils.isNotEmpty(filterProperties)) {
			filterProperties.forEach((k, v) -> builder.and(getPredicate(qUsrs, k, v)));
			return IterableUtils.toList(userDAO.findAll(builder, sort));
		}
		return userDAO.findAll(sort);
	}

	@Override
	public Page<User> findUsersPage(Map<String, String> filterProperties, Map<String, Integer> pagingProperties,
			Map<String, String> sortingProperties) {
		BooleanBuilder builder = new BooleanBuilder();
		QUser qUser = QUser.user;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), UsersCriteria.CODE_KEY.getKey()), qUser);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		Pageable pageable = PagingUtils.getPageable(pagingProperties, sort);
		if (ObjectUtils.isNotEmpty(filterProperties)) {
			filterProperties.forEach((k, v) -> builder.and(getPredicate(qUser, k, v)));
			return userDAO.findAll(builder, pageable);
		}
		return userDAO.findAll(pageable);
	}

	private ComparableExpressionBase<?> getField(String field, QUser q) {
		UsersCriteria userCriteria;
		try {
			userCriteria = UsersCriteria.fromKey(field);
		} catch (NotSupportCriteriaException e) {
			return q.userId;
		}
		switch (userCriteria) {
		case CODE_KEY:
			return q.userId;
		case FIRST_NAME_KEY:
			return q.firstName;
		case LAST_NAME_KEY:
			return q.lastName;
		case STATE_KEY:
			return q.state;
		case PROFILE_KEY:
			return q.profile.profileId;
		case TOLL_KEY:
			return q.tollId;
		default:
			return q.userId;
		}
	}

	private Predicate getPredicate(QUser q, String criteria, String value) {
		UsersCriteria uc = UsersCriteria.fromKey(criteria);
		if (Objects.isNull(uc)) {
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
		switch (uc) {
		case CODE_KEY:
			return q.userId.eq(ObjectUtilsVP.getNumber(value, Long.class));
		case FIRST_NAME_KEY:
			return q.firstName.contains(value);
		case LAST_NAME_KEY:
			return q.lastName.contains(value);
		case STATE_KEY:
			State state = State.fromValue(value);
			if (Objects.isNull(state)) {
				throw new DataTypeCriteriaException(NAME_RESOURCE, criteria, Arrays.toString(State.values()));
			}
			return q.state.eq(state.ordinal());
		case PROFILE_KEY:
			return q.profile.profileId.eq(ObjectUtilsVP.getNumber(value, Long.class));
		case TOLL_KEY:
			return q.tollId.eq(ObjectUtilsVP.getNumber(value, Integer.class));
		default:
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
	}

	private User findUser(long id) {
		return userDAO.findById(id)
				.orElseThrow(() -> new NotFoundException(NAME_RESOURCE, NAME_KEY, String.valueOf(id)));
	}

	@Transactional()
	public void inactivateUser(long code) {
		User usrs = findUser(code);
		usrs.setState(State.INACTIVO.ordinal());
		usrs.setProfile(null);
		usrs.setTollId(null);
		if (usrs.getCredentials() != null) {
			usrs.getCredentials().setTiscId(null);
		}
		userDAO.save(usrs);
	}

	@Override
	public void updateEnrollment(long code, Long profile, Integer toll) {
		User user = findUser(code);
		Profile prof = profile != null ? profiles.getProfileById(profile) : null;
		user.setTollId(toll);
		user.setProfile(prof);
		userDAO.save(user);
	}

	@Override
	public void updateAuthentication(long id, Credential data) {
		User user = findUser(id);
		updateCredentials(user, data);
		userDAO.save(user);
	}

	private void updateCredentials(User user, Credential data) {
		if (user.getCredentials() == null) {
			user.setCredentials(data);
		}
		Credential credential = user.getCredentials();
		if (data.getPassword() != null) {
			credential.setPassword(passworEnc.encode(data.getPassword()));
			credential.setPsswDate(Timestamp.valueOf(LocalDateTime.now()));
		}
	}

}
