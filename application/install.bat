@echo off

setlocal
%~d0
cd %~dp0

echo Seleccione tipo instalacion:
echo (1)= Completa [Por defecto].
echo (2)= Unicamente Archivos.
echo (3)= Unicamente Scripts BD.
set /p _typeInstall= "-> "

if "%_typeInstall%" == "" set _typeInstall=1	

if %_typeInstall% equ 3 goto runScripts

if not "%VPS_HOME%" == "" goto checkVPSHome
set VPS_HOME="C:\Software\VPS"

:checkVPSHome
if exist "%VPS_HOME%" goto okVPS
mkdir %VPS_HOME%
:okVPS
set "APP_NAME=settings-users"
set "APP_HOME=%VPS_HOME%\%APP_NAME%"
set "SERVICE_NAME=VPS-%APP_NAME%"

rem Check if service exist and if is running to stop it.
sc query state=all | find "%SERVICE_NAME%"
if %ERRORLEVEL% equ 1 goto copyBin
sc query | find "%SERVICE_NAME%"
if %ERRORLEVEL% equ 1 goto copyBin 
net stop %SERVICE_NAME%
if %ERRORLEVEL% equ 0 goto copyBin
echo ERROR: No ha sido posible detener el servicio %SERVICE_NAME%. No es posible continuar con la instalación.
goto end

if exist %APP_HOME% goto copyBin
mkdir %APP_HOME%
:copyBin
xcopy /e /y /q bin %APP_HOME%\bin\
echo Recursos ejecutables copiados 

sc query state=all | find "%SERVICE_NAME%"
if %ERRORLEVEL% equ 1 %APP_HOME%\bin\%APP_NAME%.exe install
if %ERRORLEVEL% equ 0 goto starService
echo ADVERTENCIA: No ha sido posible instalar el servicio.
goto copyWeb
:starService
net start %SERVICE_NAME%
if %ERRORLEVEL% equ 2 echo ADVERTENCIA: No ha sido posible iniciar el servicio %SERVICE_NAME%. Un usuario con privilegios administrativos debe iniciar el servicio manualmente.  

:copyWeb
if not "%CATALINA_HOME%" == "" goto checkServerHome
echo Ingrese la ruta del servidor Tomcat:
set /p CATALINA_HOME= "-> "
:checkServerHome
if exist "%CATALINA_HOME%\bin\catalina.bat" goto okServerHome
echo ADVERTENCIA: La ruta del servidor Web no es correcta. No ha sido posible instalar recursos Web.
goto end
if exist %CATALINA_HOME%\webapps\vial-plus goto okServerHome
mkdir %CATALINA_HOME%\webapps\vial-plus
:okServerHome
xcopy /e /y /q webapp %CATALINA_HOME%\webapps\vial-plus\
echo Recursos Web Copiados

if %_typeInstall% equ 2 goto end

:runScripts
echo Seleccione motor para scripts de base de datos: 
echo (1)=PostgreSQL
echo (2)=Oracle
set /p _engineDB= "-> "
if "%_engineDB%" equ "2" set _database=oracle
if "%_engineDB%" equ "1" set _database=postgres

if "%_database%" == "" (
	echo "Opcion no valida"
	goto runScripts
)

call "database\%_database%\run-scripts.bat"

:end
echo Finalizado
pause