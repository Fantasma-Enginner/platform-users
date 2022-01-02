package org.tsir.toll.settings.users.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROFILES")
public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1284628641755455852L;

	@Id
	@Column(name = "PROFILE_ID")
	private long profileId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "STATE")
	private String state;

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
