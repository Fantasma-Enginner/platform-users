package org.tsir.toll.settings.users.domain.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Esquema que permite relacionar los datos de enrolamiento de un usuario en el
 * sistema.
 */
@Schema(description = "Esquema que permite relacionar los datos de enrolamiento de un usuario en el sistema. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-25T17:52:03.254Z[GMT]")
@JacksonXmlRootElement(localName = "EnrollmentDTO")
@XmlRootElement(name = "EnrollmentDTO")
@XmlAccessorType(XmlAccessType.FIELD)

public class EnrollmentDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("profile")
	@JacksonXmlProperty(localName = "profile")
	private Long profile = null;

	@JsonProperty("toll")
	@JacksonXmlProperty(localName = "toll")
	private Integer toll = null;

	public EnrollmentDTO profile(Long profile) {
		this.profile = profile;
		return this;
	}

	/**
	 * Codigo del perfil asignado al usuario.
	 * 
	 * @return profile
	 **/
	@Schema(description = "Codigo del perfil asignado al usuario.")

	public Long getProfile() {
		return profile;
	}

	public void setProfile(Long profile) {
		this.profile = profile;
	}

	public EnrollmentDTO toll(Integer toll) {
		this.toll = toll;
		return this;
	}

	/**
	 * Codigos de las estacion asignada al usuario. El valor 0 establece la
	 * asignación del usuario a todas las estaciones del sistema. El valor 'null'
	 * establece que el usuario no tiene estación asignada.
	 * 
	 * @return toll
	 **/
	@Schema(description = "Codigos de las estacion asignada al usuario. El valor 0 establece la asignación del usuario a todas las estaciones del sistema. El valor 'null' establece que el usuario no tiene estación asignada.")

	public Integer getToll() {
		return toll;
	}

	public void setToll(Integer toll) {
		this.toll = toll;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EnrollmentDTO enrollmentDTO = (EnrollmentDTO) o;
		return Objects.equals(this.profile, enrollmentDTO.profile) && Objects.equals(this.toll, enrollmentDTO.toll);
	}

	@Override
	public int hashCode() {
		return Objects.hash(profile, toll);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EnrollmentDTO {\n");

		sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
		sb.append("    toll: ").append(toIndentedString(toll)).append("\n");
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
