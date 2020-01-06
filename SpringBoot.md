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
