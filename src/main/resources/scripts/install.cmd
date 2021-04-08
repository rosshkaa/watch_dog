@echo off

echo 'Starting Daemon ${project.version} in Background.'

set PATH=%JAVA_HOME%\bin;%PATH%

prunsrv\amd64\prunsrv.exe //IS//webhook_parrot_service --DisplayName="${project.name}_service" --Description="Webhook parrot bot"^
  --Install="%cd%\prunsrv\amd64\prunsrv.exe" --Jvm="%JAVA_HOME%\bin\server\jvm.dll" --StartMode=jvm --StopMode=jvm^
  --Classpath="%cd%\bin\${project.name}-${project.version}.jar" ^
  --LogLevel=DEBUG --LogPath="%cd%" --LogPrefix=procrun^
  --StdOutput="%cd%\stdout.log" --StdError="%cd%\stderr.log" ^
  --Startup=auto ^
  --StartClass com.parrot.Main --StartMethod start ^
  --StopClass com.parrot.Main --StopMethod stop

prunsrv\amd64\prunsrv //ES//webhook_parrot_service