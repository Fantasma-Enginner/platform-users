package org.tsir.toll.settings.users.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.tsir.toll.settings.users.domain.entities.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>, QuerydslPredicateExecutor<Profile> {

	boolean existsByName(String name);

}
