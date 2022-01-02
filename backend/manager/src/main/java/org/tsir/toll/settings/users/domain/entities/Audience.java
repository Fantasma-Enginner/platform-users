package org.tsir.toll.settings.users.domain.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUDIENCES")
public class Audience implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -166787437594268846L;

	@Id
	@Column(name = "AUDIENCE_ID")
	private int audienceId;

	@Column(name = "LABEL")
	private String label;

	public int getAudienceId() {
		return audienceId;
	}

	public void setAudienceId(int audienceId) {
		this.audienceId = audienceId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
