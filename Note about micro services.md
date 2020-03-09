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
