# QuestLog

This web application aims to help people chose the games they want to play. It does it by providing several features described bellow.

## What was it built on?

* Maven 3.6.3
* Java 8
* Spring Framework 4.2.5
* JPA 1.0.0
* Logback 1.1.2
* Mockito 2.25.1
* JUnit 4.11

## Requirements to run

This web application requires having the following dependencies:

* Maven 3.6.3     (https://maven.apache.org/download.cgi)
* Java 8 JDK      (https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
* Tomcat 7.0.76   (https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.76/bin/)
* PostgreSQL 12   (https://www.postgresql.org/download/)

## How to deploy

1. Clone this repository and go into the `QuestLog` directory:
```bash
git clone https://github.com/prausela/QuestLog.git
cd QuestLog/
```
2. Run the following command to create a Maven webapp.war for Tomcat:
```bash
mvn clean package
```
3. Copy your newly created webapp.war file into your Tomcat webapps folder:
```bash
cp webapp/target/webapp.war ${CATALINA_HOME}/webapps
```
4. Start Tomcat:
```bash
${CATALINA_HOME}/bin/startup.sh
```
5. Open your browser and go to the following url:
http://localhost:8080/webapp_war/
6. That's it! Enjoy choosing your games to play, reviewing them and interacting with other users!
