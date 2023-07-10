# Getting Started

This is recreated in the Kotlin and Spring Boot application for the Kotlin for Backend Developers mentoring program based on the provided requirements from the task description.



The service uses the [Dog CEO](https://dog.ceo/dog-api/) public API to obtain information about various dog breeds.

### REQUIREMENTS:

This implementation requires Docker to be installed with its tool, Docker Compose.
This is required because the database is created on a dockerized mySQL server and is set up at the start.

**First, inside the main folder, run the command 'docker compose up'.** <br />
mySQL server would be set up, and 'breed_db' database would be created.<br />
-With mySQL server, the phpAdmin client would be set up at localhost:9090.<br />
It's added if there is a need to inspect the database.<br />
<br />
**To log into phpAdmin, users have to:**<br />
    --run command: 'docker container ls', which would list all containers,<br />
    --next run: 'docker inspect [PASTE ID OF A mySQL SERVER FROM STEP BEFORE]'. This should print information about containers that are running,<br />
      along with its IP address.<br />
![img.png](img.png)
This address, along with the database username and password, is required information for the phpAdmin dashboard.<br />

**When this is set up, the user can run applications from the IDE, and under the access link http://localhost:8080/swagger-ui/.** <br />
**All endpoints should be displayed and ready for testing.** <br />
    



