package org.tsir.toll.settings.users.domain.values;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeración de estados generales de activación: * `NUEVO` - Objeto nuevo. *
 * `ACTIVO` - Objeto activo. * `INACTIVO` - Objeto inactivo. * `SUSPENDIDO` -
 * Objeto suspendido.
 */
public enum State {
	NUEVO("NUEVO"), ACTIVO("ACTIVO"), INACTIVO("INACTIVO"), SUSPENDIDO("SUSPENDIDO");

	private String value;

	State(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static State fromValue(String text) {
		for (State b : State.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
