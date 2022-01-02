package org.tsir.toll.settings.users.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.tsir.toll.settings.users.domain.entities.Authorization;

public interface AuthorizationRepository
		extends JpaRepository<Authorization, String>, QuerydslPredicateExecutor<Authorization> {

	List<Authorization> findByProfile(Long idProfile);

}
