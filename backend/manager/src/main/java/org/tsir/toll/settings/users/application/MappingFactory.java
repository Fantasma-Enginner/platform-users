package org.tsir.toll.settings.users.application;

import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tsir.toll.settings.users.domain.dto.AuthenticationDTO;
import org.tsir.toll.settings.users.domain.dto.ContactDTO;
import org.tsir.toll.settings.users.domain.dto.EnrollmentDTO;
import org.tsir.toll.settings.users.domain.dto.ProfileDTO;
import org.tsir.toll.settings.users.domain.dto.UserDTO;
import org.tsir.toll.settings.users.domain.dto.UserDetailDTO;
import org.tsir.toll.settings.users.domain.entities.Contact;
import org.tsir.toll.settings.users.domain.entities.Credential;
import org.tsir.toll.settings.users.domain.entities.Profile;
import org.tsir.toll.settings.users.domain.entities.User;
import org.tsir.toll.settings.users.domain.entities.legacy.Foto;
import org.tsir.toll.settings.users.domain.entities.legacy.GrupUsrs;
import org.tsir.toll.settings.users.domain.entities.legacy.Usrs;
import org.tsir.toll.settings.users.domain.values.State;
import org.tsir.toll.settings.users.infrastructure.config.ApplicationConfig;

@Service
public class MappingFactory {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ApplicationConfig config;

	public void loadUserMapping() {
		modelMapper.addConverter(this::getStateEnum, String.class, State.class);
		modelMapper.addConverter(this::getStateCode, State.class, String.class);
		if (config.isActiveLegacy()) {
			setMappingUserLegacy();
		} else {
			setMappingUserMigrate();
		}
	}

	private void setMappingUserLegacy() {
		setUsrsToUserDTO();

		setUsrsToUserDetailDTO();

		modelMapper.typeMap(Usrs.class, EnrollmentDTO.class).addMappings(mapper -> {
			mapper.map(this::getIdGrupUsrs, EnrollmentDTO::setProfile);
			mapper.map(Usrs::getIdEsta, EnrollmentDTO::setToll);
		});

	}

	private void setUsrsToUserDTO() {
		modelMapper.typeMap(Usrs.class, UserDTO.class).addMappings(mapper -> {
			mapper.map(Usrs::getIdUsrs, UserDTO::setIdentifier);
			mapper.map(Usrs::getCodigoUsrs, UserDTO::setCode);
			mapper.map(Usrs::getNombreUsrs, UserDTO::setFirstName);
			mapper.map(Usrs::getApellidoUsrs, UserDTO::setLastName);
			mapper.map(Usrs::getEstadoUsrs, UserDTO::setState);
			mapper.map(src -> src.getFoto().getContent(), UserDTO::setPhoto);
		});
		modelMapper.typeMap(UserDTO.class, Usrs.class).addMappings(mapper -> {
			mapper.map(UserDTO::getIdentifier, Usrs::setIdUsrs);
			mapper.map(UserDTO::getCode, Usrs::setCodigoUsrs);
			mapper.map(UserDTO::getFirstName, Usrs::setNombreUsrs);
			mapper.map(UserDTO::getLastName, Usrs::setApellidoUsrs);
			mapper.map(UserDTO::getState, Usrs::setEstadoUsrs);
			mapper.map(UserDTO::getPhoto, (dest, v) -> this.setUsrsPhoto(dest, (byte[]) v));
		});
	}

	private void setUsrsToUserDetailDTO() {
//		modelMapper.typeMap(Usrs.class, UserDetailDTO.class).addMappings(mapper -> {
//			mapper.map(Usrs::getCodigoUsrs, (dest, v) -> dest.getUser().setCode((Long) v));
//			mapper.map(Usrs::getNombreUsrs, (dest, v) -> dest.getUser().setFirstName(v.toString()));
//			mapper.map(Usrs::getApellidoUsrs, (dest, v) -> dest.getUser().setLastName(v.toString()));
//			mapper.map(Usrs::getEstadoUsrs,
//					(dest, v) -> dest.getUser().setState(State.values()[Integer.valueOf(v.toString())]));
//			mapper.map(src -> src.getFoto().getContent(), (dest, v) -> dest.getUser().setPhoto((byte[]) v));
//		});
		modelMapper.typeMap(UserDetailDTO.class, Usrs.class).addMappings(mapper -> {
			mapper.map(src -> src.getUser().getIdentifier(), Usrs::setIdUsrs);
			mapper.map(src -> src.getUser().getCode(), Usrs::setCodigoUsrs);
			mapper.map(src -> src.getUser().getFirstName(), Usrs::setNombreUsrs);
			mapper.map(src -> src.getUser().getLastName(), Usrs::setApellidoUsrs);
			mapper.map(src -> src.getUser().getState(), Usrs::setEstadoUsrs);
			mapper.map(src -> src.getUser().getPhoto(), (dest, v) -> this.setUsrsPhoto(dest, (byte[]) v));
		});
	}

	private Long getIdGrupUsrs(Usrs usrs) {
		return usrs.getGrupUsrs().getIdGrupUsrs();
	}

