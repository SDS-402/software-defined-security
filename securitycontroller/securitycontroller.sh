#!/bin/sh

# Set paths
SC_HOME=`pwd`
#SC_HOME=`dirname $0`
SC_JAR="${SC_HOME}/target/securitycontroller-1.1.jar"
#SC_JAR="${SC_HOME}/target/sc-storm-0.9.1.jar"
#SC_JAR="${SC_HOME}/target/securitycontroller-1.1.jar"
SC_LOGBACK="${SC_HOME}/logback.xml"

# Set JVM options
JVM_OPTS=""
JVM_OPTS="$JVM_OPTS -server -d64"
JVM_OPTS="$JVM_OPTS -Xmx2g -Xms2g -Xmn800m"
JVM_OPTS="$JVM_OPTS -XX:+UseParallelGC -XX:+AggressiveOpts -XX:+UseFastAccessorMethods"
JVM_OPTS="$JVM_OPTS -XX:MaxInlineSize=8192 -XX:FreqInlineSize=8192"
JVM_OPTS="$JVM_OPTS -XX:CompileThreshold=1500 -XX:PreBlockSpin=8"
JVM_OPTS="$JVM_OPTS -Dpython.security.respectJavaAccessibility=false"


#CP=$CLASSPATH
for s in ${SC_HOME}/lib/*.jar;
do
   CP=${CP}:"$s"
done

# Create a logback file if required
[ -f ${SC_LOGBACK} ] || cat <<EOF_LOGBACK >${SC_LOGBACK}
<configuration scan="true">
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%level [%logger:%thread] %msg%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
    <logger name="org" level="WARN"/>
    <logger name="LogService" level="WARN"/> <!-- Restlet access logging -->
    <logger name="com.sds.securitycontroller" level="INFO"/>
    <logger name="com.sds.securitycontroller.logging" level="ERROR"/>
</configuration>
EOF_LOGBACK

echo "Starting security controller server ..."
java -cp .:${SC_HOME}/config/${CP}:${SC_HOME}/lib  ${JVM_OPTS} -Dlogback.configurationFile=${SC_LOGBACK} -jar ${SC_JAR} -cf ./config/securitycontroller.default.properties
#java -cp .:${SC_HOME}/config/:${CP}:${SC_HOME}/lib  ${JVM_OPTS} -Dlogback.configurationFile=${SC_LOGBACK} -jar ${SC_JAR} -cf ./config/securitycontroller.default.properties.storm-fm.single_node
#java -cp .:${SC_HOME}/config/${CP}:${SC_HOME}/lib  ${JVM_OPTS} -Dlogback.configurationFile=${SC_LOGBACK} -jar ${SC_JAR} -cf ./config/securitycontroller.default.properties.without-fm
