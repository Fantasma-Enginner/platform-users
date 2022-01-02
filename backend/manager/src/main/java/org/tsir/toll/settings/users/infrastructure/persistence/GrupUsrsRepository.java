package org.tsir.toll.settings.users.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.tsir.toll.settings.users.domain.entities.legacy.GrupUsrs;

public interface GrupUsrsRepository extends JpaRepository<GrupUsrs, Long>, QuerydslPredicateExecutor<GrupUsrs> {
	
	boolean existsByNombreGrupUsrs(String name);
	
	boolean existsByCodigoGrupUsrs(Long code);

}
