package org.tsir.toll.settings.users.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CONTACTS")
public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3318575245989442114L;

	@Id
	@Column(name="CONTACT_ID")
	private long contactId;
	
	@OneToOne()
	@JoinColumn(name = "USER_ID")
	private User user;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "PHONE_NUMBER")
	private Long phone;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "EMERGENCY_NAME")
	private String emergencyName;

	@Column(name = "EMERGENCY_PHONE")
	private Long emergencyPhone;

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmergencyName() {
		return emergencyName;
	}

	public void setEmergencyName(String emergencyName) {
		this.emergencyName = emergencyName;
	}

	public Long getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(Long emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}
	
}
