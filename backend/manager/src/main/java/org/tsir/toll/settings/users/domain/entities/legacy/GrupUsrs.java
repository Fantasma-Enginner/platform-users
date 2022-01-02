package org.tsir.toll.settings.users.domain.entities.legacy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GRUP_USRS")
public class GrupUsrs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3777910748783508021L;

	@Id
	@Column(name = "ID_GRUP_USRS")
	private long idGrupUsrs;

	@Column(name = "CODIGO_GRUP_USRS")
	private Long codigoGrupUsrs;

	@Column(name = "NOMBRE_GRUP_USRS")
	private String nombreGrupUsrs;

	@Column(name = "ESTADO_GRUP_USRS")
	private String estadoGrupUsrs;

	public long getIdGrupUsrs() {
		return this.idGrupUsrs;
	}

	public void setIdGrupUsrs(long idGrupUsrs) {
		this.idGrupUsrs = idGrupUsrs;
	}

	public Long getCodigoGrupUsrs() {
		return this.codigoGrupUsrs;
	}

	public void setCodigoGrupUsrs(Long codigoGrupUsrs) {
		this.codigoGrupUsrs = codigoGrupUsrs;
	}

	public String getNombreGrupUsrs() {
		return this.nombreGrupUsrs;
	}

	public void setNombreGrupUsrs(String nombreGrupUsrs) {
		this.nombreGrupUsrs = nombreGrupUsrs;
	}

	public String getEstadoGrupUsrs() {
		return this.estadoGrupUsrs;
	}

	public void setEstadoGrupUsrs(String estadoGrupUsrs) {
		this.estadoGrupUsrs = estadoGrupUsrs;
	}
}