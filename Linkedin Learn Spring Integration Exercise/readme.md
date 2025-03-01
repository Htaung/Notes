https://github.com/LinkedInLearning/spring-spring-integration-2848253

msg channel is component, transmitting message

END POINT
end point is made up of one or more component, manages msg delivery to meet design needs of applications
component or collection of components, manipulate msg and make it consumable by overall business process
act conceptual interface bet msg framework and core app functionality

msg, channel, endpoint, industry standard, EIP

msg based microservices distributed context

loosely couple, separation of concern, reusable and portable


Producer => business process with info(messages)
Consumer => to communicate or receive

Message => represent data, command or event

MessageHandler => lightweight interface that defines simple contract for handling a msg
Filter => msg should be passed based on some criteria (eg. selector, injected selector)
Transform => modify a msg that can meet expectation of consumer
Router => consume msg from a msg channel and fwd to one or more different msg channel, depending on set of conditions
Split/Aggregate => partition a msg into several parts or combine multiple msg into single msg

Service Activator Pattern => is both EIP and specific Spring integration implementation
connect msg on a channel to service being accessed

1. Adaptor Pattern => allows the interface of an existing class to be used as different interface
	One way, enables connecting a single consumer or producer to msg channel
	so in and out channel require 2 adapter configuration
	in bound adaptor add config to the send end of channel => Send end of channel 
	out bound adaptor add config to the receiving end of channel => Receive end of channel
2. Gateway Pattern => encapsulates access to an external system or resource, separating the messaging from application code
	inbound gateway configure server side of interaction => Server side of interaction
	outbound gateway configure client side => Client side of interaction
	
Spring int supported protocol => File,(S)FTP,Mail,HTTP,JDBC,JMS,MongoDB,Redis,RMI,WebFlux,WebSockets,Twitter

https://www.linkedin.com/learning/spring-spring-integration/jdbc-connectivity-using-spring-integration?autoSkip=true&contextUrn=urn%3Ali%3Ala_learningPlanV2%3AAEQAAAq3yO0BbxlQKzmskFg2aeRJuYcHm00mgHQ&resume=false&u=116771770


int-jdbc:outbound-channel-adapter
handle a msg, take a msg in , use that msg to execute a query, and response with result by sending it to reply channel

int-jdbc:inbound-channel-adapter
execute a select statement return the result as a message

04_03b => Twitter end point'


https://www.linkedin.com/learning/spring-spring-integration/integrate-twitter-using-spring-integration?autoSkip=true&contextUrn=urn%3Ali%3Ala_learningPlanV2%3AAEQAAAq3yO0BbxlQKzmskFg2aeRJuYcHm00mgHQ&resume=false&u=116771770

https://developer.x.com/en/portal/projects/1882428359688503296/apps/29995918/keys

there was drawback spring-int current version don't support twitter v2, it support only 1.1

int-http:outbound-gateway => send request and receive response
int-http:inbound-gateway => receive request and send response


Advance Topics
Protocol-Specific Endpoints
Annotation config
Error-handling => error aware and responsive, especially in asynchronous situations
Channel-Interceptors
Endpoint Roles
System Management => int:management or @EnableIntegrationManagement => to capture runtime integration metrics => send rates and error counts
Reactive Streams => Java 9 Reactive Streams SPI


EIP by Gregor Hohpe and Bobby Woolf
Spring Integration in Action by Mark Fisher and Other

