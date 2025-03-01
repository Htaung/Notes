App executing mulitple tasks - concurrency - overlapped time period
to execute multiple tasks or processes simultaneously. It enables efficient utilization of CPU resources by executing independent or 
interdependent tasks concurrently rather than sequentially. Java provides built-in support for concurrency through multithreading and 
parallel execution mechanisms

Parallelism => running task exactly at the same time

asynchronous => calls take a callback reference

Concurrency in java=> multi-threading

Switching bet process (IPC) - Inter Process Communication
Creating new thread require fewer resources

Every thread will have its own thread id, own set of exception handlers, priority scheduling, use register, stack to store local procedure call or
local variable and a set of structures that the system uses in order to save the context of a thread until it is picked up for execution

Scheduling and thread priority
order of threads executing in app
JVM used fixed priority, JVM Scheduling is also preemptive(prevent it from happening by doing something which makes it unnecessary or impossible)

let's say two thread have same priority => JVM going to arbitarily(on the basis of random choice or personal whim, rather than any reason or system.) 
pickup one of them for execution

existing 3 thread, suddently thread 4 come in with highest priority
JVM will suspend current thread execution and pickup thread 4 and run
JVM employ time slicing(a technique that divides CPU time into short intervals, or time slices, to allow multiple processes to run simultaneously)
 scheduling as per os, means every thread is given an equal time slice for execution
once thread excution elapses(the amount of time that passes between the beginning and end of an event), jvm going to save the context of thread
and pickup next thread

Priorities can be allocated by java's multi-threading API
JVM employ time slicing scheduling, it can ignore higher priority thread and pickup lower priority one instead inorder to avoid
starvation(a situation where a computer process is continuously denied access to necessary resources, 
preventing it from completing its task due to other processes being prioritized)
nothing is gurantee, have to be careful
Never assume platform use time slicing scheduling

whenever a thread executed, it always transits through many state during its entire life cycle

multi-threading have  BASIC API and high-level api

BASIC API => need SOLID PARALLELISM LOGIC
Thread and Runnable => sufficient for low level and basic task
eg. when the jobs to be executed, if there is an error in execution, if interruption in execution
how to make sure threads are completly terminated when app shut down
as there was complexity of system, no. of user grows 

large scale app, thread creation and mgmt become tedious using BASIC API
Additional requirement for managing thread scheduling 
schedule job for certain regular interval, may be with definite delay, will have to create your own logic
too difficult to implement
TO OVERCOME LIMITATION => USE HIGH LEVEL API

HIGH Level API
Executor Framework
INVOCATION, Scheduling, execution, control of asynchronous tasks in pralllel thread
composed of many classes and interfaces that provide creation and mgmt of threads
- Decouple thread creation and mgmt from app business logic
- smooth coordination bet thread submitting the task and thread excuting the job
- provide clean shutdown of all tasks
- help check the output of completed tasks
- automatically create thread pool helps you spawn thread(A "pool" contains a list of available "threads" ready to be used
 whereas "spawning" refers to actually creating a new thread. The usefulness of "Thread Pooling" lies in "lower time-to-use")


Thread Pools
-to generate a txn report of every bank account
- millions of bank account => one thread per bank account => will exceed the maximum no. of threads, sys will crash down
if you are going to spawn one thread per client => got excessive processing overhead, alloc and de-alloc memory resources, costly business for thread
so will have a thred pools => logical pools with fixed no. of threads
once the job is terminated, in its place new thread will be created,
app always have fixed no. of thread, reusability is important

Creation of thread pool is better way to scale concurrent app performance
- Reduce per task invocation overhead
- Manage resource efficiently
- often used with connection pooling, every thread maintain connection to db, creaing too many db object as well
- degrade app gracefuly 
eg. every incoming request, always manage by a thread, may have millions of incoming requests, cannot possibly spawn a new thread
when you create a thread pool, will have a limited no. of threads
even request coming in at faster pace, threads are going to make sure reqs are handled according to substainability of the system
executor frame will do it for you 
thread pool sized based on factor => cpu core
tasks are io intensive tasks



