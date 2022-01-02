package org.tsir.toll.settings.users.domain.services.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
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
import org.tsir.common.services.CommunicationsService;
import org.tsir.common.utils.ObjectUtilsVP;
import org.tsir.common.utils.PagingUtils;
import org.tsir.common.utils.SortingKey;
import org.tsir.toll.settings.users.domain.entities.Authorization;
import org.tsir.toll.settings.users.domain.entities.QAuthorization;
import org.tsir.toll.settings.users.domain.entities.legacy.GrupUsrs;
import org.tsir.toll.settings.users.domain.entities.legacy.QGrupUsrs;
import org.tsir.toll.settings.users.domain.services.GroupsService;
import org.tsir.toll.settings.users.domain.values.ProfilesCriteria;
import org.tsir.toll.settings.users.domain.values.State;
import org.tsir.toll.settings.users.infrastructure.persistence.AuthorizationRepository;
import org.tsir.toll.settings.users.infrastructure.persistence.GrupUsrsRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

@Service
public class GroupsServiceImpl implements GroupsService {

	Logger log = LoggerFactory.getLogger(GroupsServiceImpl.class);

	private static final String NAME_RESOURCE = "Grupo de Usuario";
	private static final String CODE_KEY = "Código";
	private static final String NAME_KEY = "Nombre";

	@Autowired
	private GrupUsrsRepository groupsDAO;

	@Autowired
	private AuthorizationRepository authorizationDAO;

	@Autowired
	private CommunicationsService commService;

	@Override
	public GrupUsrs getProfileById(Long id) {
		Optional<GrupUsrs> opt = groupsDAO.findById(id);
		return opt.orElseThrow(() -> new NotFoundException(NAME_RESOURCE, CODE_KEY, id.toString()));
	}

	@Override
	public void createProfile(GrupUsrs profile) {
		if (profile.getIdGrupUsrs() == 0) {
			profile.setIdGrupUsrs(profile.getCodigoGrupUsrs());
		}
		checkProfile(profile, true);
		if (profile.getEstadoGrupUsrs() == null) {
			profile.setEstadoGrupUsrs(String.valueOf(State.NUEVO.ordinal()));
		}
		groupsDAO.save(profile);
		commService.createTask("GRUP_USRS", profile.getIdGrupUsrs());
	}

