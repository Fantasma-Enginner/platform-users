package org.tsir.toll.settings.users.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.tsir.toll.settings.users.domain.entities.legacy.Usrs;

public interface UsrsRepository extends JpaRepository<Usrs, Long>, QuerydslPredicateExecutor<Usrs> {

	Optional<Usrs> findByCodigoUsrs(Long code);

}
