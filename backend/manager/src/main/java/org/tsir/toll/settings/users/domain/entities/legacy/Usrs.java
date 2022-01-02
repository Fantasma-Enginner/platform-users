package org.tsir.toll.settings.users.domain.entities.legacy;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Usrs implements Serializable {
	@Id
	@Column(name = "ID_USRS")
	private long idUsrs;

	@Column(name = "CODIGO_USRS")
	private Long codigoUsrs;

	@Column(name = "NOMBRE_USRS")
	private String nombreUsrs;

	@Column(name = "APELLIDO_USRS")
	private String apellidoUsrs;

	@Column(name = "ESTADO_USRS")
	private String estadoUsrs;

	@Column(name = "CLAVE_USRS")
	private String claveUsrs;

	@Column(name = "PWD_FECHA")
	private Timestamp pwdFecha;

	@Column(name = "ID_ESTA")
	private Long idEsta;

	@ManyToOne
	@JoinColumn(name = "ID_FOTO")
	private Foto foto;

	@ManyToOne
	@JoinColumn(name = "ID_GRUP_USRS")
	private GrupUsrs grupUsrs;

	@ManyToOne
	@JoinColumn(name = "ID_HUEL")
	private Huel huel;

	@Column(name = "ID_TISC")
	private String idTisc;

	private static final long serialVersionUID = 1L;

	public Usrs() {
		super();
	}

	public Usrs(long idUsrs) {
		super();
		this.idUsrs = idUsrs;
	}

	public long getIdUsrs() {
		return this.idUsrs;
	}

	public void setIdUsrs(long idUsrs) {
		this.idUsrs = idUsrs;
	}

	public Long getCodigoUsrs() {
		return this.codigoUsrs;
	}

	public void setCodigoUsrs(Long codigoUsrs) {
		this.codigoUsrs = codigoUsrs;
	}

	public String getNombreUsrs() {
		return this.nombreUsrs;
	}

	public void setNombreUsrs(String nombreUsrs) {
		this.nombreUsrs = nombreUsrs;
	}

	public String getApellidoUsrs() {
		return this.apellidoUsrs;
	}

	public void setApellidoUsrs(String apellidoUsrs) {
		this.apellidoUsrs = apellidoUsrs;
	}

	public String getEstadoUsrs() {
		return this.estadoUsrs;
	}

	public void setEstadoUsrs(String estadoUsrs) {
		this.estadoUsrs = estadoUsrs;
	}

	public String getClaveUsrs() {
		return this.claveUsrs;
	}

	public void setClaveUsrs(String claveUsrs) {
		this.claveUsrs = claveUsrs;
	}

	public Timestamp getPwdFecha() {
		return this.pwdFecha;
	}

	public void setPwdFecha(Timestamp pwdFecha) {
		this.pwdFecha = pwdFecha;
	}

	public Long getIdEsta() {
		return this.idEsta;
	}

	public void setIdEsta(Long idEsta) {
		this.idEsta = idEsta;
	}

	public Foto getFoto() {
		return this.foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public GrupUsrs getGrupUsrs() {
		return this.grupUsrs;
	}

	public void setGrupUsrs(GrupUsrs grupUsrs) {
		this.grupUsrs = grupUsrs;
	}

	public Huel getHuel() {
		return this.huel;
	}

	public void setHuel(Huel huel) {
		this.huel = huel;
	}

	public String getIdTisc() {
		return this.idTisc;
	}

	public void setIdTisc(String idTisc) {
		this.idTisc = idTisc;
	}

}
