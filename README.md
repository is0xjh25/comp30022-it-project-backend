# comp30022-it-project-backend

## Project Description

This is the backend for team yyds of comp30022 It Project.

## Features
* backend security
* Login/logout
* Register
* Forget password (Send an email with a random generated password)
* Retrieve a member’s organizations as json data
* Create an organization
* Search organization base on name
* Join an orgainzation
* Retrieve departments in an organization as json data
* Create an department
* Retrieve all members(sorted, query page number) in the department as json
* Change a member’s permission/Accept a member
* Delete a member from the department
* Request to join a department
* Retrieve search results as json
* Retrieve all contacts in a department
* Get customer detail
* Insert customer detail into db
* Update customer detail into db
* Delete customer in db

## Installation and Getting Started

### environment
download java runtime environment(JRE) 8 [link to Java download](https://www.oracle.com/java/technologies/downloads/#java8)

Then go to release [link to release](https://github.com/is0xjh25/comp30022-it-project-backend/releases) and choose the latest release.

### source
Go to assert and download the java package(.jar)
![image](https://user-images.githubusercontent.com/71265122/134913809-5631de25-8731-4534-978a-f3e84fb362a0.png)

Then open a terminal at java package's file and run the java package by
```
java -jar [name of the java package]
```


### Configuration
if you want to set the configuration, create a file name ``` application.yml ```

example ``` application.yml ```
```yml
spring:
  #datasource: mysql
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    #url of mysql
    url: jdbc:mysql://localhost:3306/mydb
    #user name
    username: root
    #password
    password: 415623
  mvc:
    log-resolved-exception: true

  #jackson
  jackson:
    property-naming-strategy: SNAKE_CASE
  
  #mail config
  mail:
    host: smtp.gmail.com
    username: connecti.yyds@gmail.com
    password: ysqujsagxxfyqwvm
    protocol: smtp
    properties:
      mail:
        smtp:
          auth: true
          port: 465
          ssl:
            enable: true
          starttls:
            enable: true
            required: true
    #send from where
    from: connecti.yyds@gmail.com
    default-encoding: utf-8
#debug mode
#debug: true

mybatis-plus:
  typeEnumsPackage: tech.crm.crmserver.common.enums
  configuration:
    #print sql excuted to stdout
    #log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

## Getting Help

Are you having trouble with yyds's backend server? We want to help!

Report bugs with Spring Boot at [issues](https://github.com/is0xjh25/comp30022-it-project-backend/issues).

## Reporting Issues

yyds's Backend Server uses GitHub’s integrated issue tracking system to record bugs and feature requests. If you want to raise an issue, please follow the recommendations below:

* Before you log a bug, please search the [issue tracker](https://github.com/is0xjh25/comp30022-it-project-backend/issues) to see if someone has already reported the problem.

* If the issue doesn’t already exist, create a [new issue](https://github.com/is0xjh25/comp30022-it-project-backend/issues/new).

* Please provide as much information as possible with the issue report. We like to know the Spring Boot version, operating system, and JVM version you’re using.

* If you need to paste code or include a stack trace, use Markdown. ``` escapes before and after your text.

* If possible, try to create a test-case or project that replicates the problem and attach it to the issue.

## System REquirements
The backend can be run on any operating system.

List below are the requirements to run the application:

* [Java 8](https://www.oracle.com/java/technologies/downloads/#java8)

We also recommend using [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/) for development.

## Technologies Used

* [Spring boot](https://spring.io/)
* [Maven](https://maven.apache.org/)
