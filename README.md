# Polls API

User polls for gathering live responses on various topic.

> #### Why polls application was created?

>This application is created to:
> - Serve the objective of learning and building REST API with Java and Spring Boot.
> - Understand and employ TDD and SOLID principles.
> - Develop quality API by keeping OWASP REST Api Security Principles in mind.
 
### Steps to Setup the Spring Boot Backend API

 1. **Clone the application**
 
 	```bash
 	git clone https://github.com/callicoder/spring-security-react-ant-design-polls-app.git
 	cd polling-app-server
 	```
 
 2. **Create MySQL database**
 
 	```bash
 	create database polls_db
 	```
 
 3. **Change MySQL username and password as per your MySQL installation**
 
 	+ open `src/main/resources/application.properties` file.
 
 	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation
 
 4. **Run the app**
 
 	You can run the spring boot app by typing the following command -
 
 	```bash
 	mvn spring-boot:run
 	```
 
 	The server will start on port 8080.
 
 	You can also package the application in the form of a `jar` file and then run it like so -
 
 	```bash
 	mvn package
 	java -jar target/polls-0.0.1-SNAPSHOT.jar
 	```
 5. **Default Roles**
 	
 	The spring boot app uses role based authorization powered by spring security. To add the default roles in the database, I have added the following sql queries in `src/main/resources/data.sql` file. Spring boot will automatically execute this script on startup -
 
 	```sql
 	INSERT IGNORE INTO roles(name) VALUES('ROLE_USER');
 	INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');
 	```
 
 	Any new user who signs up to the app is assigned the `ROLE_USER` by default.
