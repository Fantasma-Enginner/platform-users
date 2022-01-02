package org.tsir.toll.settings.users.domain.entities.legacy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "FOTO")
public class Foto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8332958840679636036L;

	@Id
	@Column(name = "ID_FOTO")
	private long idFoto;

	@Lob
	@Type(type = "org.hibernate.type.BinaryType")
	@Column(name = "FOTO")
	private byte[] content;

	public long getIdFoto() {
		return this.idFoto;
	}

	public void setIdFoto(long idFoto) {
		this.idFoto = idFoto;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] foto) {
		this.content = foto;
	}

}
