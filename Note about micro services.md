C:\Users\Htaung\Desktop\Temp Ebook\freetutorials eu\microservices-with-spring-boot-and-spring-cloud [FreeTutorials.Us]\03 Restful Web Services with Spring Boot 


Spring rest to return location in response header.
ResponseEntity.create(location).build();

throwing Exception spring boot will automatically handle respose status as 500 internal server error
to customize it
just by extending RuntimeException and add annotation @ResponseStatus(HttpStatus.NotFound)


To customized the Exception handling in rest
extends ResponseEntityExceptionHandler

C:\Users\Htaung\Desktop\Temp Ebook\freetutorials eu\microservices-with-spring-boot-and-spring-cloud [FreeTutorials.Us]\03 Restful Web Services with Spring Boot
25 26

To validate a request
just add @valiate in @RequestBody

birthdate have @past validation


HATEOAS => Hipermedia as the Engine of Application State
To provide a useful resource link in web service response,

Resource<User> resource = new Resource<User>(user);
  ControllerLinkBuilder linkto 
  resource.add(linkto.withRel("all-users")
  
  
  Internationalization in web service
  Configure LocalResolver
  ResourceBundleMessageSource
  Add Locale in request method
  public String hello(@RequestHeader(name="Accept-Language") Locale locale)
  call messageSource.getMessage() from message.properties message_fr.properties
  
  don't need to add RequestHeader in spring boot
  just by adding AcceptHeaderLocaleResolver
  
  don't need to configure in ResourceBundleMessageSource
  just by adding application.properties as spring.message.basename=message
  
  
  To provide xml just add jacksondatabind xml in pom.xml
  
  to provide WSDL in Rest
  add springfox-swagger2 and springfox-swagger-ui
  
  Add SwaggerConfig
  swagger-ui.html 
  in swagger config can define api info as our desire content
  
  Customizing Swagger
  in entity or dto 
  to provide User information
  @ApiModel(description="Detail Users")
  clase User{
  
  @Past
  @ApiModelProperty(notes="Birth date should be in the past")
  Date birthDate
  
  @Size(50)
  @ApiModelProperty(notes="Name should have at least 50 characters")
  String name;
  

Spring Boot Actuator used for health checking, data rest Hal browser used to monitor spring rest
hal browser provide easily access to api by using browser without post man
configure in spring actuator in application.properties
management.endpoints.web.exposure.include=*

in hal broser can check for spring actuator heath, spring configuraton problem, spring bean. metric can check jvm memory usage

Static Filtering sentivive information
just add @jsonIgonre in field
or add @JsonIgnoreProperties(value={"filed1"}) in class level

Dynamic filtering sentitive information
SimplePropertyFilter filter= SimplePropertyFilter.filterOutAllExcept("field1", "field2");
FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
MappingJacksonValue mapping = new MappingJacksonValue(someBean);
mapping.setFilter(filters);


need to add in dto 
SomeBeanFilter
@JsonFilter("SomeBeanFilter")
class SomeBean{}


Url Versioning just use custom path like v1/getUserList v2/getUserList

Param versioning just use in Get or PostMapping user/param with params="version=1")

Header versioning just use with user/header headers="X-API-VERSION=1"

MIME type versioning
Produces versioning user/produces produces="application/vnd.company.app-v1+json"
in headers add Accept application/vnd.company.app-v1+json

To show validate msg in swagger wsdl Add @ApiModelProperty(notes="requre message")


048 Step 36 - Richardson Maturity Model when creating rest

Best Practices
Response status
200 for success
201 for created
404 resource not found
401 unauthorized
500 server error
400 bad request for validation


What is microservice? Not exact meaing
Rest, Small Well chosen Deployable unit and cloud enable

1 micro service can have many instance

Identify the boundary for each of microservices

Deciding right boundary of microservices

dynamic scale up and scale down like docker

Microservice Architecture
functionality is distributed among 10 services, there is a bug how do u identify where the bug is
To know that need to monitor the micro services to know which service casue the bug

5 Challenges in Microservices
Bounded Context
Right boundry 
Configuration Management
Dynamic scale up and down => to distribute load among the active instances.
Visibility => what is happening behind the scenes with each of these services. which services was cause for defect.
Pack of Cards => how to prevent one service being down taking down the entire application, how to build fault tolerance into micoservices.

Spring cloud provide common patterns in distributed system

Used in this project
Spring Cloud Finchely M2


Spring cloud config server provides an approach where you can store all config for all

Dynamic scale up and down => will use
Naming Server (Eureka) 
=> Service Registration, 
Service Discovery => Give me the instance of currency exchange service => this helps to establish bet currency cal service and instance
Ribbon(Client Side load balancing)
Feign(Easier Rest Client)

Visibility and Monitoring => will use
=> Zipkin Distributed Tracing
=> Netflix Zuel Api Gateway
=> will use spring cloud slot to assign id to request across multiple components

Fault Tolerance => will use
Hystrix 


Advantages of micorservices
combination of microservies can communicate with each other using simple messages each of these m-services can be built differnet technoliges(languages)
Typical monolith app would not have this flexibility, for eg=> m-1 java , m-2 notejs ,etc.
provides dynamic scaling 

Dynamic Scaling
in the season of black friday amazon will get huge load
if you micro services are cloud enable, they can scale dynamically
can scale up and down dynamically based on load

Will installed 7 projects 
https://github.com/in28minutes/spring-microservices/tree/master/03.microservices

centralizd m services config or app config
each of m services have their own config
managing config of alot of different instances of different micro services
that's where centralized config come to pictures

spring cloud say put all conig in git repository, will take care of managing config and providing it to specific m-sevices
currencyCalService, currentcyExchangeService, Limits Service => if each service want their config 
spring cloud config server will provide for each service, act as centralized m-service config

Establish connection between spring cloud config server and git repository
create new folder and git init
add git link in eclipse ==> add source folder
right click project > Build path > link source
add file called limits-service.properties ==> (should be same name that defined in limits-serveice project's artifactid) in source folder

Config for multiple environment in git repository
limits-service default
limits-service-dev.properties

what ever change to spring cloud config' git-local-config make sure to commit first then it is available

to access qa properties
http://localhost:8888/limits-service/qa
http://localhost:8888/limits-service/default
http://localhost:8888/limits-service/dev

Connect limit service to spring cloud config server
to pick up config from config server
to pick up need to rename the limit-service app.properties to any name(bootstrap.properties)
application name is very important in config
add properties
spring.application.name=limits-service
spring.cloud.config.uri=http://localhost:8888
in bootstrap.properties

1st start config server, then limits-service

Configuring profiles for limits-service
setup active profiles in limits-service
spring.profiles.active=dev

communicate service to service use the port to distinguish which service are connected to
To get 2 instance of currency exchage service running
externally set the server port
to do that set it up in run config > currency-exchange-service 8001
in arguments jvm => 
under VM arguments =>
-Dserver.port=8001 
it overide the application.properties




C:\Users\Htaung\Desktop\Temp Ebook\freetutorials eu\microservices-with-spring-boot-and-spring-cloud [FreeTutorials.Us]\04 Microservices with Spring Cloud
64
