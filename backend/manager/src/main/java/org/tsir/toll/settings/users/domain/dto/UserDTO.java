package org.tsir.toll.settings.users.domain.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.validation.annotation.Validated;
import org.tsir.toll.settings.users.domain.values.State;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Esquema de información básica de usuario
 */
@Schema(description = "Esquema de información básica de usuario")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-25T17:52:03.254Z[GMT]")
@JacksonXmlRootElement(localName = "UserDTO")
@XmlRootElement(name = "UserDTO")
@XmlAccessorType(XmlAccessType.FIELD)

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("identifier")
	@JacksonXmlProperty(localName = "identifier")
	private Long identifier = null;

	@JsonProperty("code")
	@JacksonXmlProperty(localName = "code")
	private Long code = null;

	@JsonProperty("firstName")
	@JacksonXmlProperty(localName = "firstName")
	private String firstName = null;

	@JsonProperty("lastName")
	@JacksonXmlProperty(localName = "lastName")
	private String lastName = null;

	@JsonProperty("nickname")
	@JacksonXmlProperty(localName = "nickname")
	private String nickname = null;

	@JsonProperty("state")
	@JacksonXmlProperty(localName = "state")
	private State state = null;

	@JsonProperty("photo")
	@JacksonXmlProperty(localName = "photo")
	private byte[] photo = null;

	public UserDTO identifier(Long id) {
		this.identifier = id;
		return this;
	}

	/**
	 * Get id
	 * 
	 * @return id
	 **/
	@Schema(description = "")

	public Long getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Long id) {
		this.identifier = id;
	}

	public UserDTO code(Long code) {
		this.code = code;
		return this;
	}

	/**
	 * Get code
	 * 
	 * @return code
	 **/
	@Schema(required = true, description = "")
	@NotNull

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public UserDTO firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * Nombres del usuario.
	 * 
	 * @return firstName
	 **/
	@Schema(description = "Nombres del usuario.")

	@Pattern(regexp = "^[A-Z]{3,}([ ]{1}[A-Z]+)?$")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserDTO lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * Apellidos del usuario.
	 * 
	 * @return lastName
	 **/
	@Schema(description = "Apellidos del usuario.")

	@Pattern(regexp = "^[A-Z]{3,}([ ]{1}[A-Z]+)?$")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserDTO nickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	/**
	 * Alias de identificación del usuario en la plataforma.
	 * 
	 * @return nickname
	 **/
	@Schema(description = "Alias de identificación del usuario en la plataforma. ")

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public UserDTO state(State state) {
		this.state = state;
		return this;
	}

	/**
	 * Get state
	 * 
	 * @return state
	 **/
	@Schema(description = "")

	@Valid
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public UserDTO photo(byte[] photo) {
		this.photo = photo;
		return this;
	}

	/**
	 * Imagen del usuario.
	 * 
	 * @return photo
	 **/
	@Schema(description = "Imagen del usuario.")

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserDTO userDTO = (UserDTO) o;
		return Objects.equals(this.identifier, userDTO.identifier) && Objects.equals(this.code, userDTO.code)
				&& Objects.equals(this.firstName, userDTO.firstName) && Objects.equals(this.lastName, userDTO.lastName)
				&& Objects.equals(this.nickname, userDTO.nickname) && Objects.equals(this.state, userDTO.state)
				&& Objects.equals(this.photo, userDTO.photo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier, code, firstName, lastName, nickname, state, photo);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserDTO {\n");

		sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
		sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
		sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
		sb.append("    state: ").append(toIndentedString(state)).append("\n");
		sb.append("    photo: ").append(toIndentedString(photo)).append("\n");
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
