package org.tsir.toll.settings.users.domain.services.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.tsir.common.api.exceptions.ConflictException;
import org.tsir.common.api.exceptions.DataTypeCriteriaException;
import org.tsir.common.api.exceptions.NotFoundException;
import org.tsir.common.api.exceptions.NotSupportCriteriaException;
import org.tsir.common.api.exceptions.ServerProcessException;
import org.tsir.common.utils.ObjectUtilsVP;
import org.tsir.common.utils.PagingUtils;
import org.tsir.common.utils.SortingKey;
import org.tsir.toll.settings.users.domain.entities.Authorization;
import org.tsir.toll.settings.users.domain.entities.Profile;
import org.tsir.toll.settings.users.domain.entities.QAuthorization;
import org.tsir.toll.settings.users.domain.entities.QProfile;
import org.tsir.toll.settings.users.domain.services.ProfilesService;
import org.tsir.toll.settings.users.domain.values.ProfilesCriteria;
import org.tsir.toll.settings.users.domain.values.State;
import org.tsir.toll.settings.users.infrastructure.persistence.AuthorizationRepository;
import org.tsir.toll.settings.users.infrastructure.persistence.ProfileRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

@Service
public class ProfileServiceImpl implements ProfilesService {

	Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

	private static final String NAME_RESOURCE = "Perfil";
	private static final String CODE_KEY = "Código";
	private static final String NAME_KEY = "Nombre";

	@Autowired
	private ProfileRepository profileDAO;

	@Autowired
	private AuthorizationRepository authorizationDAO;

	@Override
	public Profile getProfileById(Long id) {
		Optional<Profile> opt = profileDAO.findById(id);
		return opt.orElseThrow(() -> new NotFoundException(NAME_RESOURCE, CODE_KEY, id.toString()));
	}

	@Override
	public void createProfile(Profile profile) {
		checkProfile(profile, true);
		if (profile.getState() == null) {
			profile.setState(String.valueOf(State.INACTIVO.ordinal()));
		}
		profileDAO.save(profile);
	}

	@Override
	public List<Profile> findProfilesList(Map<String, String> mapFilter, Map<String, String> sortingProperties) {
		QProfile qProfile = QProfile.profile;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), ProfilesCriteria.CODE_KEY.toString()),
				qProfile);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		if (!mapFilter.isEmpty()) {
			BooleanBuilder builder = new BooleanBuilder();
			mapFilter.forEach((k, v) -> builder.and(getPredicate(qProfile, k, v)));
			return IterableUtils.toList(profileDAO.findAll(builder, sort));
		}
		return profileDAO.findAll(sort);
	}

	@Override
	public Page<Profile> findProfilesPage(Map<String, String> mapFilter, Map<String, Integer> pagingProperties,
			Map<String, String> sortingProperties) {
		QProfile qProfile = QProfile.profile;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), ProfilesCriteria.CODE_KEY.toString()),
				qProfile);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		Pageable pageable = PagingUtils.getPageable(pagingProperties, sort);
		if (!mapFilter.isEmpty()) {
			BooleanBuilder builder = new BooleanBuilder();
			mapFilter.forEach((k, v) -> builder.and(getPredicate(qProfile, k, v)));
			return profileDAO.findAll(builder, pageable);
		}
		return profileDAO.findAll(pageable);
	}

	private ComparableExpressionBase<?> getField(String field, QProfile q) {
		ProfilesCriteria pc = ProfilesCriteria.fromKey(field);
		if (Objects.isNull(pc)) {
			return q.profileId;
		}
		switch (pc) {
		case CODE_KEY:
			return q.profileId;
		case NAME_KEY:
			return q.name;
		case STATE_KEY:
			return q.state;
		default:
			return q.profileId;
		}
	}

	private BooleanExpression getPredicate(QProfile q, String criteria, String value) {
		ProfilesCriteria pc = ProfilesCriteria.fromKey(criteria);
		if (Objects.isNull(pc)) {
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
		switch (pc) {
		case CODE_KEY:
			return q.profileId.eq(ObjectUtilsVP.getNumber(value, Long.class));
		case NAME_KEY:
			return q.name.contains(value);
		case STATE_KEY:
			State state = State.fromValue(value);
			if (Objects.isNull(state)) {
				throw new DataTypeCriteriaException(NAME_RESOURCE, criteria, Arrays.toString(State.values()));
			}
			return q.state.eq(String.valueOf(state.ordinal()));
		default:
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
	}

	@Override
	public void updateProfile(Long id, Profile profile) {
		Profile entity = profileDAO.findById(id)
				.orElseThrow(() -> new NotFoundException(NAME_RESOURCE, CODE_KEY, id.toString()));
		checkProfile(profile, false);
		try {
			BeanUtils.copyProperties(profile, entity);
		} catch (BeansException e) {
			throw new ServerProcessException("No es posible realizar la copia de datos", e);
		}
		profileDAO.save(entity);
	}

	@Override
	public List<Authorization> getProfileAuthorizations(Long id) {
		return authorizationDAO.findByProfile(id);
	}

	@Override
	public void grantResources(Long profile, List<Long> resources) {
		resources.forEach(r -> {
			log.info("Assign resoruce {} to profile {}", r, profile);
			Authorization authorization = new Authorization();
			authorization.setAuthorizationId(String.format("%02X%016X", profile, r.longValue()));
			authorization.setProfile(profile);
			authorization.setResource(r);
			authorizationDAO.save(authorization);
			log.info("Authorization generated {}", authorization.getAuthorizationId());
		});
	}

	@Override
	public void revokeResource(Long profile, Long resource) {
		QAuthorization qAuthorization = QAuthorization.authorization;
		Predicate predicate = qAuthorization.profile.eq(profile).and(qAuthorization.resource.eq(resource));
		Iterator<Authorization> results = authorizationDAO.findAll(predicate).iterator();
		while (results.hasNext()) {
			authorizationDAO.delete(results.next());
		}
	}

	private void checkProfile(Profile profile, boolean checkId) {
		if (checkId && profileDAO.existsById(profile.getProfileId())) {
			throw new ConflictException(NAME_RESOURCE, CODE_KEY, String.valueOf(profile.getProfileId()));
		}
		if (profileDAO.existsByName(profile.getName())) {
			throw new ConflictException(NAME_RESOURCE, NAME_KEY, profile.getName());
		}
	}

}
