Ref from https://www.devglan.com/spring-mvc/spring-hibernate-integration-example-javaconfig
https://o7planning.org/en/10129/spring-mvc-tutorial-for-beginners
web.xml deployment descriptor
when creaing maven project using maven-archetype-webapp

Ref:
https://github.com/spring-projects/sts4/issues/82
Enabling Java assistant ==> window > pref > java > editor > advance > Enabling Java Proposals , Java Type Proposals
web.xml=> webapp deployment descriptor need to change from 2.3 to 3.0 inorder to worked for spring expression

page language need to change inorder to worked with spring expression
<%@page language="java" contentType="text/html;charset=UTF-8"%>

InternalResourceViewResolver ===>resolve the specifiled path of /WEB-INF/jsp/ as jsp file

ContentNegotiatingViewResolver  ==> resolve the controller return response stream as json or any

MultipartResolver ==> handling file upload limit 

Java Config
 implements WebApplicationInitializer
initialized dispatcher servlet contextLoader class like defined in web.xml

in sistic dispathcherServletConfig defined all beans resolveView


in pom.xml ==> servlet api 3.1 need to add if the server is tomcat
javax.servlet-api
for tag list ==> javax.servlet.jstl, taglibs standard, spring-security-taglibs
for spring web mvc ==< spring core, spring web mvc, spring web
for hibernate ==> hibernate core, hibernate entity manager, spring orm,  spring aop, spring context,spring-tx, spring aop, spring context support,spring bean
for json ==>jackson-core, jackson-databind, gson
commons-dbcp ==> enhanced database connection pool
Many Apache projects support interaction with a relational database. Creating a new connection for each user can be time consuming (often requiring multiple seconds of clock time), 
in order to perform a database transaction that might take milliseconds. Opening a connection per user can be unfeasible in a publicly-hosted Internet application where the number of 
simultaneous users can be very large. Accordingly, developers often wish to share a "pool" of open connections between all of the application's current users. The number of users actually 
performing a request at any given time is usually a very small percentage of the total number of active users, and during request processing is the only time that a database connection is required. 
The application itself logs into the DBMS, and handles any user account issues internally.
There are several Database Connection Pools already available, both within Apache products and elsewhere. This Commons package provides an opportunity to coordinate the efforts required
 to create and maintain an efficient, feature-rich package under the ASF license.
The commons-dbcp2 package relies on code in the commons-pool2 package to provide the underlying object pool mechanisms that it utilizes.


https://mvnrepository.com/artifact/javassist/javassist
Javassist
Javassist (JAVA programming ASSISTant) makes Java bytecode manipulation simple. It is a class library for editing bytecodes in Java.
