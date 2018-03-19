## **Training task for Spring MVC, Spring data JPA, Hibernate** ##
####**TASK DEFINITION**####
Create an app that has CRUD (create/read/update/delete) operations implemented for object called Dog via REST.<br>
_Properties of a dog:_

* name - 1-100 symbols.
* date of birth - must be before NOW, optional
* height, weight - must be greater than 0

<h5><i>Protocol:</i></h5>

* POST /dog - create a new dog.
* GET /dog/{id} - shows the dog’s props in JSON format
* PUT /dog/{id} - updates the dog (dog JSON is sent in request body)
* DELETE /dog/{id} - removes the dog from DB

<h5><i>The app needs to be tested on all levels:</i></h5>

 - Unit tests to test validation
 - DAO tests to test how the entities are saved into DB (HSQLDB can be instantiated during test start)
 - Component REST tests
 - System REST tests - run against fully deployed app, send actual HTTP requests

<h6><i>Note:</i></h6> All tests should be executed as a part of CI/CD pipeline through Jenkins

###How to:###
-------------
To create .war file and run unit test use command: ``mvn clean install``

To run system test you need to deploy .war file on server and then use command : ``mvn test -Dtest=SystemTest``

To deploy .war file you need:
1) download JBOSS server(wildfly 11.0.0.Final) and unzip
2) configure local JBosss server in IntelliJ Idea(Run/Debug Configurations -> "+" -> JBoss Server -> Local)
3) in created Jboss runner please select "Server tab"
 - add URL(e.g. "http://localhost:8080/") and VM options "-Dspring.profiles.active=test"
4) Select "Deployment" tad and add .war file 
5) Select "Startup/Connection" tab and in Start script enter "path_to_your_wildfly/bin/standalone.sh"
6) Don't forget save all changes =)
7) Select Jboss runner and press "Run" button. The default browser with localhost url should open. 

<h5><i>Used technologies:</i></h5>

 - Maven
 - HSQLDB(Embedded and In memory), Hibernate, HibernateValidator
 - JBoss, Jackson2
 - Spring MVC
 - Spring data JPA
 - Version control: Git, GitHub
 - Testing: TestNG, RestAssured, MockMVC, Mockito
 - CI/CD: Jenkins, Travis CI
 
