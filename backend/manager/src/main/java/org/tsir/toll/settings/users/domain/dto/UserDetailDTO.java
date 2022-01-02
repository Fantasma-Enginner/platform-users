package org.tsir.toll.settings.users.domain.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Esquema que contiene todo el detalle asociado a un usuario.
 */
@Schema(description = "Esquema que contiene todo el detalle asociado a un usuario. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-25T17:52:03.254Z[GMT]")
@JacksonXmlRootElement(localName = "UserDetailDTO")
@XmlRootElement(name = "UserDetailDTO")
@XmlAccessorType(XmlAccessType.FIELD)

public class UserDetailDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("user")
	@JacksonXmlProperty(localName = "user")
	private UserDTO user = null;

	@JsonProperty("contact")
	@JacksonXmlProperty(localName = "contact")
	private ContactDTO contact = null;

	@JsonProperty("enrollment")
	@JacksonXmlProperty(localName = "enrollment")
	private EnrollmentDTO enrollment = null;

	@JsonProperty("authentication")
	@JacksonXmlProperty(localName = "authentication")
	private AuthenticationDTO authentication = null;

	public UserDetailDTO user(UserDTO user) {
		this.user = user;
		return this;
	}

	/**
	 * Get user
	 * 
	 * @return user
	 **/
	@Schema(description = "")

	@Valid
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public UserDetailDTO contact(ContactDTO contact) {
		this.contact = contact;
		return this;
	}

	/**
	 * Get contact
	 * 
	 * @return contact
	 **/
	@Schema(description = "")

	@Valid
	public ContactDTO getContact() {
		return contact;
	}

	public void setContact(ContactDTO contact) {
		this.contact = contact;
	}

	public UserDetailDTO enrollment(EnrollmentDTO enrollment) {
		this.enrollment = enrollment;
		return this;
	}

	/**
	 * Get enrollment
	 * 
	 * @return enrollment
	 **/
	@Schema(description = "")

	@Valid
	public EnrollmentDTO getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(EnrollmentDTO enrollment) {
		this.enrollment = enrollment;
	}

	public UserDetailDTO authentication(AuthenticationDTO authentication) {
		this.authentication = authentication;
		return this;
	}

	/**
	 * Get authentication
	 * 
	 * @return authentication
	 **/
	@Schema(description = "")

	@Valid
	public AuthenticationDTO getAuthentication() {
		return authentication;
	}

	public void setAuthentication(AuthenticationDTO authentication) {
		this.authentication = authentication;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserDetailDTO userDetailDTO = (UserDetailDTO) o;
		return Objects.equals(this.user, userDetailDTO.user) && Objects.equals(this.contact, userDetailDTO.contact)
				&& Objects.equals(this.enrollment, userDetailDTO.enrollment)
				&& Objects.equals(this.authentication, userDetailDTO.authentication);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, contact, enrollment, authentication);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserDetailDTO {\n");

		sb.append("    user: ").append(toIndentedString(user)).append("\n");
		sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
		sb.append("    enrollment: ").append(toIndentedString(enrollment)).append("\n");
		sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
