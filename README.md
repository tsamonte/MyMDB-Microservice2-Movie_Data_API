# MyMDB-Microservice2-Movie_Data_API

## About the Project

MyMDB is a project showcasing my knowledge of Web API Design and Implementation. MyMDB is a Movie Catalog web application where you can search for and buy movies.

The Movie Data API is the second of three microservices that make up the backend of the application. It is the outward-facing portion of the web application where requests involving movie data are handled. The other microservices of the web application are all working to support interactions with the Movie Data API. This microservice is entirely focused on all content involving movies.

### Built With
- [Java 8](https://www.java.com/en/download/help/java8.html) (Gradle, Jersey, Grizzly, JSON, JDBC)
- [MySQL](https://www.mysql.com/)

### API Documentaion
[Movie Data API Documentation](https://docs.google.com/document/d/1c6HKlOrwnKFkZ_xbR4GvTiVLzLhkPxFFAxbKVdluOew/edit?usp=sharing)


## Getting Started

### Prerequisites
This API requires an instance of the [Identity Management API](https://github.com/tsamonte/MyMDB-Microservice1-Identity_Management_API) to be run. Refer to the following repository to find directions on how to set up that project first:
[Identity Management API](https://github.com/tsamonte/MyMDB-Microservice1-Identity_Management_API)

To run this project locally, you must be able to run [JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html) Java Projects, as well as have a MySQL database to connect to the backend. A [local MySQL database](https://www.mysql.com/downloads/) instance would suffice.


### Installation
```
git clone https://github.com/tsamonte/MyMDB-Microservice2-Movie_Data_API
```
After cloning this repo into your local system, no further library downloads  in Java are needed manually. Required packages and libraries are handled the build automation tool [Gradle](https://gradle.org/)


### MySQL Database Initialization
Within your MySQL database, initialize the database by running the files found in the [db_scripts directory](https://github.com/tsamonte/MyMDB-Microservice2-Movie_Data_API/tree/master/db_scripts) of the repository:
- CreateMoviesDB.sql
- gender.sql
- person-1.sql
- person-2.sql
- person-3.sql
- movies.sql
- genre.sql
- keyword.sql
- genre_in_movie.sql
- person_in_movie.sql
- keyword_in_movie.sql

Ensure these files are run in the order above.


### Java Configuration File
Before running the project, create a configuration file called "config.yaml" in the root of the Java project. The structure of "config.yaml" should be as follows:
```yaml
serviceConfig:
  scheme: http://
  hostName: 0.0.0.0
  port: 12346
  path: /api/movies

loggerConfig:
  outputDir: ./logs/
  outputFile: test.log

databaseConfig:
  dbUsername: 
  dbPassword: 
  dbHostname: 
  dbPort: 
  dbDriver: com.mysql.cj.jdbc.Driver
  dbName: MyMDB_MOVIES
  dbSettings: ?autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST


idmConfig:
  scheme: http://
  hostName: 0.0.0.0
  port: 12345
  path: /api/idm

idmEndpoints:
  privilege: /privilege
```
Fill in the blank fields with your information.

## Usage
After completing all all prerequisites, you can build the project using the following command:
```
gradlew build
```

Run the project using the following command:
```
java -jar build/libs/tsamonte.service.movies.jar -c config.yaml
```
