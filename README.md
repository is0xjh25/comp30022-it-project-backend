# comp30022-it-project-backend

## Project Description

This is the backend for team yyds of comp30022 It Project, a personal CRM website to record customers' details

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
* Record recent activity of use
* Able to update user details
* Change password will clean token and send email for safety
* Verify email when user register
* Resend verify email when pending user try to login or reset password
* TodoList backend API, query TodoList and create TodoList
* Move all constants about email to a constant class
* Transfer ownership of organization
* Basic CRUD for Event
* Get recent contact
* Automatically record recent contact when get contact details
* Update&delete todo list
* Search member in department and organization
* Get Event based on time period 
* Get start time of all the events of this user
* Get statistic information of events & to do lists for this user
* Get user’s event amount in a month by given year and month
* Implemented to-do list Get-Create-Update-Delete tests
* Change time zone to GMT
* Add NullOrNotBlank annotation
* Add DTO for page 
* update date-format(LocalDateTime) to yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
* Modify: upload and download User photo
* Add: upload contact photo and return base64 photo
* Modify: move exception to common package
* Add: Leave department and organization
* Add: achieve single sign on(SSO)
* Add: check whether login from new place 

## Installation and Getting Started

### Environment
download [java runtime environment(JRE) 8](https://www.oracle.com/java/technologies/downloads/#java8)

download [MySQL](https://dev.mysql.com/downloads/installer/)

Then go to [release](https://github.com/is0xjh25/comp30022-it-project-backend/releases) and choose the latest release.

### Source
Go to assert and download the java package(.jar) and the `first.sql`

![image](https://user-images.githubusercontent.com/71265122/135340804-e8d9a0ce-644a-43ca-8af3-408682c46ecf.png)

Use `first.sql` to create MySQL database.

Then open a terminal at java package's file and run the java package by
```
java -jar [name of the java package]
```


### Configuration
if you want to set the configuration, create a file name ``` application.yml ```

example ``` application.yml ```
```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://mydb.mysql.database.azure.com:3306/mydb?reconnect=true&serverTimezone=GMT
    username: yyds
    password: comp30022-
  mvc:
    log-resolved-exception: true
    format:
      date-time: ${spring.jackson.date-format}

  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 1000MB

  #jackson
  jackson:
    property-naming-strategy: SNAKE_CASE
    date-format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
    time-zone: GMT

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
    #send email from which mail
    from: connecti.yyds@gmail.com
    #url for active account
    active: https://comp30022-yyds.herokuapp.com/verify
    default-encoding: utf-8

mybatis-plus:
  typeEnumsPackage: tech.crm.crmserver.common.enums
#  configuration:
#    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

```

## Getting Help

Are you having trouble with yyds's Backend Server? We want to help!

Report bugs with yyds's Backend Server at [issues](https://github.com/is0xjh25/comp30022-it-project-backend/issues).

## Reporting Issues

yyds's Backend Server uses GitHub’s integrated issue tracking system to record bugs and feature requests. If you want to raise an issue, please follow the recommendations below:

* Before you log a bug, please search the [issue tracker](https://github.com/is0xjh25/comp30022-it-project-backend/issues) to see if someone has already reported the problem.

* If the issue doesn’t already exist, create a [new issue](https://github.com/is0xjh25/comp30022-it-project-backend/issues/new).

* Please provide as much information as possible with the issue report. We like to know the Spring Boot version, operating system, and JVM version you’re using.

* If you need to paste code or include a stack trace, use Markdown. ``` escapes before and after your text.

* If possible, try to create a test-case or project that replicates the problem and attach it to the issue.

## System Requirements
The backend can be run on any operating system.

List below are the requirements to run the application:

* [Java 8](https://www.oracle.com/java/technologies/downloads/#java8)

We also recommend using [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/) for development.

## Technologies Used

* [Spring boot](https://spring.io/)
* [Spring Security](https://spring.io/projects/spring-security)
* [Maven](https://maven.apache.org/)
* [Mybatis Plus](https://baomidou.com/)
* [lombok](https://projectlombok.org/)
* [jwt](https://jwt.io/)
* [jjwt](https://github.com/jwtk/jjwt)
* [Junit 5](https://junit.org/junit5/)
