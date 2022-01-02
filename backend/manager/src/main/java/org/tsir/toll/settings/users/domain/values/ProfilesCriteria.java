package org.tsir.toll.settings.users.domain.values;

import java.util.Arrays;

import org.springframework.util.ObjectUtils;
import org.tsir.common.api.exceptions.DataTypeCriteriaException;
import org.tsir.common.api.exceptions.NotSupportCriteriaException;

public enum ProfilesCriteria {

	CODE_KEY("CODE"),

	NAME_KEY("NAME"),

	STATE_KEY("STATE");

	private ProfilesCriteria(String key) {
		this.key = key;
	}

	private String key;

	public String getKey() {
		return key;
	}

	public static ProfilesCriteria fromKey(String key) {
		if (ObjectUtils.isEmpty(key)) {
			throw new DataTypeCriteriaException("Perfil/Grupo de Usuario", key,
					Arrays.toString(ProfilesCriteria.values()));
		}
		for (ProfilesCriteria pc : ProfilesCriteria.values()) {
			if (pc.getKey().equals(key)) {
				return pc;
			}
		}
		throw new NotSupportCriteriaException("Perfil/Grupo de Usuario", key);
	}
}
