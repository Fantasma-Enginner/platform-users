APP_NAME=settings-users
VPS_HOME=/opt/vps
APP_FOLDER="$VPS_HOME"/"$APP_NAME"
SERVICE_NAME=vps-"$APP_NAME"d
PATH_JAR="$APP_FOLDER"/bin/"$APP_NAME".jar

[ -d "/etc/systemd/system" ] && SYSTEMD="1"
echo $SYSTEMD

echo "Seleccione tipo instalacion:"
echo "(1)=Completa [Por defecto]"
echo "(2)= Unicamente Archivos"
echo "(3)= Unicamente Scripts BD"
read -p "-> " _type_install

[ -z "$_type_install" ] && _type_install="1"

if [ "$_type_install" != "3" ]; then

		if [ -z "$SYSTEMD" ]; then
				service "$SERVICE_NAME" status > /dev/null && service "$SERVICE_NAME" stop
		else
				systemctl is-active --quiet $SERVICE_NAME && systemctl stop $SERVICE_NAME
		fi

        [ ! -d "$APP_FOLDER" ] && mkdir -p "$APP_FOLDER"
        scp -r bin "$APP_FOLDER"/
        echo "INFORMACION: Recursos ejecutables copiados ..."

        chown -R root.root "$APP_FOLDER"
        chmod -R 755 "$APP_FOLDER"
        chmod 500 "$PATH_JAR"
        echo "INFORMACION: permisos de ejecución asignados ..."

		if [ -z "$SYSTEMD" ]; then
				ln -s "$PATH_JAR" /etc/init.d/"$SERVICE_NAME"
				chkconfig --add $SERVICE_NAME
				service "$SERVICE_NAME" start
				echo "INFORMACION: servicio instalado y configurado ... SYSV "
		else
				scp -r "$APP_FOLDER"/bin/"$SERVICE_NAME".service /etc/systemd/system/
				systemctl enable "$SERVICE_NAME".service
				systemctl daemon-reload
				systemctl start $SERVICE_NAME
				echo "INFORMACION: servicio instalado y configurado ... SYSTEMD "
		fi

        if [ -z "$CATALINA_HOME" ]; then
                echo "Ingrese la ruta del servidor Tomcat:"
                read -p "-> " CATALINA_HOME
        fi
        EXECUTABLE=bin/catalina.sh

        if [ ! -x "$CATALINA_HOME"/"$EXECUTABLE" ]; then
                echo "No se encuentra $CATALINA_HOME/$EXECUTABLE"
                echo "ADVERTENCIA: La ruta del servidor Web no es correcta. No ha sido posible instalar recursos Web."
                exit 1
        fi

        _folder_app="$CATALINA_HOME"/webapps/vial-plus
        [ ! -d $_folder_app ] && mkdir  -p $_folder_app

        scp -r webapp/* "$CATALINA_HOME"/webapps/vial-plus/
        echo "INFORMACION: Recursos Web copiados"
fi

if [ "$_type_install" != "2" ]; then
        echo "Seleccione motor para scripts de base de datos"
        echo "(1)=PostgreSQL"
        echo "(2)=Oracle"
        read -p "-> " _engineDB
        [ "$_engineDB" = "2" ] && _database="oracle"
        [ "$_engineDB" = "1" ] && _database="postgres"

        if [ -z "$_database" ]; then
                echo "Opcion no valida"
        else
                _fileScripts="database/$_database/run-scripts.sh"
                sudo chmod 500 $_fileScripts
                exec $_fileScripts
        fi
fi
echo "Finalizado"