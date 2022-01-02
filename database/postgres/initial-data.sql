DO $$
DECLARE
	V_COUNT NUMERIC;
	V_PROP_LEGACY NUMERIC;

BEGIN
	V_PROP_LEGACY := 1;
	
	IF V_PROP_LEGACY = 1 THEN
		SELECT COUNT(1) INTO V_COUNT FROM GRUP_USRS WHERE ID_GRUP_USRS = 1;
		IF V_COUNT = 0 THEN
			INSERT INTO GRUP_USRS (ID_GRUP_USRS,CODIGO_GRUP_USRS,NOMBRE_GRUP_USRS,ESTADO_GRUP_USRS) 
				VALUES ('1','1','DEFAULT','1');
		END IF;
		
		SELECT COUNT(1) INTO V_COUNT FROM USRS WHERE ID_USRS = 147;
		
		IF V_COUNT = 0 THEN
			INSERT INTO USRS (ID_USRS,ID_GRUP_USRS,ID_ESTA,ID_HUEL,ID_FOTO,ID_TISC,CODIGO_USRS,NOMBRE_USRS,APELLIDO_USRS,ESTADO_USRS,CLAVE_USRS,PWD_FECHA,ESTA_CCO) 
				VALUES ('147','1','0',null,null,null,'147','USER','DEFAULT','1','ICy5YqxZB1uWSwcVLSNLcA==',CURRENT_TIMESTAMP,null);
		END IF;

	ELSE
		
		SELECT COUNT(1) INTO V_COUNT FROM PROFILES WHERE PROFILE_ID = 1;
		IF V_COUNT = 0 THEN
			INSERT INTO PROFILES (PROFILE_ID,NAME,STATE) 
				VALUES ('1','DEFAULT','1');
		END IF;
		
		SELECT COUNT(1) INTO V_COUNT FROM USERS WHERE USER_ID = 147;
		
		IF V_COUNT = 0 THEN
			INSERT INTO USERS (USER_ID,FIRST_NAME,LAST_NAME,STATE,PROFILE_ID,TOLL_ID) 
				VALUES ('147','USER','DEFAULT','1','1','0');
			INSERT INTO CREDENTIALS (CREDENTIAL_ID,USER_ID,PASSWORD,PSSW_DATE,HAND,FINGER,TISC_ID) 
				VALUES ('147','147','{bcrypt}$2a$10$8KognaOLsCTIm35zajP6Su.N8FSUjvNCayUY1FBEVWaeeNkJytiDa',CURRENT_TIMESTAMP,null,null,null);
		END IF;
		
	END IF;

	SELECT COUNT(1) INTO V_COUNT FROM AUTHORIZATIONS WHERE PROFILE_ID = 1;
	IF V_COUNT = 0 THEN
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010001000100000001','281479271677953','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010001000100000002','281479271677954','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010001000100000004','281479271677956','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010001000100000008','281479271677960','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010001000100000010','281479271677968','1',null);

		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010002000100000001','562954248388609','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010002000100000004','562954248388612','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010002000200000001','562958543355905','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010002000200000004','562958543355908','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010002000200000010','562958543355920','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010002000200000020','562958543355936','1',null);
		INSERT INTO AUTHORIZATIONS (AUTHORIZATION_ID,RESOURCE_ID,PROFILE_ID,AUDIENCE_ID) VALUES ('010002000200000040','562958543355968','1',null);
	END IF;
	COMMIT;
END $$;