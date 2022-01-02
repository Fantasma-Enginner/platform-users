package org.tsir.toll.settings.users.domain.values;

import java.util.Arrays;

import org.springframework.util.ObjectUtils;
import org.tsir.common.api.exceptions.DataTypeCriteriaException;
import org.tsir.common.api.exceptions.NotSupportCriteriaException;

public enum UsersCriteria {

	CODE_KEY("CODE"),

	FIRST_NAME_KEY("FIRSTNAME"),

	LAST_NAME_KEY("LASTNAME"),

	STATE_KEY("STATE"),

	PROFILE_KEY("PROFILE"),

	TOLL_KEY("TOLL");

	private UsersCriteria(String key) {
		this.key = key;
	}

	private String key;

	public String getKey() {
		return key;
	}

	public static UsersCriteria fromKey(String key) {
		if (ObjectUtils.isEmpty(key)) {
			throw new DataTypeCriteriaException("Usuario", key, Arrays.toString(UsersCriteria.values()));
		}
		for (UsersCriteria uc : UsersCriteria.values()) {
			if (uc.getKey().equals(key)) {
				return uc;
			}
		}
		throw new NotSupportCriteriaException("Usuario", key);
	}

}
