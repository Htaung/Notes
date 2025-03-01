https://docs.spring.io/spring-boot/docs/1.1.6.RELEASE/reference/html/common-application-properties.html

jms txn manager
rollback msg is not send

requeue of msg is broker dependent

5.1
listener container factory have setup txn manager
so don't need to add transactional in listener class

5.3
if msg are not read in couple of seconds it will auto disappear off the queue
if ms fall in DLQ, want to be pesisted, set deliveryPersistent in jms template
setSessionTransacted => to use txn manager

5.4
msg headers are not propagated to ware house queue using @SendTo
To solve will use message builder

5.5
using JMSResponse can compute destination response at run time
dynamically send msg to diff queue