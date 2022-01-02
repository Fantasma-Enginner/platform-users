package org.tsir.toll.settings.users.application.register;

import static org.tsir.common.modules.ResourceConstants.SETTINGS_DOMAIN;
import static org.tsir.common.modules.ResourceConstants.SEPARATOR;
import static org.tsir.common.modules.ResourceConstants.USERS_MODULE;
import static org.tsir.common.modules.ResourceConstants.ASSISTANCE_MODULE;

import org.tsir.common.modules.Operation;
import org.tsir.common.modules.Registrable;

public final class UsersModule implements Registrable {

	public static final UsersModule INSTANCE = new UsersModule();
	
	private UsersModule() {
	}

	private static final String FIND_USERS_CODE = "1";
	public static final String FIND_USERS_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ FIND_USERS_CODE;
	public static final Operation FIND_USERS_RESOURCE = Operation.getFromAuthority("Consultar", FIND_USERS_AUTHORITY);

	private static final String REGISTER_USER_CODE = "2";
	public static final String REGISTER_USER_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ REGISTER_USER_CODE;
	public static final Operation REGISTER_USER_RESOURCE = Operation.getFromAuthority("Registrar",
			REGISTER_USER_AUTHORITY);

	private static final String GET_USER_CODE = "4";
	public static final String GET_USER_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ GET_USER_CODE;
	public static final Operation GET_USER_RESOURCE = Operation.getFromAuthority("Detalle", GET_USER_AUTHORITY);

	private static final String UPDATE_USER_CODE = "8";
	public static final String UPDATE_USER_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ UPDATE_USER_CODE;
	public static final Operation UPDATE_USER_RESOURCE = Operation.getFromAuthority("Modificar", UPDATE_USER_AUTHORITY);

	private static final String INACTIVATE_USER_CODE = "10";
	public static final String INACTIVATE_USER_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ INACTIVATE_USER_CODE;
	public static final Operation INACTIVATE_USER_RESOURCE = Operation.getFromAuthority("Inactivar",
			INACTIVATE_USER_AUTHORITY);

	private static final String ENROLLMENT_CODE = "20";
	public static final String ENROLLMENT_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ ENROLLMENT_CODE;
	public static final Operation ENROLLMENT_RESOURCE = Operation.getFromAuthority("Enrolar", ENROLLMENT_AUTHORITY);

	private static final String AUTHENTICATION_CODE = "40";
	public static final String AUTHENTICATION_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + USERS_MODULE + SEPARATOR
			+ AUTHENTICATION_CODE;
	public static final Operation AUTHENTICATION_RESOURCE = Operation.getFromAuthority("Autenticación",
			AUTHENTICATION_AUTHORITY);

}
