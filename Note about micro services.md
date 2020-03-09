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
