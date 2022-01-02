package org.tsir.toll.settings.users.domain.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CREDENTIALS")
public class Credential implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5835169419421794470L;

	@Id
	@Column(name = "CREDENTIAL_ID")
	private long credentialId;

	@OneToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "PSSW_DATE")
	private Timestamp psswDate;

//	@Lob
	@Column(name = "TEMPLATE")
	private byte[] template;

	@Column(name = "HAND")
	private Integer hand;

	@Column(name = "FINGER")
	private Integer finger;

	@Column(name = "TISC_ID")
	private String tiscId;

	public long getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(long credentialId) {
		this.credentialId = credentialId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getPsswDate() {
		return psswDate;
	}

	public void setPsswDate(Timestamp psswDate) {
		this.psswDate = psswDate;
	}

	public byte[] getTemplate() {
		return template;
	}

	public void setTemplate(byte[] template) {
		this.template = template;
	}

	public Integer getHand() {
		return hand;
	}

	public void setHand(Integer hand) {
		this.hand = hand;
	}

	public Integer getFinger() {
		return finger;
	}

	public void setFinger(Integer finger) {
		this.finger = finger;
	}

	public String getTiscId() {
		return tiscId;
	}

	public void setTiscId(String tiscId) {
		this.tiscId = tiscId;
	}

}
