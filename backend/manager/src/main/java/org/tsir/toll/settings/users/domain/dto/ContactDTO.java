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
 * Datos de contacto de un usuario.
 */
@Schema(description = "Datos de contacto de un usuario. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-11-25T17:52:03.254Z[GMT]")
@JacksonXmlRootElement(localName = "ContactDTO")
@XmlRootElement(name = "ContactDTO")
@XmlAccessorType(XmlAccessType.FIELD)

public class ContactDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("address")
	@JacksonXmlProperty(localName = "address")
	private String address = null;

	@JsonProperty("email")
	@JacksonXmlProperty(localName = "email")
	private String email = null;

	@JsonProperty("phone")
	@JacksonXmlProperty(localName = "phone")
	private Integer phone = null;

	@JsonProperty("emergencyName")
	@JacksonXmlProperty(localName = "emergencyName")
	private String emergencyName = null;

	@JsonProperty("emergencyPhone")
	@JacksonXmlProperty(localName = "emergencyPhone")
	private Integer emergencyPhone = null;

	public ContactDTO address(String address) {
		this.address = address;
		return this;
	}

	/**
	 * Dirección de residencia.
	 * 
	 * @return address
	 **/
	@Schema(description = "Dirección de residencia. ")

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ContactDTO email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Dirección de correo electrónico.
	 * 
	 * @return email
	 **/
	@Schema(description = "Dirección de correo electrónico. ")

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ContactDTO phone(Integer phone) {
		this.phone = phone;
		return this;
	}

	/**
	 * Numero de telefono o celular de contato del usuario.
	 * 
	 * @return phone
	 **/
	@Schema(description = "Numero de telefono o celular de contato del usuario. ")

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}

	public ContactDTO emergencyName(String emergencyName) {
		this.emergencyName = emergencyName;
		return this;
	}

	/**
	 * Nombre de la persona de contacto en caso de emergencia.
	 * 
	 * @return emergencyName
	 **/
	@Schema(description = "Nombre de la persona de contacto en caso de emergencia. ")

	public String getEmergencyName() {
		return emergencyName;
	}

	public void setEmergencyName(String emergencyName) {
		this.emergencyName = emergencyName;
	}

	public ContactDTO emergencyPhone(Integer emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
		return this;
	}

	/**
	 * Numero de telefono para contacto de la persona de contacto en caso de
	 * emergencia.
	 * 
	 * @return emergencyPhone
	 **/
	@Schema(description = "Numero de telefono para contacto de la persona de contacto en caso de emergencia. ")

	public Integer getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(Integer emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ContactDTO contactDTO = (ContactDTO) o;
		return Objects.equals(this.address, contactDTO.address) && Objects.equals(this.email, contactDTO.email)
				&& Objects.equals(this.phone, contactDTO.phone)
				&& Objects.equals(this.emergencyName, contactDTO.emergencyName)
				&& Objects.equals(this.emergencyPhone, contactDTO.emergencyPhone);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, email, phone, emergencyName, emergencyPhone);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ContactDTO {\n");

		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
		sb.append("    emergencyName: ").append(toIndentedString(emergencyName)).append("\n");
		sb.append("    emergencyPhone: ").append(toIndentedString(emergencyPhone)).append("\n");
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
