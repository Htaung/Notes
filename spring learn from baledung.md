https://www.baeldung.com/spring-web-contexts

https://o7planning.org/en/10129/spring-mvc-tutorial-for-beginners

https://www.pegaxchange.com/2018/01/24/java-web-project-with-spring-framework/

2. The Root Web Application Context
Every Spring webapp has an associated application context that is tied to its lifecycle: the root web application context.

The context is started when the application starts, and it’s destroyed when it stops, thanks to a servlet context listener. The most common types of contexts can also be refreshed at runtime, although not all ApplicationContext implementations have this capability.

The context in a web application is always an instance of WebApplicationContext. That’s an interface extending ApplicationContext with a contract for accessing the ServletContext.

Anyway, applications usually should not be concerned about those implementation details: the root web application context is simply a centralized place to define shared beans.

2.1. The ContextLoaderListener
The root web application context described in the previous section is managed by a listener of class org.springframework.web.context.ContextLoaderListener, which is part of the spring-web module.

By default, the listener will load an XML application context from /WEB-INF/applicationContext.xml. However, those defaults can be changed. We can use Java annotations instead of XML, for example.

We can configure this listener either in the webapp descriptor (web.xml file) or programmatically in Servlet 3.x environments.

2.2. Using web.xml and an XML Application Context
When using web.xml, we configure the listener as usual:


<listener>
    <listener-class>
        org.springframework.web.context.ContextLoaderListener
    </listener-class>
</listener>
We can specify an alternate location of the XML context configuration with the contextConfigLocation parameter:


<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/rootApplicationContext.xml</param-value>
</context-param>


2.3. Using web.xml and a Java Application Context

<context-param>
    <param-name>contextClass</param-name>
    <param-value>
        org.springframework.web.context.support.AnnotationConfigWebApplicationContext
    </param-value>
</context-param>

Every type of context may have a default configuration location. In our case, the AnnotationConfigWebApplicationContext does not have one,
 so we have to provide it.
We can thus list one or more annotated classes:
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
        com.baeldung.contexts.config.RootApplicationConfig,
        com.baeldung.contexts.config.NormalWebAppConfig
    </param-value>
</context-param>


in sistic appContext is defined as SisticAppConfig in web.xml 
initialized in SpringServiceManager
