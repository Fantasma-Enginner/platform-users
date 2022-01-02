package org.tsir.toll.settings.users.domain.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4823009571536781162L;

	@Id
	@Column(name = "USER_ID")
	private long userId;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "STATE")
	private Integer state;

	@ManyToOne
	@JoinColumn(name = "PROFILE_ID")
	private Profile profile;

	@Column(name = "TOLL_ID")
	private Integer tollId;

	@OneToOne(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Contact contact;

	@OneToOne(mappedBy = "user", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Credential credentials;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profileId) {
		this.profile = profileId;
	}

	public Integer getTollId() {
		return tollId;
	}

	public void setTollId(Integer tollId) {
		this.tollId = tollId;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Credential getCredentials() {
		return credentials;
	}

	public void setCredentials(Credential credentials) {
		this.credentials = credentials;
	}

}
