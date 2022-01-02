SET SERVEROUTPUT ON;
DECLARE
	V_COUNT NUMBER;
	V_TABLE_NAME VARCHAR2(32);
	V_PROP_LEGACY NUMBER;

BEGIN
	V_PROP_LEGACY := 1;
	IF V_PROP_LEGACY = 1 THEN
		V_TABLE_NAME := 'GRUP_USRS';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE GRUP_USRS (
					ID_GRUP_USRS NUMBER(19) NOT NULL,
					CODIGO_GRUP_USRS NUMBER(19) NOT NULL, 
					NOMBRE_GRUP_USRS VARCHAR2(30) NOT NULL,
					ESTADO_GRUP_USRS VARCHAR2(2) NOT NULL,
					CONSTRAINT PK_GRUP_USRS PRIMARY KEY(ID_GRUP_USRS),
					CONSTRAINT GRUP_COD_UK UNIQUE(CODIGO_GRUP_USRS),
					CONSTRAINT GRUP_COD_UK2 UNIQUE(NOMBRE_GRUP_USRS),
					CONSTRAINT ESTADO_GRUPO CHECK (ESTADO_GRUP_USRS IN (0, 1, 2, 3)))');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN GRUP_USRS.ID_GRUP_USRS IS ''Identificador del registro del grupo de usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN GRUP_USRS.CODIGO_GRUP_USRS IS ''Codigo único del grupo de usuario para el sistema.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN GRUP_USRS.NOMBRE_GRUP_USRS IS ''Nombre establecido para el grupo.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN GRUP_USRS.ESTADO_GRUP_USRS IS ''Codigo de estado de activación del grupo.''');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE GRUP_USRS IS ''Tabla que contiene los grupos de usuarios existentes en el sistema''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
			SELECT count(1) INTO V_COUNT from user_constraints where constraint_name = 'ESTADO_GRUPO';
			IF (V_COUNT > 0) THEN 
				EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' DROP CONSTRAINT ESTADO_GRUPO');
			END IF;
			EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' ADD CONSTRAINT ESTADO_GRUPO CHECK (ESTADO_GRUP_USRS in (0,1,2,3))');
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;	
		
		V_TABLE_NAME := 'FOTO';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE FOTO (
					ID_FOTO NUMBER(19) NOT NULL,
					FOTO BLOB,
					CONSTRAINT PK_FOTO PRIMARY KEY(ID_FOTO))');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN FOTO.ID_FOTO IS ''Identificador del usuario al que pertenece la foto.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN FOTO.FOTO IS ''Contenido de la imagen.''');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE FOTO IS ''Tabla en la cual se almacena la foto que se carga cuando se realiza el proceso de enrolamiento de usuarios''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;
		
		V_TABLE_NAME := 'HUEL';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE HUEL (
					ID_HUEL NUMBER(19) NOT NULL,
					HUELLA BLOB,
					MANO NUMBER(1) NOT NULL,
					DEDO NUMBER(1) NOT NULL,
					CONSTRAINT HUEL_PK PRIMARY KEY(ID_HUEL))');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN HUEL.ID_HUEL IS ''Identificador del registro. Corresponde al identificador del usuario al cual pertenece la huella.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN HUEL.HUELLA IS ''Contenido de la plantilla de la huella dactilar.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN HUEL.MANO IS ''Código de la mano a la que corresponde la huella dactilar. Derecha = 0, Izquierda = 1''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN HUEL.DEDO IS ''Código del dedo al que corresponde la huella dactilar.Pulgar = 0, Índice = 1, Corazón = 2, Anular= 3, Meñique = 4''');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE HUEL IS ''Tabla que almacena la plantilla binaria que contiene la huella de los usuarios para sistemas que cuentan con identificación biometrica''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;	
		
		V_TABLE_NAME := 'USRS';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE USRS (
					ID_USRS NUMBER(19) NOT NULL,
					ID_GRUP_USRS NUMBER(19),
					ID_ESTA NUMBER(19),
					ID_HUEL NUMBER(19),
					ID_FOTO NUMBER(19),
					ID_TISC VARCHAR2(10), 
					CODIGO_USRS NUMBER(10) NOT NULL,
					NOMBRE_USRS VARCHAR2(50) NOT NULL,
					APELLIDO_USRS VARCHAR2(50) NOT NULL,
					ESTADO_USRS VARCHAR2(2) DEFAULT 0 NOT NULL,
					CLAVE_USRS VARCHAR2(70) NOT NULL,
					PWD_FECHA TIMESTAMP(6),
					ESTA_CCO NUMBER,
					CONSTRAINT PK_USRS PRIMARY KEY (ID_USRS),
					CONSTRAINT USRS_USRS_UK1 UNIQUE (CODIGO_USRS),
					CONSTRAINT USRS_FOTO_FK FOREIGN KEY(ID_FOTO) 
						REFERENCES FOTO(ID_FOTO),
					CONSTRAINT USRS_GRUP_FK FOREIGN KEY(ID_GRUP_USRS) 
						REFERENCES GRUP_USRS(ID_GRUP_USRS),
					CONSTRAINT USRS_HUEL_FK1 FOREIGN KEY(ID_HUEL) 
						REFERENCES HUEL(ID_HUEL),
					CONSTRAINT ESTADO_USRS CHECK (ESTADO_USRS IN (0, 1, 2, 3))');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ID_USRS IS ''Identificador del registro.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ID_GRUP_USRS IS ''Identificador del grupo al que pertenece el usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ID_ESTA IS ''Identificador del la estación a la que se encuentra asociado el usuario''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ID_HUEL IS ''Identificador de la plantilla de huella registrado.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ID_FOTO IS ''Identificador del la imagen registrada por el usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ID_TISC IS ''Identificador de la tarjeta de ingreso asignada al usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.CODIGO_USRS IS ''Codigo de identificacion del usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.NOMBRE_USRS IS ''Nombres de la persona.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.APELLIDO_USRS IS ''Apellidos de la persona.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ESTADO_USRS IS ''Estado del usuario en el sistema.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.CLAVE_USRS IS ''Contraseña de autenticacion del usuario para el sistema VIAL.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.PWD_FECHA IS ''Ultima fecha de actualizacion de la contraseña.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USRS.ESTA_CCO IS ''Codigo de estación de ingreso permitido al CCO''');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE USRS IS ''Tabla que almacena los usuarios que se encuentran registrados en la aplicación''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
			SELECT count(1) INTO V_COUNT from user_constraints where constraint_name = 'ESTADO_USRS';
			IF (V_COUNT > 0) THEN 
				EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' DROP CONSTRAINT ESTADO_USRS');
			END IF; 
			EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' ADD CONSTRAINT ESTADO_USRS CHECK (ESTADO_USRS in (0,1,2,3))');
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;

		V_TABLE_NAME := 'CONTACTS';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE CONTACTS (
					CONTACT_ID NUMBER(19),
					USER_ID NUMBER(19) NOT NULL,
					ADDRESS VARCHAR2(64),
					PHONE_NUMBER VARCHAR2(16),
					EMAIL VARCHAR2(128),
					EMERGENCY_NAME VARCHAR2(64),
					EMERGENCY_PHONE VARCHAR2(16),
					CONSTRAINT CONTACTS_PK PRIMARY KEY (CONTACT_ID),
					CONSTRAINT CONTACT_USER_FK FOREIGN KEY (USER_ID) REFERENCES USRS (ID_USRS))');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE CONTACTS IS ''Almacena los registros de datos de contacto relacionados a un usuario de la plataforma.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.CONTACT_ID IS ''Identificador de l registro de contacto.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.USER_ID IS ''Identificador del usuario al cual le pertenecen los datos de contacto del registro.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.ADDRESS IS ''Dirección de residencia del usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.PHONE_NUMBER IS ''Numero telefonoco o celular del usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.EMAIL IS ''Direccion de corredo electrónico.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.EMERGENCY_NAME IS ''Nombre de la persona de contacto en caso de emergencia.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.EMERGENCY_PHONE IS ''Telefono de la persona de contacto en caso de emergencia.''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;	
	ELSE		
		V_TABLE_NAME := 'PROFILES';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE PROFILES (
					PROFILE_ID NUMBER(19),
					NAME VARCHAR2(30) NOT NULL, 
					STATE VARCHAR2(2) NOT NULL,
					CONSTRAINT PROFILES_PK PRIMARY KEY (PROFILE_ID),
					CONSTRAINT PROFILE_NAME_UK UNIQUE (NAME),
					CONSTRAINT PROFILE_STATE_CK CHECK (STATE IN (0, 1, 2, 3))');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE PROFILES IS ''Tabla que almacena la información básica de los perfiles definidos bajo los cuales se agrupan los usuarios registrados para la operación de la plataforma VIAL+.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN PROFILES.PROFILE_ID IS ''Código identificador del perfil.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN PROFILES.NAME IS ''Nombre de presentación.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN PROFILES.STATE IS ''Código de representación del estado de activación. 0=Inactivo, 1=Activo, 2=Suspendido.''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
			SELECT count(1) INTO V_COUNT from user_constraints where constraint_name = 'PROFILE_STATE_CK';
			IF (V_COUNT > 0) THEN 
				EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' DROP CONSTRAINT PROFILE_STATE_CK');
			END IF; 
			EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' ADD CONSTRAINT PROFILE_STATE_CK CHECK (STATE in (0,1,2,3))');
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;	

		V_TABLE_NAME := 'USERS';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE USERS (
					USER_ID NUMBER(19),  
					FIRST_NAME VARCHAR2(50) NOT NULL, 
					LAST_NAME VARCHAR2(50) NOT NULL, 
					STATE NUMBER(2) DEFAULT 0 NOT NULL, 
					PROFILE_ID NUMBER(19), 
					TOLL_ID NUMBER(19),
					CONSTRAINT USERS_PK PRIMARY KEY (USER_ID),
					CONSTRAINT USERS_PROFILE_FK FOREIGN KEY (PROFILE_ID) REFERENCES PROFILES(PROFILE_ID),
					CONSTRAINT USER_STATE_CK CHECK (STATE IN (0, 1, 2, 3))');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE USERS IS ''Tabla que almacena la información básica de los usuarios que se encuentran registrados para la operación de la plataforma VIAL+.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USERS.USER_ID IS ''Número de identificación del usuario''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USERS.FIRST_NAME IS ''Nombre o nombres.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USERS.LAST_NAME IS ''Apellidos.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USERS.STATE IS ''Estado de activación del usuario. 0=Inactivo, 1=Activo, 2= Suspendido.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USERS.PROFILE_ID IS ''Identificador del perfil al cual pertenece.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN USERS.TOLL_ID IS ''Identificador de la estación a la cual está relacionada el usuario.''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
		SELECT count(1) INTO V_COUNT from user_constraints where constraint_name = 'USER_STATE_CK';
			IF (V_COUNT > 0) THEN 
				EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' DROP CONSTRAINT USER_STATE_CK');
			END IF; 
			EXECUTE IMMEDIATE ('ALTER TABLE '|| V_TABLE_NAME ||' ADD CONSTRAINT USER_STATE_CK CHECK (STATE in (0,1,2,3))');
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;
		
		V_TABLE_NAME := 'CREDENTIALS';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE CREDENTIALS (
					CREDENTIAL_ID NUMBER(19),
					USER_ID NUMBER(19),
					PASSWORD VARCHAR2(70), 
					PSSW_DATE TIMESTAMP,
					TEMPLATE BLOB,
					HAND NUMBER(2),
					FINGER NUMBER(2),
					TISC_ID VARCHAR2(16),
					CONSTRAINT CREDENTIALS_PK PRIMARY KEY (CREDENTIAL_ID),
					CONSTRAINT CREDENTIAL_USER_FK FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID))');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE CREDENTIALS IS ''Almacena las credenciales o datos de los diferentes medios de autenticación permitidos en la plataforma registradas por los usuarios.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.CREDENTIAL_ID IS ''Identificador del registro de credenciales.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.USER_ID IS ''Identificador del usuari al cual le pertencecen las credenciales.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.PASSWORD IS ''Palabra clave o contraseña.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.PSSW_DATE IS ''Ultima fecha de cambio de la contraseña.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.TEMPLATE IS ''Plantilla de la huella biometrica registrada por el usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.HAND IS ''Codigo de la mano a la cual pertence la huella registrada.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.FINGER IS ''Codigo del dedo al cual pertenece la huella registrada.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CREDENTIALS.TISC_ID IS ''Identificador de la tarjeta de asistencia asiganada al usuario.''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
		
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;
		
		V_TABLE_NAME := 'CONTACTS';
		SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
		IF V_COUNT = 0 THEN
			 EXECUTE IMMEDIATE ('
				CREATE TABLE CONTACTS (
					CONTACT_ID NUMBER(19),
					USER_ID NUMBER(19) NOT NULL,
					ADDRESS VARCHAR2(64),
					PHONE_NUMBER VARCHAR2(16),
					EMAIL VARCHAR2(128),
					EMERGENCY_NAME VARCHAR2(64),
					EMERGENCY_PHONE VARCHAR2(16),
					CONSTRAINT CONTACTS_PK PRIMARY KEY (CONTACT_ID),
					CONSTRAINT CONTACT_USER_FK FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID))');
			EXECUTE IMMEDIATE ('COMMENT ON TABLE CONTACTS IS ''Almacena los registros de datos de contacto relacionados a un usuario de la plataforma.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.CONTACT_ID IS ''Identificador de l registro de contacto.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.USER_ID IS ''Identificador del usuario al cual le pertenecen los datos de contacto del registro.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.ADDRESS IS ''Dirección de residencia del usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.PHONE_NUMBER IS ''Numero telefonico o celular del usuario.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.EMAIL IS ''Direccion de corredo electrónico.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.EMERGENCY_NAME IS ''Nombre de la persona de contacto en caso de emergencia.''');
			EXECUTE IMMEDIATE ('COMMENT ON COLUMN CONTACTS.EMERGENCY_PHONE IS ''Telefono de la persona de contacto en caso de emergencia.''');
			DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
		ELSE 
			DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
		END IF;	
	END IF;
	
	V_TABLE_NAME := 'AUDIENCES';
	SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
	IF V_COUNT = 0 THEN
		 EXECUTE IMMEDIATE ('
			CREATE TABLE AUDIENCES (
				AUDIENCE_ID NUMBER(8), 
				LABEL VARCHAR2(32) NOT NULL,
				CONSTRAINT AUDIENCES_PK PRIMARY KEY (AUDIENCE_ID))');
		EXECUTE IMMEDIATE ('COMMENT ON COLUMN AUDIENCES.AUDIENCE_ID IS ''Identificador de la audiencia.''');
		EXECUTE IMMEDIATE ('COMMENT ON COLUMN AUDIENCES.LABEL IS ''Nombre o etiqueta de la audiencia.''');
		EXECUTE IMMEDIATE ('COMMENT ON TABLE AUDIENCES IS ''Almacena las diferentes audiencias o conjunto de nodos a las cuales aplica un permiso o autorización.''');
		DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
	ELSE 
		DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
	END IF;		
	
	V_TABLE_NAME := 'AUTHORIZATIONS';
	SELECT count(1) INTO V_COUNT from user_tables where table_name = V_TABLE_NAME;
	IF V_COUNT = 0 THEN
		 EXECUTE IMMEDIATE ('
			CREATE TABLE AUTHORIZATIONS (
				AUTHORIZATION_ID VARCHAR2(32), 
				RESOURCE_ID NUMBER(16) NOT NULL, 
				PROFILE_ID NUMBER NOT NULL, 
				AUDIENCE_ID NUMBER(8),
				CONSTRAINT AUTHORIZATIONS_PK PRIMARY KEY (AUTHORIZATION_ID),
				CONSTRAINT AUTHORIZATION_AUDIENCE_FK FOREIGN KEY (AUDIENCE_ID)
				  REFERENCES AUDIENCES (AUDIENCE_ID))');
		EXECUTE IMMEDIATE ('COMMENT ON COLUMN AUTHORIZATIONS.AUTHORIZATION_ID IS ''Identificador del registro de autorización.''');
		EXECUTE IMMEDIATE ('COMMENT ON COLUMN AUTHORIZATIONS.RESOURCE_ID IS ''Identificador del recurso relacionado a la autorización.''');
		EXECUTE IMMEDIATE ('COMMENT ON COLUMN AUTHORIZATIONS.PROFILE_ID IS ''Identificador del perfil al cual se asigna el acceso al recurso relacionado.''');
		EXECUTE IMMEDIATE ('COMMENT ON COLUMN AUTHORIZATIONS.AUDIENCE_ID IS ''Nivel o nodo en el cual se asigna acceso al recurso.''');
		EXECUTE IMMEDIATE ('COMMENT ON TABLE AUTHORIZATIONS  IS ''Tabla de relación que almacena los permisos de acceso de los perfiles de usuarios a los recursos registrados en la plataforma.''');
		DBMS_OUTPUT.PUT_LINE('TABLA CREADA SATISFACTORIAMENTE: '|| V_TABLE_NAME);
	ELSE 
		DBMS_OUTPUT.PUT_LINE('TABLA VALIDADA: ' || V_TABLE_NAME);
	END IF;		

END;
