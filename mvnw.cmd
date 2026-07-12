@echo off
REM ----------------------------------------------------------------------------
REM Maven Wrapper
REM ----------------------------------------------------------------------------
SETLOCAL

SET BASEDIR=%~dp0
IF "%BASEDIR%"=="" SET BASEDIR=.

IF DEFINED JAVA_HOME (
  SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) ELSE (
  SET "JAVA_EXE=java"
)

REM Use classpath invocation to run the Maven wrapper main class
"%JAVA_EXE%" -cp "%BASEDIR%\.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