Thread is created => state of thread is NEW
when you start thread => state => Runnable => ready to run
jvm execute as per scheduling => execute run => Running State
job completed => TERMINATED STATE/ DEAD

Thread is sleep(BLOCK STATE), waitng for resource (WAITING STATE)=> 


Executor Framework
Executor
ExecutorService => manage life cycle of all individual threads also of executor, excute a collection of runnable/callable objects,
help completely shutdown executor service -> clean shutdown when app ends
to capture the output use submit
SheduledExecutorService
have both runnable and callable

Callable - call() => return generic value <V>, can throw checked exception, tropolcate them down the calling hierachy
Return value from Callable is captured in Future Object

Runnable doesn't throw any kind of exception


use case
process user info 
read info from file, process them, and insert a record of each user in db

newCachedThreadPool
newWorkStealingPool => fork joint implementation

future.get is blocking operation, main method going to block until it get the future object

ExecutorService 
invokeAny => submit task and return output of one successful/exception execution, 
among collection at least one have to be called successfully, To assure tasks have invoke successfully for once
invokeAll => submits all tasks and return their output

shutdown => no news tasks accepted coz of shutdown initiated,not gurantee previous tasks are complete or not, drawback
if you know all the tasks reach completion point, it is good to destroy service object via shutdown

shutdownNow => hold execution of waiting tasks completely and stop the execution of current tasks, return list of waiting tasks (immediate shutdwon)

awaitTermination=> check after initialized shutdown request, true => if service got terminated correctly, false if time elapses
completion check can be check with awaitTermination, have timeout for waiting previous submitted task


SheduledExecutorService
schedule certain job => run job after certain delay 
scheduleWithFixedDelay and scheduleAtFixedRate
housekeeping job for the application

scheduleAtFixedRate
ses.scheduleAtFixedRate(new CleaningScheduler(), 5, 4, TimeUnit.SECONDS);
intialdelay, occur every second(period)

scheduleWithFixedDelay
ses.scheduleWithFixedDelay(new CleaningScheduler(), 5, 4, TimeUnit.SECONDS);
if schedular(runnable) take 2 seconds to execute => 5+2 => 7 seconds for first schedular, then going to wait 4 seconds
time required to complete 
1st task => after initialDelay 5 seconds+ execution time 2s
2nd task => Waiting for 4 seconds delay + execution time 2s


ThreadFactory API
if don't specify any kind of thread factory reference
default => same thread group, same non-priority, number 5, non-demand status

Custom ThreadFactory
To decide the name of 

java ee Concurrency
jave ee app works within an underlying container or app server
container provide runtime support for app component like EJB and server
provide layer bet app component code and platform serivces and resources
provide central resource(jdbc, txn, connection pooling) mgmt
managed env setup by container
in such env, app integrity is important

thread running a job in ee env, the container is going to expect that thread get all container's supplied objects
and resources to run asynchronous tasks,
thread is required to access Standard EE Services like JMS, EJB,etc, it is required contexual info of container
is provided to thread
contexual info => java naming standard, dir interface naming, class loader info, security context
very important that container propagates this info to all the threads executing job for you

if we create our own thread using java ee platform, then container will not be aware or don't have any knowledge of
these thread resources, it will be problem coz not able to provide the contexual info needed to access java ee services

if you have EJB or servlet, they are not able to reliably use standard ee services from a thread, which is not managed by container
wiser to have those thread managed instead of creating them ourselves,
that's why java ee concurency born

JSR 236, enables java ee app to sumit asynchronous tasks
defined and use centralized manageable objects inside java ee server
java enterprise concurency api extends uti.concurrent api, resources of container, everthing is going to managed by container
concurrent api will help you manage and monitor the lifecycle of all asynchronous operation
if you have all the threads and all resources managed by container, then thread which are executing the task will be 
provided with correct contexual info of container

java ee Concurrency api
ManagedExecutorService, ManagedScheduleExecutorService, ManagedThreadFactory
ContextService utilized to create contexual proxies in EE App

As an applicaion what are your responsbilities
find out ways to use env reference of these managed resources
to use ManagedExecutorService to submit particular task, need to understand how do i take a ref of resource in ee app or servlet or restful api
declare them in you deployment descriptor or
look it up in container/env through JNDI or inject using resource annotation

