package org.tsir.toll.settings.users.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "AUTHORIZATIONS")
public class Authorization implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8840667979088856425L;

	@Id
	@Column(name = "AUTHORIZATION_ID")
	private String authorizationId;

	@Column(name = "RESOURCE_ID")
	private Long resource;

	@Column(name = "PROFILE_ID")
	private Long profile;

	@ManyToOne
	@JoinColumn(name = "AUDIENCE_ID")
	private Audience audience;

	public String getAuthorizationId() {
		return authorizationId;
	}

	public void setAuthorizationId(String authorizationId) {
		this.authorizationId = authorizationId;
	}

	public Long getResource() {
		return resource;
	}

	public void setResource(Long resource) {
		this.resource = resource;
	}

	public Long getProfile() {
		return profile;
	}

	public void setProfile(Long profile) {
		this.profile = profile;
	}

	public Audience getAudience() {
		return audience;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}

}
