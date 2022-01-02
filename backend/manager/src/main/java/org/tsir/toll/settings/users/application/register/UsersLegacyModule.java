package org.tsir.toll.settings.users.application.register;

import static org.tsir.common.modules.ResourceConstants.SETTINGS_DOMAIN;
import static org.tsir.common.modules.ResourceConstants.SEPARATOR;
import static org.tsir.common.modules.ResourceConstants.USERS_MODULE;

import org.tsir.common.modules.Operation;
import org.tsir.common.modules.Registrable;

public final class UsersLegacyModule implements Registrable {

	public static final UsersLegacyModule INSTANCE = new UsersLegacyModule();
	
	private UsersLegacyModule() {
	}

	private static final String FIND_USERS_CODE = "1";
	public static final String FIND_USERS_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ FIND_USERS_CODE;
	public static final Operation FIND_USERS_RESOURCE = Operation.getFromAuthority("Consultar", FIND_USERS_AUTHORITY);

	private static final String GET_USER_CODE = "4";
	public static final String GET_USER_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ GET_USER_CODE;
	public static final Operation GET_USER_RESOURCE = Operation.getFromAuthority("Detalle", GET_USER_AUTHORITY);

}
