#!/usr/bin/env sh
# ----------------------------------------------------------------------------
# Maven Wrapper
# ----------------------------------------------------------------------------

basedir=$(dirname "$0")
if [ -z "$basedir" ]; then
  basedir="."
fi

MAVEN_PROJECTBASEDIR="$basedir"

if [ -z "$JAVA_HOME" ]; then
  JAVA_EXE=java
else
  JAVA_EXE="$JAVA_HOME/bin/java"
fi

exec "$JAVA_EXE" -cp "$basedir/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain "$@"
