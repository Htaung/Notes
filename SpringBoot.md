C:\Users\Htaung\Desktop\Temp Ebook\spring-boot-tutorial-for-beginners\05 Spring Boot Deep Dive with a simple API

64

69

Useful to monitor the health of application
HAL browser, Spring Boot actuator

for adding dynamic configuration like message.propterties
just add in application.properties
welcome.message=Welcome

placeholder
${}

in controller just
@Autowiring
@Value(${"welcome.message"})
private String welcomeMsg;

to pass as jvm argument
just add
--welcome.message="Welcome"


just add in application.properties
app.name= in28Minutes
welcome.message=Welcome by ${app.name}


Adding spring profile
just by adding
application-prod.properties file and application-dev.properties file
declare prod or dev in application.properties file as 
spring.profiles.active=prod

to declare in jvm
-Dspring.profiles.active=prod

in spring config
just by declaring
@Profile("prod")


Adding config properties to use as spring bean
by adding 
@Component
@ConfigurationProperties("basic)
private String test

in application.properties add
basic.value ="test"

C:\Users\Htaung\Desktop\Temp Ebook\spring-boot-tutorial-for-beginners\05 Spring Boot Deep Dive with a simple API

64

69

73

75

80

88

91

I:\TEMP\06 Introduction to JPA With Spring Boot in 10 Steps
91


I:\TEMP\07 Connecting Web Application with JPA
99

I:\TEMP\09 Appendix  - First 10 Steps in Spring
109

Spring boot provide Web Jars instead of adding js file manullay just put the dependency of bootrap dependencies
Useful to monitor the health of application
HAL browser, Spring Boot actuator

for adding dynamic configuration like message.propterties
just add in application.properties
welcome.message=Welcome

placeholder
${}

in controller just
@Autowiring
@Value(${"welcome.message"})
private String welcomeMsg;

to pass as jvm argument
just add
--welcome.message="Welcome"


just add in application.properties
app.name= in28Minutes
welcome.message=Welcome by ${app.name}


Adding spring profile
just by adding
application-prod.properties file and application-dev.properties file
declare prod or dev in application.properties file as 
spring.profiles.active=prod

to declare in jvm
-Dspring.profiles.active=prod

in spring config
just by declaring
@Profile("prod")


Adding config properties to use as spring bean
by adding 
@Component
@ConfigurationProperties("basic)
private String test

in application.properties add
basic.value ="test"


Spring data JPA => allow interface to extends CRUD repository ==> can called common method of CRUD without implementing any class
CommandLineRunner => to initialized stuff at the start of application

Spring Data Rest => can do CRUD operation like rest service
HATEOAS => Hypermedia as Representation of Application State
just add in pom spring-data-repository
and add annotation @RepositoryRestResource(path="users", collectionResourceRel="users") in interface by extending 
PagingAndSortingRepository<User, Long>

then can query in POST => by calling /users/.. etc


Spring boot junit test by adding annotation @RunWith(SpringRunner.class), can create the randonPort in webEnvironment
Testing Rest Api ==> use TestRestTemplate
to test the response json => spring boot provide JSONAssert

Unit testing is concept called mocking
To do: R click => refactor => extract method

To test only one controller ==> need to have service layer ==> to do this add @WebMvcTest(xxxController.class)
to get service layer available in unit test just add @MockBean xxxService service;
by doing this will not load all the beans 

MockMvc
MvcResult

after adding spring security to pom.xml config works automatically and it start deny access without authenication
default user id is =>user
default password will show in console

To see the auto config log SET logging.level.org.springframework:DEBUG and check positive and negative matches
can also find in spring actuator browser in autoconfig

Deep dive into autoconfig
spring-boot-autoconfigure-1.4.0.RELEASE.jar ==> looked at sub package and spring.factories under META-INF

how about adding autoconfigure in normal maven setup project or dynamic web project


Mybatic same as spring jdbc template but it doesn't support like JPA

For housebutler app, we have Customer and Cleaner, both class should be used inheritance
Mistake as made on housebutler, should be mapped like this

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DescriminatorColumn(name = "User_Type")
public class Employee{

}
public class Cleaner extends Employee{
	private String phoneNo;
}

public class Customer extends Employee{
	private String email;
}

in mysql table should be 
Employee{
 Id,
 User_Type,
 phoneNo
 email
}

hibernate is ORM Framework, implementation of JPA
JPA is API

By using spring boot with h2 embedded database, can test hibernate easily

Setting up embedded database and JPA
Add dependency in pom
spring-data-jpa
h2

Add In application.properties
spring.jpa.show-sql=true
spring.h2.console.enabled=true

Access h2 console by localhost:8080/h2-console => user name => sa

Creating initial data => add data.sql under resources => spring boot will automatically called that sql

In spring boot web need to add this 3 dependency in pom.xml


 	<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
    </dependency>

    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
    </dependency>



View Resolver in application.properties
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp
