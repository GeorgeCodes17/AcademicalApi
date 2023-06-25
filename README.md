# SchoolioApi

### Description
This is the backend API for a school management software meant for teachers & school administrators. It has many endpoints & handles all the data + queries needed

### Technologies
- Java
- Java Spark Framework
- Gradle
- Okta Auth

### Requirements
- OS X
- Gradle 7.6+ with JVM 17.0.6

### Installation
1. Navigate to root of project
2. Run `gradle build` to install dependencies
3. Request all the secrets needed for the `config.properties` file from me (not giving out to public)
4. Run `gradle run` to run the api

### Naming Convention
Variables
- static final vars or var is not calling a method/ class/ is a font = THIS_CASE
- everything else = snakeCase