	@Override
	public List<GrupUsrs> findProfilesList(Map<String, String> mapFilter, Map<String, String> sortingProperties) {
		QGrupUsrs qGrupUsrs = QGrupUsrs.grupUsrs;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), ProfilesCriteria.CODE_KEY.getKey()),
				qGrupUsrs);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		if (!ObjectUtils.isEmpty(mapFilter)) {
			BooleanBuilder builder = new BooleanBuilder();
			mapFilter.forEach((k, v) -> builder.and(getPredicate(qGrupUsrs, k, v)));
			return IterableUtils.toList(groupsDAO.findAll(builder, sort));
		}
		return groupsDAO.findAll(sort);
	}

	@Override
	public Page<GrupUsrs> findProfilesPage(Map<String, String> mapFilter, Map<String, Integer> pagingProperties,
			Map<String, String> sortingProperties) {
		QGrupUsrs qGrupUsrs = QGrupUsrs.grupUsrs;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), ProfilesCriteria.CODE_KEY.getKey()),
				qGrupUsrs);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		Pageable pageable = PagingUtils.getPageable(pagingProperties, sort);
		if (!ObjectUtils.isEmpty(mapFilter)) {
			BooleanBuilder builder = new BooleanBuilder();
			mapFilter.forEach((k, v) -> builder.and(getPredicate(qGrupUsrs, k, v)));
			return groupsDAO.findAll(builder, pageable);
		}
		return groupsDAO.findAll(pageable);
	}

	private ComparableExpressionBase<?> getField(String field, QGrupUsrs q) {
		ProfilesCriteria pc = ProfilesCriteria.fromKey(field);
		if (Objects.isNull(pc)) {
			return q.codigoGrupUsrs;
		}
		switch (pc) {
		case CODE_KEY:
			return q.codigoGrupUsrs;
		case NAME_KEY:
			return q.nombreGrupUsrs;
		case STATE_KEY:
			return q.estadoGrupUsrs;
		default:
			return q.codigoGrupUsrs;
		}
	}

	private BooleanExpression getPredicate(QGrupUsrs q, String criteria, String value) {
		ProfilesCriteria pc = ProfilesCriteria.fromKey(criteria);
		if (Objects.isNull(pc)) {
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
		switch (pc) {
		case CODE_KEY:
			return q.codigoGrupUsrs.eq(ObjectUtilsVP.getNumber(value, Long.class));
		case NAME_KEY:
			return q.nombreGrupUsrs.contains(value);
		case STATE_KEY:
			State state = State.fromValue(value);
			if (Objects.isNull(state)) {
				throw new DataTypeCriteriaException(NAME_RESOURCE, criteria, Arrays.toString(State.values()));
			}
			return q.estadoGrupUsrs.eq(String.valueOf(state.ordinal()));
		default:
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
	}

	@Override
	public void updateProfile(Long id, GrupUsrs profile) {
		GrupUsrs entity = groupsDAO.findById(id)
				.orElseThrow(() -> new NotFoundException(NAME_RESOURCE, CODE_KEY, id.toString()));
		if (!profile.getNombreGrupUsrs().equalsIgnoreCase(entity.getNombreGrupUsrs()))
			checkProfile(profile, false);
		try {
			BeanUtils.copyProperties(profile, entity);
		} catch (BeansException e) {
			throw new ServerProcessException("No es posible realizar la copia de datos", e);
		}
		groupsDAO.save(entity);
	}

	@Override
	public List<Authorization> getProfileAuthorizations(Long id) {
		return authorizationDAO.findByProfile(id);
	}

	@Override
	public void grantResources(Long profileId, List<Long> resources) {
		resources.forEach(r -> {
			log.info("Assign resoruce {} to profile {}", r, profileId);
			Authorization authorization = new Authorization();
			authorization.setAuthorizationId(String.format("%02X%016X", profileId, r.longValue()));
			authorization.setProfile(profileId);
			authorization.setResource(r);
			authorizationDAO.save(authorization);
			log.info("Authorization generated {}", authorization.getAuthorizationId());
		});
	}

	public long hashCodeAuthorization(long profileId, long resourceId) {
		long hash = 1;
		hash = hash * 17 + profileId;
		hash = hash * 31 + resourceId;
		return hash;
	}

	@Override
	public void revokeResource(Long profileId, Long resource) {
		QAuthorization qAuthorization = QAuthorization.authorization;
		Predicate predicate = qAuthorization.profile.eq(profileId).and(qAuthorization.resource.eq(resource));
		Iterator<Authorization> results = authorizationDAO.findAll(predicate).iterator();
		while (results.hasNext()) {
			authorizationDAO.delete(results.next());
		}
	}

	private void checkProfile(GrupUsrs profile, boolean checkId) {
		if (checkId && groupsDAO.existsById(profile.getIdGrupUsrs())) {
			throw new ConflictException(NAME_RESOURCE, CODE_KEY, String.valueOf(profile.getIdGrupUsrs()));
		}
		if (checkId && groupsDAO.existsByCodigoGrupUsrs(profile.getCodigoGrupUsrs())) {
			throw new ConflictException(NAME_RESOURCE, CODE_KEY, String.valueOf(profile.getIdGrupUsrs()));
		}
		if (groupsDAO.existsByNombreGrupUsrs(profile.getNombreGrupUsrs())) {
			throw new ConflictException(NAME_RESOURCE, NAME_KEY, profile.getNombreGrupUsrs());
		}
	}

}
