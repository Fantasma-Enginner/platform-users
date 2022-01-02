package org.tsir.toll.settings.users.application.register;

import static org.tsir.common.modules.ResourceConstants.SETTINGS_DOMAIN;
import static org.tsir.common.modules.ResourceConstants.PROFILES_MODULE;
import static org.tsir.common.modules.ResourceConstants.SEPARATOR;

import org.tsir.common.modules.Operation;
import org.tsir.common.modules.Registrable;

public final class ProfilesModule implements Registrable {

	public static final ProfilesModule INSTANCE = new ProfilesModule();

	private ProfilesModule() {
	}

	/*
	 * PROFILES MODULE DEFINITIONS
	 */
	private static final String FIND_PROFILES_CODE = "1";
	public static final String FIND_PROFILES_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + PROFILES_MODULE + SEPARATOR
			+ FIND_PROFILES_CODE;
	public static final Operation FIND_PROFILES_RESOURCE = Operation.getFromAuthority("Consultar",
			FIND_PROFILES_AUTHORITY);

	private static final String REGISTER_PROFILE_CODE = "2";
	public static final String REGISTER_PROFILE_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + PROFILES_MODULE + SEPARATOR
			+ REGISTER_PROFILE_CODE;
	public static final Operation REGISTER_PROFILE_RESOURCE = Operation.getFromAuthority("Crear",
			REGISTER_PROFILE_AUTHORITY);

	private static final String GET_PROFILE_CODE = "4";
	public static final String GET_PROFILE_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + PROFILES_MODULE + SEPARATOR
			+ GET_PROFILE_CODE;
	public static final Operation GET_PROFILE_RESOURCE = Operation.getFromAuthority("Detalle", GET_PROFILE_AUTHORITY);

	private static final String UPDATE_PROFILE_CODE = "8";
	public static final String UPDATE_PROFILE_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + PROFILES_MODULE + SEPARATOR
			+ UPDATE_PROFILE_CODE;
	public static final Operation UPDATE_PROFILE_RESOURCE = Operation.getFromAuthority("Modificar",
			UPDATE_PROFILE_AUTHORITY);

	private static final String GET_AUTHORIZATIONS_CODE = "10";
	public static final String GET_AUTHORIZATIONS_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + PROFILES_MODULE
			+ SEPARATOR + GET_AUTHORIZATIONS_CODE;
	public static final Operation GET_AUTHORIZATIONS_RESOURCE = Operation.getFromAuthority("Consultar permisos",
			GET_AUTHORIZATIONS_AUTHORITY);

	private static final String GRANT_CODE = "20";
	public static final String GRANT_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + PROFILES_MODULE + SEPARATOR
			+ GRANT_CODE;
	public static final Operation GRANT_RESOURCE = Operation.getFromAuthority("Asignar permiso", GRANT_AUTHORITY);

	private static final String REVOKE_CODE = "40";
	public static final String REVOKE_AUTHORITY = SETTINGS_DOMAIN + SEPARATOR + PROFILES_MODULE + SEPARATOR
			+ REVOKE_CODE;
	public static final Operation REVOKE_RESOURCE = Operation.getFromAuthority("Revocar permiso", REVOKE_AUTHORITY);

}
