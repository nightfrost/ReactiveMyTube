# ReactiveMyTube

Contains the backend logic of a private movie streaming platform using Spring Webflux and MongoDB reactively. 

## Description

RMT is currently under development, and many features might be missing. 
The goal is to create the full backend support for streaming movies, user authentication, commenting, playlists and a like/dislike system. Currently we support movie streaming, upload and commenting.
Currently working on user authentication.

## Getting Started
1. Clone the project.
2. Build with gradle.build
3. Configure application.properties (if using default mongodb install, it should be plug-n-play.)
4. Configure MongoConfiguration.java found in src/main/java/io/nightfrost/reactivemytube/configurations/MongoConfiguration.java
4. Run as SpringBootApplication

### Dependencies

* MongoDB 8.+
* Java 21

### Installing

* Follow "Getting Started"
* Build as bootJar with gradle, ``` .\gradlew bootJar ``` 

### Executing program

* Copy built jar from "Installing" into preferable folder. Chef's choice.
* Start any shell (Powershell, bash, prompt) and change directory to given folder.
* Run the jar with your java installation, example: ``` java -jar .\path\to\jar.jar ```

## Authors

Contributors

Lucas
[@Nightfrost](https://github.com/nightfrost)

Oliver
[@Flamme97](https://github.com/Flamme97)

## Version History

* 1.0.0
    * Initial Release

## License

This project is licensed under the [NAME HERE] License - see the LICENSE.md file for details
