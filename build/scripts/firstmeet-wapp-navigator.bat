@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  firstmeet-wapp-navigator startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and FIRSTMEET_WAPP_NAVIGATOR_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\firstmeet-wapp-navigator-1.0-SNAPSHOT.jar;%APP_HOME%\lib\spring-boot-starter-web-2.0.3.RELEASE.jar;%APP_HOME%\lib\tomcat-embed-jasper-8.5.31.jar;%APP_HOME%\lib\redisson-spring-starter-1.0-SNAPSHOT.jar;%APP_HOME%\lib\spring-boot-starter-actuator-2.0.3.RELEASE.jar;%APP_HOME%\lib\micrometer-registry-prometheus-1.3.1.jar;%APP_HOME%\lib\spring-boot-devtools-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-thymeleaf-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-cloud-starter-config-2.0.0.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-data-jpa-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-security-jwt-1.0.8.RELEASE.jar;%APP_HOME%\lib\spring-session-data-redis-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-data-redis-2.0.3.RELEASE.jar;%APP_HOME%\lib\commons-pool2-2.6.1.jar;%APP_HOME%\lib\lombok-1.16.22.jar;%APP_HOME%\lib\commons-lang3-3.8.1.jar;%APP_HOME%\lib\fastjson-1.2.47.jar;%APP_HOME%\lib\druid-spring-boot-starter-1.1.10.jar;%APP_HOME%\lib\mysql-connector-java-5.1.46.jar;%APP_HOME%\lib\spring-boot-starter-json-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-cloud-starter-2.0.0.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-aop-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-jdbc-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-starter-tomcat-2.0.3.RELEASE.jar;%APP_HOME%\lib\hibernate-validator-6.0.10.Final.jar;%APP_HOME%\lib\spring-webmvc-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-cloud-config-client-2.0.0.RELEASE.jar;%APP_HOME%\lib\spring-web-5.0.7.RELEASE.jar;%APP_HOME%\lib\tomcat-embed-websocket-8.5.31.jar;%APP_HOME%\lib\tomcat-embed-core-8.5.31.jar;%APP_HOME%\lib\tomcat-embed-el-8.5.31.jar;%APP_HOME%\lib\ecj-3.12.3.jar;%APP_HOME%\lib\spring-boot-actuator-autoconfigure-2.0.3.RELEASE.jar;%APP_HOME%\lib\micrometer-core-1.3.1.jar;%APP_HOME%\lib\simpleclient_common-0.7.0.jar;%APP_HOME%\lib\spring-boot-actuator-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-autoconfigure-2.0.3.RELEASE.jar;%APP_HOME%\lib\spring-boot-2.0.3.RELEASE.jar;%APP_HOME%\lib\thymeleaf-spring5-3.0.9.RELEASE.jar;%APP_HOME%\lib\thymeleaf-extras-java8time-3.0.1.RELEASE.jar;%APP_HOME%\lib\redisson-3.10.4.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.9.6.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.9.6.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.9.6.jar;%APP_HOME%\lib\jackson-databind-2.9.8.jar;%APP_HOME%\lib\hibernate-core-5.2.17.Final.jar;%APP_HOME%\lib\javax.transaction-api-1.2.jar;%APP_HOME%\lib\spring-data-jpa-2.0.8.RELEASE.jar;%APP_HOME%\lib\spring-aspects-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-security-rsa-1.0.5.RELEASE.jar;%APP_HOME%\lib\bcpkix-jdk15on-1.56.jar;%APP_HOME%\lib\spring-data-redis-2.0.8.RELEASE.jar;%APP_HOME%\lib\spring-session-core-2.0.3.RELEASE.jar;%APP_HOME%\lib\lettuce-core-5.0.4.RELEASE.jar;%APP_HOME%\lib\spring-data-keyvalue-2.0.8.RELEASE.jar;%APP_HOME%\lib\spring-context-support-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-context-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-aop-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-orm-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-jdbc-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-tx-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-oxm-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-data-commons-2.0.8.RELEASE.jar;%APP_HOME%\lib\spring-beans-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-expression-5.0.7.RELEASE.jar;%APP_HOME%\lib\spring-core-5.0.8.RELEASE.jar;%APP_HOME%\lib\spring-boot-configuration-processor-2.0.3.RELEASE.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.33.Final.jar;%APP_HOME%\lib\jackson-annotations-2.9.7.jar;%APP_HOME%\lib\spring-boot-starter-logging-2.0.3.RELEASE.jar;%APP_HOME%\lib\druid-1.1.10.jar;%APP_HOME%\lib\HikariCP-2.7.9.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\log4j-to-slf4j-2.10.0.jar;%APP_HOME%\lib\jul-to-slf4j-1.7.25.jar;%APP_HOME%\lib\thymeleaf-3.0.9.RELEASE.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.9.8.jar;%APP_HOME%\lib\snakeyaml-1.23.jar;%APP_HOME%\lib\validation-api-2.0.1.Final.jar;%APP_HOME%\lib\hibernate-commons-annotations-5.0.1.Final.jar;%APP_HOME%\lib\jboss-logging-3.3.2.Final.jar;%APP_HOME%\lib\classmate-1.3.4.jar;%APP_HOME%\lib\tomcat-annotations-api-8.5.31.jar;%APP_HOME%\lib\simpleclient-0.7.0.jar;%APP_HOME%\lib\spring-cloud-context-2.0.0.RELEASE.jar;%APP_HOME%\lib\spring-cloud-commons-2.0.0.RELEASE.jar;%APP_HOME%\lib\fst-2.57.jar;%APP_HOME%\lib\jackson-core-2.9.8.jar;%APP_HOME%\lib\aspectjweaver-1.8.13.jar;%APP_HOME%\lib\hibernate-jpa-2.1-api-1.0.0.Final.jar;%APP_HOME%\lib\ognl-3.1.12.jar;%APP_HOME%\lib\javassist-3.22.0-GA.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\jandex-2.0.3.Final.jar;%APP_HOME%\lib\dom4j-1.6.1.jar;%APP_HOME%\lib\bcprov-jdk15on-1.56.jar;%APP_HOME%\lib\spring-jcl-5.0.8.RELEASE.jar;%APP_HOME%\lib\reactor-core-3.2.6.RELEASE.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.33.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.33.Final.jar;%APP_HOME%\lib\netty-handler-4.1.33.Final.jar;%APP_HOME%\lib\netty-codec-4.1.33.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.33.Final.jar;%APP_HOME%\lib\netty-transport-4.1.33.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.33.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.33.Final.jar;%APP_HOME%\lib\netty-common-4.1.33.Final.jar;%APP_HOME%\lib\cache-api-1.0.0.jar;%APP_HOME%\lib\rxjava-2.2.7.jar;%APP_HOME%\lib\byte-buddy-1.9.10.jar;%APP_HOME%\lib\jodd-bean-5.0.8.jar;%APP_HOME%\lib\spring-security-crypto-5.0.6.RELEASE.jar;%APP_HOME%\lib\reactive-streams-1.0.2.jar;%APP_HOME%\lib\objenesis-2.5.1.jar;%APP_HOME%\lib\jodd-core-5.0.8.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\log4j-api-2.10.0.jar;%APP_HOME%\lib\HdrHistogram-2.1.11.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\attoparser-2.0.4.RELEASE.jar;%APP_HOME%\lib\unbescape-1.1.5.RELEASE.jar

@rem Execute firstmeet-wapp-navigator
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %FIRSTMEET_WAPP_NAVIGATOR_OPTS%  -classpath "%CLASSPATH%" com.chujian.wapp.navigator.WAppNavigatorApplication %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable FIRSTMEET_WAPP_NAVIGATOR_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%FIRSTMEET_WAPP_NAVIGATOR_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
