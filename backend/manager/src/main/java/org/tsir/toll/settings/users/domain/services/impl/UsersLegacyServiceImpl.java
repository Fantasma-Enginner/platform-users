package org.tsir.toll.settings.users.domain.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.tsir.common.api.exceptions.ConflictException;
import org.tsir.common.api.exceptions.DataTypeCriteriaException;
import org.tsir.common.api.exceptions.NotFoundException;
import org.tsir.common.api.exceptions.NotSupportCriteriaException;
import org.tsir.common.api.exceptions.ServerProcessException;
import org.tsir.common.services.CommunicationsService;
import org.tsir.common.utils.ObjectUtilsVP;
import org.tsir.common.utils.PagingUtils;
import org.tsir.common.utils.SortingKey;
import org.tsir.toll.settings.users.domain.entities.legacy.QUsrs;
import org.tsir.toll.settings.users.domain.entities.legacy.Usrs;
import org.tsir.toll.settings.users.domain.services.UsersLegacyService;
import org.tsir.toll.settings.users.domain.values.State;
import org.tsir.toll.settings.users.domain.values.UsersCriteria;
import org.tsir.toll.settings.users.infrastructure.persistence.UsrsRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

@Service
public class UsersLegacyServiceImpl implements UsersLegacyService {

	private static final String NAME_RESOURCE = "Usuario";
	private static final String NAME_KEY = "Código";

	@Autowired
	private UsrsRepository usrsDAO;

	@Autowired
	private CommunicationsService commService;

	@Override
	public Usrs getUserByCode(Long code) {
		Optional<Usrs> result = usrsDAO.findById(code);
		return result.orElseThrow(() -> new NotFoundException(NAME_RESOURCE, NAME_KEY, code.toString()));
	}

	@Override
	public void registerUser(Usrs usrs) {
		if (usrsDAO.existsById(usrs.getIdUsrs())) {
			throw new ConflictException(NAME_RESOURCE, NAME_KEY, String.valueOf(usrs.getIdUsrs()));
		}
		if (usrs.getEstadoUsrs() == null) {
			usrs.setEstadoUsrs(String.valueOf(State.NUEVO.ordinal()));
		}
		usrsDAO.save(usrs);
		commService.createTask("USRS", usrs.getIdUsrs());
	}

	@Override
	public void updateUser(Long code, Usrs usrs) {
		Usrs local = getUserByCode(code);
		try {
			BeanUtils.copyProperties(usrs, local);
			usrsDAO.save(local);
			commService.createTask("USRS", usrs.getIdUsrs());
		} catch (BeansException e) {
			throw new ServerProcessException("No es posible realizar la copia de datos", e);
		}
	}

	@Override
	public boolean isUserRegistered(Long code) {
		return usrsDAO.existsById(code);
	}

	@Override
	public List<Usrs> findUsersList(Map<String, String> mapFilter, Map<String, String> sortingProperties) {
		QUsrs qUsrs = QUsrs.usrs;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), UsersCriteria.CODE_KEY.getKey()),
				qUsrs);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		if (!ObjectUtils.isEmpty(mapFilter)) {
			BooleanBuilder builder = new BooleanBuilder();
			mapFilter.forEach((k, v) -> builder.and(getPredicate(qUsrs, k, v)));
			return IterableUtils.toList(usrsDAO.findAll(builder, sort));
		}
		return usrsDAO.findAll(sort);
	}

	@Override
	public Page<Usrs> findUsersPage(Map<String, String> mapFilter,
			Map<String, Integer> pagingProperties, Map<String, String> sortingProperties) {
		QUsrs qUsrs = QUsrs.usrs;
		ComparableExpressionBase<?> expression = getField(
				sortingProperties.getOrDefault(SortingKey.FIELD.toString(), UsersCriteria.CODE_KEY.getKey()),
				qUsrs);
		Sort sort = PagingUtils.getSortEspecification(sortingProperties, expression);
		Pageable pageable = PagingUtils.getPageable(pagingProperties, sort);
		if (!ObjectUtils.isEmpty(mapFilter)) {
			BooleanBuilder builder = new BooleanBuilder();
			mapFilter.forEach((k, v) -> builder.and(getPredicate(qUsrs, k, v)));
			return usrsDAO.findAll(builder, pageable);
		} else {
			return usrsDAO.findAll(pageable);
		}
	}

	private ComparableExpressionBase<?> getField(String field, QUsrs q) {
		UsersCriteria userCriteria;
		try {
			userCriteria = UsersCriteria.fromKey(field);
		} catch (NotSupportCriteriaException e) {
			return q.idUsrs;
		}
		switch (userCriteria) {
		case CODE_KEY:
			return q.idUsrs;
		case FIRST_NAME_KEY:
			return q.nombreUsrs;
		case LAST_NAME_KEY:
			return q.apellidoUsrs;
		case STATE_KEY:
			return q.estadoUsrs;
		case PROFILE_KEY:
			return q.grupUsrs.idGrupUsrs;
		case TOLL_KEY:
			return q.idEsta;
		default:
			return q.idUsrs;
		}
	}

	private Predicate getPredicate(QUsrs q, String criteria, String value) {
		UsersCriteria userCriteria = UsersCriteria.fromKey(criteria);
		if (Objects.isNull(userCriteria)) {
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
		switch (userCriteria) {
		case CODE_KEY:
			return q.idUsrs.eq(ObjectUtilsVP.getNumberCriteria(NAME_RESOURCE, value, criteria, Long.class));
		case FIRST_NAME_KEY:
			return q.nombreUsrs.contains(value);
		case LAST_NAME_KEY:
			return q.apellidoUsrs.contains(value);
		case STATE_KEY:
			State state = State.fromValue(value);
			if (Objects.isNull(state)) {
				throw new DataTypeCriteriaException(NAME_RESOURCE, criteria, Arrays.toString(State.values()));
			}
			return q.estadoUsrs.eq(String.valueOf(state.ordinal()));
		case PROFILE_KEY:
			return q.grupUsrs.idGrupUsrs.eq(ObjectUtilsVP.getNumber(value, Long.class));
		case TOLL_KEY:
			return q.idEsta.eq(ObjectUtilsVP.getNumber(value, Long.class));
		default:
			throw new NotSupportCriteriaException(NAME_RESOURCE, criteria);
		}
	}

}
