package org.tsir.toll.settings.users.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Esquema que permite relacionar los datos de la diferentes opciones de
 * autenticación del usuario soportados por el sistema.
 */
@Schema(description = "Esquema que permite relacionar los datos de la diferentes opciones de autenticación del usuario soportados por el sistema. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-25T17:52:03.254Z[GMT]")
@JacksonXmlRootElement(localName = "AuthenticationDTO")
@XmlRootElement(name = "AuthenticationDTO")
@XmlAccessorType(XmlAccessType.FIELD)

public class AuthenticationDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("user")
	@JacksonXmlProperty(localName = "user")
	private Long user = null;

	@JsonProperty("password")
	@JacksonXmlProperty(localName = "password")
	private String password = null;

	@JsonProperty("datePassword")
	@JacksonXmlProperty(localName = "datePassword")
	private LocalDateTime datePassword = null;

	@JsonProperty("cardId")
	@JacksonXmlProperty(localName = "cardId")
	private String cardId = null;

	@JsonProperty("fingerprint")
	@JacksonXmlProperty(localName = "fingerprint")
	private byte[] fingerprint = null;

	public AuthenticationDTO user(Long user) {
		this.user = user;
		return this;
	}

	/**
	 * Get user
	 * 
	 * @return user
	 **/
	@Schema(description = "")

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public AuthenticationDTO password(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Clave de autenticación del usuario en el sistema.
	 * 
	 * @return password
	 **/
	@Schema(description = "Clave de autenticación del usuario en el sistema.")

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AuthenticationDTO datePassword(LocalDateTime datePassword) {
		this.datePassword = datePassword;
		return this;
	}

	/**
	 * Fecha de la ultima actualizacion de la clave del usuario en el sistema.
	 * 
	 * @return datePassword
	 **/
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Fecha de la ultima actualizacion de la clave del usuario en el sistema.")

	@Valid
	public LocalDateTime getDatePassword() {
		return datePassword;
	}

	public void setDatePassword(LocalDateTime datePassword) {
		this.datePassword = datePassword;
	}

	public AuthenticationDTO cardId(String cardId) {
		this.cardId = cardId;
		return this;
	}

	/**
	 * Identificador de la tarjeta de ingreso personal asignada al usuario.
	 * 
	 * @return cardId
	 **/
	@Schema(example = "A1B23F7B", description = "Identificador de la tarjeta de ingreso personal asignada al usuario.")

	@Pattern(regexp = "^[0-9A-F]{12}$")
	@Size(max = 12)
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public AuthenticationDTO fingerprint(byte[] fingerprint) {
		this.fingerprint = fingerprint;
		return this;
	}

	/**
	 * Plantilla de huella digital registrada.
	 * 
	 * @return fingerprint
	 **/
	@Schema(description = "Plantilla de huella digital registrada. ")

	public byte[] getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(byte[] fingerprint) {
		this.fingerprint = fingerprint;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AuthenticationDTO authenticationDTO = (AuthenticationDTO) o;
		return Objects.equals(this.user, authenticationDTO.user)
				&& Objects.equals(this.password, authenticationDTO.password)
				&& Objects.equals(this.datePassword, authenticationDTO.datePassword)
				&& Objects.equals(this.cardId, authenticationDTO.cardId)
				&& Objects.equals(this.fingerprint, authenticationDTO.fingerprint);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, password, datePassword, cardId, fingerprint);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AuthenticationDTO {\n");

		sb.append("    user: ").append(toIndentedString(user)).append("\n");
		sb.append("    password: ").append(toIndentedString(password)).append("\n");
		sb.append("    datePassword: ").append(toIndentedString(datePassword)).append("\n");
		sb.append("    cardId: ").append(toIndentedString(cardId)).append("\n");
		sb.append("    fingerprint: ").append(toIndentedString(fingerprint)).append("\n");
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