	private void setMappingUserMigrate() {
		modelMapper.typeMap(User.class, EnrollmentDTO.class).addMappings(mapper -> {
			mapper.map(User::getProfile, EnrollmentDTO::setProfile);
			mapper.map(User::getTollId, EnrollmentDTO::setToll);
		});
		
		modelMapper.typeMap(User.class, UserDTO.class).addMappings(mapper-> {
			mapper.map(User::getUserId, UserDTO::setIdentifier);
			mapper.map(User::getUserId, UserDTO::setCode);
		});
		
		modelMapper.typeMap(UserDTO.class, User.class).addMappings(mapper -> {
			mapper.map(UserDTO::getIdentifier, User::setUserId);
			mapper.map(UserDTO::getCode, User::setUserId);
		});
	}

	private void setUsrsPhoto(Usrs usrs, byte[] content) {
		Foto foto = usrs.getFoto();
		if (foto == null) {
			foto = new Foto();
			foto.setIdFoto(usrs.getIdUsrs());
		}
		foto.setContent(content);
	}

	private State getStateEnum(MappingContext<String, State> ctx) {
		return State.values()[Integer.valueOf(ctx.getSource())];
	}

	private String getStateCode(MappingContext<State, String> ctx) {
		return String.valueOf(ctx.getSource().ordinal());
	}

	public Usrs mapUsrs(UserDTO model) {
		return modelMapper.map(model, Usrs.class);
	}

	public UserDTO mapUserDTO(User domain) {
		return modelMapper.map(domain, UserDTO.class);
	}

	public UserDTO mapLegacyUserDTO(Usrs domain) {
		return modelMapper.map(domain, UserDTO.class);
	}

	public UserDetailDTO mapUserDetailDTO(User domain) {
		UserDetailDTO detail = new UserDetailDTO();
		detail.setUser(mapUserDTO(domain));
		detail.setEnrollment(mapEnrollmentDTO(domain));
		detail.setContact(mapContactDTO(domain.getContact()));
		detail.setAuthentication(mapAuthenticationDTO(domain.getCredentials()));
		return detail;
	}

	public UserDetailDTO mapUserDetailDTO(Usrs domain) {
		UserDetailDTO detail = new UserDetailDTO();
		detail.setUser(mapLegacyUserDTO(domain));
		detail.setEnrollment(mapEnrollmentDTO(domain));
		return detail;
	}

	public EnrollmentDTO mapEnrollmentDTO(User from) {
		return modelMapper.map(from, EnrollmentDTO.class);
	}

	public EnrollmentDTO mapEnrollmentDTO(Usrs from) {
		return modelMapper.map(from, EnrollmentDTO.class);
	}

	public User mapUser(UserDTO from) {
		return modelMapper.map(from, User.class);
	}

	public Contact mapContact(ContactDTO from) {
		return modelMapper.map(from, Contact.class);
	}

	public ContactDTO mapContactDTO(Contact from) {
		return modelMapper.map(from, ContactDTO.class);
	}

	public Credential mapCredential(AuthenticationDTO from) {
		return modelMapper.map(from, Credential.class);
	}

	public AuthenticationDTO mapAuthenticationDTO(Credential from) {
		return modelMapper.map(from, AuthenticationDTO.class);
	}

	private void loadProfileConverters() {
		modelMapper.addConverter(this::getStateEnum);
		modelMapper.addConverter(this::getStateCode);
	}

	public void loadProfileMapping() {
		loadProfileConverters();
		if (config.isActiveLegacy()) {
			setGrupUserMapping();
		}
	}


	public void setGrupUserMapping() {
		loadProfileConverters();
		modelMapper.typeMap(GrupUsrs.class, ProfileDTO.class).addMappings(mapper -> {
			mapper.map(GrupUsrs::getIdGrupUsrs, ProfileDTO::setIdentifier);
			mapper.map(GrupUsrs::getCodigoGrupUsrs, ProfileDTO::setCode);
			mapper.map(GrupUsrs::getNombreGrupUsrs, ProfileDTO::setName);
			mapper.map(GrupUsrs::getEstadoGrupUsrs, ProfileDTO::setState);
		});
		modelMapper.typeMap(ProfileDTO.class, GrupUsrs.class).addMappings(mapper -> {
			mapper.map(ProfileDTO::getIdentifier, GrupUsrs::setIdGrupUsrs);
			mapper.map(ProfileDTO::getCode, GrupUsrs::setCodigoGrupUsrs);
			mapper.map(ProfileDTO::getName, GrupUsrs::setNombreGrupUsrs);
			mapper.map(ProfileDTO::getState, GrupUsrs::setEstadoGrupUsrs);
		});
	}

	public ProfileDTO mapProfileDTO(Profile model) {
		return modelMapper.map(model, ProfileDTO.class);
	}

	public Profile mapProfile(ProfileDTO dto) {
		return modelMapper.map(dto, Profile.class);
	}

	public ProfileDTO mapProfileDTO(GrupUsrs domain) {
		return modelMapper.map(domain, ProfileDTO.class);
	}

	public GrupUsrs mapGrupUsrs(ProfileDTO model) {
		return modelMapper.map(model, GrupUsrs.class);
	}

}
