package org.tsir.toll.settings.users.domain.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
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
 * Información de los grupos de usuarios.
 */
@Schema(description = "Información de los grupos de usuarios.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-25T17:52:03.254Z[GMT]")
@JacksonXmlRootElement(localName = "ProfileDTO")
@XmlRootElement(name = "ProfileDTO")
@XmlAccessorType(XmlAccessType.FIELD)

public class ProfileDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("identifier")
	@JacksonXmlProperty(localName = "identifier")
	private Long identifier = null;

	@JsonProperty("code")
	@JacksonXmlProperty(localName = "code")
	private Long code = null;

	@JsonProperty("name")
	@JacksonXmlProperty(localName = "name")
	private String name = null;

	@JsonProperty("state")
	@JacksonXmlProperty(localName = "state")
	private State state = null;

	public ProfileDTO id(Long id) {
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

	public ProfileDTO code(Long code) {
		this.code = code;
		return this;
	}

	/**
	 * Codigo numerico identificador del perfil.
	 * 
	 * @return code
	 **/
	@Schema(description = "Codigo numerico identificador del perfil.")

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public ProfileDTO name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Nombre del perfil.
	 * 
	 * @return name
	 **/
	@Schema(description = "Nombre del perfil.")

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProfileDTO state(State state) {
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

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ProfileDTO profileDTO = (ProfileDTO) o;
		return Objects.equals(this.identifier, profileDTO.identifier) && Objects.equals(this.code, profileDTO.code)
				&& Objects.equals(this.name, profileDTO.name) && Objects.equals(this.state, profileDTO.state);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier, code, name, state);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ProfileDTO {\n");

		sb.append("    id: ").append(toIndentedString(identifier)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    state: ").append(toIndentedString(state)).append("\n");
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
