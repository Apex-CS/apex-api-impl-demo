FROM docker.fnis.com/redhat-openjdk-18/openjdk18-openshift

ARG pomVersion='no-pomVersion-defined'
ENV JAVA_APP_JAR=/deployments/api-{{API_NAME}}-{{CORE}}-server-${pomVersion}.jar

COPY target/*.jar /deployments/