define task same as callable/runnable
task implements ManagedTask and even register ManagedTaskListener to receive life cycle notifications
ManagedTask interface is used to provide identifying info about task or provide additional execution properties

let's say
to provide additional execution property like hint about whether task is going to take a longer time to complete
may be want to have another execution property which specifies under which txn should this current task to be executed in?
so any kind of additional execution properties could be set via ManagedTask Interface	
ManageTaskListener is used to received lifecycle notifications about track the state of your task's future 
so whenever state change you can configure this listener as the part of processing




Manage Resource of the server
deployment descriptor entry: web.xml

JNDI lookup or resource annotation
API in java to access directory service - by name, by jndi name => lookup data and resources, done through Initial Context API

Stateless Session Bean => used to perform independent operation

app server create pool of session beans

Global Txn => XA Resource for resource manager => XA =>Extended Architecture => 
executing a global txn that access more than one backend data store,
define interaction be txn manager and resource manager,
the goal of XA is to allow multiple resource like db, JMS, transactional queue,... to be accessed within the same transaction

Java Transaction API
resource manager, app server, app core
JPA specify bet txn manager and all ot these parties


every bank account, one instance of report processor that extends callbale<boolean>

 Callable implementation => define jobs


 
 two ways of injection
 1. JNDI lookup
 InitialContext.lookup
 
 2. Resource annotation to lookup
 
 if using java ee manage container manageExecutorservice, won't need to shutdown those service, 
 
 JSE ThreadFactory
 
 
 setting up new ThreadPoolExecutor => take whole container level 
 keepAliveTime => if keepAliveTime is over excessive threads in the pool will be terminated
 timUnit => specifying for keepAliveTime
 if pool have more than your core pool size threads, then excessive threads in that pool will be terminated if they have idle for more than
 this keepAliveTime
 
 Blocking Queue => used to hold submitted tasks
 if you have threads thar are fewer than your core pool size threads, executor prefer adding new thread rather than queuing
 if core pool size more threads are running then executor prefer queuing a request rather than adding new thread
 how many no. of task blocking queue hold at a time
 
 
 Context service api
 contexual object => java obj which have app component's container context associated with it 
 
 Contexual task => task submitted to manage resource
 allow app to create contexual objs without managed executors
 
 if create thread using JSE platform inside enterprise environment, contexual info is not gurantee to be given thread
 coz container is not aware of thread that created with JSE platform
 if you want to capture additional info of the container context, use Context Service API
 Context service api => give a way to capture context info, store it, can run it 
 Context service api => dynamic proxies capabilites that are provided under relection api to associate the app component's context
 with obj instance, becomes contexual object 
 
 Context service help you create contexual objs that will have context info (JNDI naming, classloader info and security context)
 
  Context service api
  inject java:comp/DefaultContextService(Enterprise server glassfish, jboss, ) by Resource annotation
  
  
  // not sure whether it can get from security context info 
  Thread t = new Thread(runnable);
  t.start();
  
  //to overcome
  contextService.createContexualProxy // proxy will retain security context info 
 
 
 stop at here
 
 https://www.linkedin.com/learning/java-ee-concurrency-and-multithreading/java-transaction-api-jta-transactions-with-concurrency?autoSkip=true&contextUrn=urn%3Ali%3Ala_learningPlanV2%3AAEQAAAq3yO0BbxlQKzmskFg2aeRJuYcHm00mgHQ&resume=false&u=116771770

 JTA
 allows apps to perform distributed txns
 1 datasource => 1 db
 2 datasource => 1 db
 2 datasource => 2 db
 
 if there is distributed network, there will be global txn, due to threads, jms queues, app servers, etc.
 commit and roll back work successfully 
 
 javax.transaction.UserTransaction allows developer to manage txn boundaries
 
 Concurrency Utilities rely on JTA to perform and support txn, work with XA resource
 Resource Manager(driver) should be XA Compatible
 
 userTransaction.begin();
 userTransaction.commit();
 userTransaction.rollback();