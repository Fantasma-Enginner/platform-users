package org.tsir.toll.settings.users.domain.entities.legacy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "HUEL")
public class Huel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4594833799250038782L;

	@Id
	@Column(name = "ID_HUEL")
	private long idHuel;

	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "HUELLA")
	private byte[] huella;

	@Column(name = "MANO")
	private Long mano;

	@Column(name = "DEDO")
	private Long dedo;

	public long getIdHuel() {
		return this.idHuel;
	}

	public void setIdHuel(long idHuel) {
		this.idHuel = idHuel;
	}

	public byte[] getHuella() {
		return this.huella;
	}

	public void setHuella(byte[] huella) {
		this.huella = huella;
	}

	public Long getMano() {
		return this.mano;
	}

	public void setMano(Long mano) {
		this.mano = mano;
	}

	public Long getDedo() {
		return this.dedo;
	}

	public void setDedo(Long dedo) {
		this.dedo = dedo;
	}

}
