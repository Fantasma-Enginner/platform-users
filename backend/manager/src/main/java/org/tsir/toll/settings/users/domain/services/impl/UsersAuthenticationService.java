package org.tsir.toll.settings.users.domain.services.impl;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tsir.common.modules.Operation;
import org.tsir.toll.settings.users.domain.entities.Authorization;
import org.tsir.toll.settings.users.domain.entities.User;
import org.tsir.toll.settings.users.domain.entities.legacy.GrupUsrs;
import org.tsir.toll.settings.users.domain.entities.legacy.Usrs;
import org.tsir.toll.settings.users.domain.values.State;
import org.tsir.toll.settings.users.infrastructure.config.ApplicationConfig;
import org.tsir.toll.settings.users.infrastructure.persistence.AuthorizationRepository;
import org.tsir.toll.settings.users.infrastructure.persistence.UserRepository;
import org.tsir.toll.settings.users.infrastructure.persistence.UsrsRepository;

@Service
public class UsersAuthenticationService implements UserDetailsService {

	private Logger log = LoggerFactory.getLogger(UsersAuthenticationService.class);

	@Autowired
	private AuthorizationRepository authorzationsDAO;

	@Autowired
	private UserRepository userDAO;

	@Autowired
	private UsrsRepository usrsDAO;

	@Autowired
	private ApplicationConfig config;

	@Override
	public UserDetails loadUserByUsername(String username) {
		if (config.isActiveLegacy()) {
			return getUserLegacy(username);
		} else {
			return getUserMigrate(username);
		}
	}

	private UserDetails getUserLegacy(String username) {
		Usrs usrs = usrsDAO.findByCodigoUsrs(Long.valueOf(username))
				.orElseThrow(() -> new UsernameNotFoundException(username));
		String password = usrs.getClaveUsrs();
		LocalDateTime pswDate = usrs.getPwdFecha().toLocalDateTime();
		boolean userEnabled = isUserEnabled(usrs);
		boolean groupEnabled = isGroupEnabled(usrs.getGrupUsrs());
		boolean passwordNotExpired = password != null && pswDate != null
				&& LocalDateTime.now().isBefore(pswDate.plusDays(config.getPasswordValidityDays()));
		Set<GrantedAuthority> authorities = compressAuthorities(usrs.getGrupUsrs().getIdGrupUsrs());
		log.info("Users: userEnabled {}, groupEnabled {}, expired {}", userEnabled, groupEnabled, !passwordNotExpired);
		return new org.springframework.security.core.userdetails.User(String.valueOf(usrs.getIdUsrs()), password,
				userEnabled && groupEnabled, true, passwordNotExpired, true, authorities);
	}

	private boolean isUserEnabled(Usrs usrs) {
		return (Integer.valueOf(usrs.getEstadoUsrs()) == State.ACTIVO.ordinal());
	}

	private boolean isGroupEnabled(GrupUsrs group) {
		return group != null ? Integer.valueOf(group.getEstadoGrupUsrs()) == State.ACTIVO.ordinal() : Boolean.FALSE;
	}

	private UserDetails getUserMigrate(String username) {
		User user = userDAO.findById(Long.parseLong(username))
				.orElseThrow(() -> new UsernameNotFoundException(username));
		String password = null;
		LocalDateTime pswDate = null;
		if (user.getCredentials() != null) {
			password = user.getCredentials().getPassword();
			pswDate = user.getCredentials().getPsswDate().toLocalDateTime();
		}
		boolean enabled = (user.getState() == State.ACTIVO.ordinal());
		boolean passwordNotExpired = password != null && pswDate != null
				&& LocalDateTime.now().isBefore(pswDate.plusDays(config.getPasswordValidityDays()));
		Set<GrantedAuthority> authorities = compressAuthorities(user.getProfile().getProfileId());
		log.info("Users info. enabled {}. expired {}", enabled, !passwordNotExpired);
		return new org.springframework.security.core.userdetails.User(username, password, enabled, true,
				passwordNotExpired, true, authorities);

	}

	private Set<GrantedAuthority> compressAuthorities(Long profile) {
		List<Authorization> authorizations = authorzationsDAO.findByProfile(profile);
		List<Long[]> identifiers = authorizations.stream().map(a -> Operation.getIdentifiers(a.getResource()))
				.collect(Collectors.toList());
		Set<GrantedAuthority> authorities = identifiers.stream()
				.collect(groupingBy(a -> a[0], groupingBy(a -> a[1], summingLong(a -> a[2])))).entrySet().stream()
				.map(e -> getHex(e.getKey()) + ":" + printMap(e.getValue())).map(SimpleGrantedAuthority::new)
				.collect(toSet());
		authorities.add(new SimpleGrantedAuthority("ROLE_" + profile));
		return authorities;
	}

	private String printMap(Map<Long, Long> map) {
		return map.entrySet().stream().map(e -> getHex(e.getKey()) + "-" + Long.toHexString(e.getValue()))
				.collect(Collectors.joining(";"));
	}

	private String getHex(Long auth) {
		return Long.toHexString(auth).toUpperCase();
	}

}
