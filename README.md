## **Training task for Spring MVC, Spring data JPA, Hibernate** ##
####**TASK DEFINITION**####
Create an app that has CRUD (create/read/update/delete) operations implemented for object called Dog via REST.
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
 - Component REST tests - use TestFramework
 - System REST tests - run against fully deployed app, send actual HTTP requests

<h6><i>Note:</i></h6> All tests should be executed as a part of CI/CD pipeline through Jenkins

How to:
To deploy .war file to server. you nee to start server via /bin/standalone.sh
and then use command ``mvn wildfly:deploy``.
To undeploy please use ``mvn wildfly:undeploy``

<h5><i>Used technologies:</i></h5>
 - Maven
 - HSQLDB(Embedded and In memory), Hibernate, HibernateValidator
 - JBoss, Jackson2
 - Spring MVC
 - Spring data JPA
 - Version control: Git, GitHub
 - Testing: TestNG, RestAssured, MockMVC, Mockito
 - CI/CD: Jenkins
 
**TODO:**
---------
  
  
 Points       | Done ?        
------------- |------ 
Use Spring DAO instead of try&catch construction in DogDAOImpl.class| Yes
Switch on validation via Spring Validator(Hibernate validator)| Yes
Create mechanism for switching between two DB| Yes. Needs review
Create unit tests|No
Create integration tests for controller methods |No
Create integration tests for DB |No
Create system tests |No
Run all tests on Jenkins |No
Remove unused constructions |No
Remove dependencies on strict system path for embedded url prop| No
Create drop sql script| Yes
Investigate how to run system test(firesave plugin)|No
 
