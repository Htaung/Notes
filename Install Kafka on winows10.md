https://www.goavega.com/install-apache-kafka-on-windows/

In case of getting jvm paging error

https://stackoverflow.com/questions/21448907/kafka-8-and-memory-there-is-insufficient-memory-for-the-java-runtime-environme


Set this in environment variable


KAFKA_HEAP_OPTS: "-Xmx512M -Xms256M"


The "Complete Guide to Apache Kafka for Beginners" is part of a series o
f courses on Kafka. While this course focuses on the fundamentals, there are additional courses on LinkedIn Learning that cover more advanced topics lik
e Kafka Connect, Kafka Streams, ksqlDB, Confluent Components, Kafka Security, Kafka Monitoring, and Kafka Cluster Setup and Administration. You
 can explore LinkedIn Learning to find these courses and continue your Kafka learning journey.



https://www.confluent.io/certification/


What critical challenge does Apache Kafka address in data integration?

streamlining protocol variations

Kafka clusters
logs, purchases, twitter_tweets, trucks_gps


Kafka topics
topics, are split in partition(eg. 100 partitions), are immutable 
msgs within partition, are ordered, get an incremental id, called offset
- particular stream of data 
- like db 

Topics, partitions and offsets 
Once the data is written to a partition, cannot be changed(immutablity)
Data is kept only for a limited time ( default is 1 week - configurable)
Offset only have a meaning for specific partition
eg. offset 3 in partition 0 not represent same data as offset 3 in partition 1
offsets are not reused even if previous msg have been deleted
Order is guranteed only within a partition(not across partition)
Data is assigned ramdomly to a partition unless a key is provided(more on this later)


## Producer
- write data to topics which are made of partition
- know to which partition to write to (and which broker has it)
- in case of kafka broker failures, Producer will automatically recover

### Producers: Message Keys
- can choose to send a key with msg (string, binary, number,...)
- if key = null, data is sent round robin(partition0, 1 ,2,...)
- if key !=null, all msg for key will go to same partition(hashing)

## Kafka msg anatomy
Key (Binary), Value (Binary), Compession Type(none, gzip, snappy, lz4, zstd), Header, Partition + offset, Timstamp

### Kafka msg serializer
Kafka only accept bytes as input from producer
send bytes out as output to consumer
Message serilization means transforming object/ data to bytes
they are used on value and key

Key 123 => Keyserilizer - IntegerSerializer => Key - binary 01100
Value "Hello" => Keyserilizer - StringSerializer => Value - binary 01100


## Kafka msg key hashing
- kafka partitioner is code logic that take a record and detemines which partition to send into
Record => send => Producer Partitioner logic => assign partition 1 
- is the process of detemining the mapping of a key to partition
- default kafka partitioner, keys are hashed using murmur2 algorithm, as belows
targetPartiton = Math.abs(Utils.murmur2(keyBytes)) % numPartition -1

## Consumer
- read data from topic - pull model
- automatically know which broker to read from
- in case of broker failures, consumer know how to recover
- data is read in order from low to high offset within each partitions


### Consumer deserializer
- deserialize how to transform byes to object/data
- used on value and key of msg
- serilization/ deserialization should not be change during topic lifecycle ( create a new topic instead)

### Consumer group
- All the consumers in an app read data as a consumer groups
- each consumer within a group reads from exclusive partitions

### Consumer groups - what if too many consumers?
if you have more consumers than partitions, some consumers will be inactive

### Multiple consumers on one topic
- is acceptable to have multiple consumer groups on same topic

### Consumer offset
- kafka stores the offset at which a consumer
- Kafka stores the offsets at which a consumer group has been reading in a Kafka topic named __consumer_offsets.
- Commit Offsets: Consumers commit offsets to Kafka brokers to indicate how far they have read into a Kafka topic. 
- This helps in resuming reading from the last committed offset in case of a failure.

- Delivery Semantics: 
- by default, java consumers will automatically commit offsets(at least once)
There are three delivery semantics based on how and when offsets are committed:
- At least once: Offsets are committed after processing messages, which may lead to duplicate processing if a failure occurs.
  - make sure processing is idepempotent (ie. processing again the msg won't impact your systems)
- At most once: 
 - Offsets are committed as soon as messages are received, which may lead to message loss if processing fails.
- Exactly once: 
 - Ensures messages are processed just once, 
 - kafka throught kafka workflow, read from topic and write back to topic, use transactional API, kafka strem API
 - kafka to external system use Idempotent consumer
 - typically used with Kafka's transactional API.


## Kafka broker
- A kafka cluster is composed of multiple brokers(server)
- received and send data 
- identifies with ID (integer)
- each broker contains certain topic partitions
- After connecting to any broker (called bootstrap broker), clients or producer or consumer will be connected to entire cluster
- A good number to get started is 3 brokers, but some big clusters have over 100 brokers


## Broker and topics
- Topic A with 3 partitions, Topic B with 2 partitions

Broker1 =>  TopicA - Partition 0, TopicB - Partition 0  
Broker2 =>  TopicA - Partition 1, TopicB - Partition 1
Broker3 =>  TopicA - Partition 2

- data is distributed and broker 3 doesn't have TopicB data
- data in partition going to be distributed acrosss all brokers, called horizontal scaling
- the more partitions, brokers we add, the more data is spread out across our entire cluster
- broker don't have all data, have the data they should have

## Broker discovery
- every kafka broker is also called bootstrap server
- only need to connect to one broker, kafka client will know how to be connected to enrire cluster(smart client)
- kafka client -> initiate connection , metatdata request -> Broker1 -> return list of all broker
  kafka client -> can connect to the needed broker (produce or consumer data)
  
  https://www.linkedin.com/learning/complete-guide-to-apache-kafka-for-beginners/topic-replication?autoSkip=true&resume=false&u=116771770


## Topic replication factor
should have factor >1  (between 2 and 3)
if a broker is down, another broker can serve the data that wil become leder of partition

# Leader of partition
- anytime only one broker can be leader for given partition ( like master slave)
- producer can only send data to the broker that is leader of a partition

each replica called ISR(in-sync replica)

default producer and consumer behavior with leaders
- producer can only write to leader broker for partition,not to the replica (ISR)
- consumer read by default only from leader broker of partition, not get from replica(ISR)


## Kafka consumer replica fetching(v 2.4+)
- allows consumer to read from closest replicas
- consumer will read from replicas(ISR) -> improve latency , that was closest replicas, decrease network costs


## Producer acknoledgements
- can choose to receive ack of data writes:
acks=0 => won't wait for ack ( possbile data loss)
acks=1 => wait for leader's ack (limited data loss)
acks=2 => leader + replica ack (no data loss)

## Kafka topic durability
- topic replica factor of 3, topica data durability can withstand 2 broker loss
- replica factor of N, can withstand N-1 broker


## Zookeeper 
- manages brokers(keep a list of them)
- help in performing leader election for partition
- sends notification to kafka in case of changes
(eg. new topic, broker dies, broker come up, delete topics, etc)
kafka 3.x can work without zookeeper (KIP 500) - using Kafka Raft instead, Kraft
Kafka 4.x will not have zookeeper
- by design, operates with odd number of server( 1,3,5,7)
- has a leader (writes) the rest of the servers are follwer(reads)
- does not store consumer offsets with kafka > v0.10


## Kafka Kraft 
- With Quorum Controller, have only kafka broker, one of them is quorum leader

- Kafka cluster can be made up of multiple brokers
- topics
- partition
- replication
- partition leader and in-sync-replica(ISR)
- offset topics
- producer take from source system and then send the data into Kafka via round-robin, key-based ordering, acks strategy
- Consumer
 - consumer offset
 - consumer group
 - at least once
 - at most once
 - exactly once






The concept of sequential streams within Kafka
Ordered partitions: Each partition within a topic is an ordered, immutable, append-only sequence of messages. Producers append new messages to the end of a partition log, and consumers read them in the same order they were written.
Offsets for tracking: Every message within a partition is identified by a sequential ID number called an offset. This offset is used by consumers to track their position in the stream and enables them to reread old messages or replay events if needed.
A pull-based approach: Consumers "pull" messages from a topic at their own pace, rather than having them pushed. This allows different consumer groups to process messages from the same topic independently and at different speeds


Decoupled producer-consumer model
The topic acts as a logical channel that fully decouples data producers and consumers. 
Multiple producers and consumers: A single topic can have many producers writing events and multiple consumer groups reading them.
Consumer independence: Each consumer group independently tracks its own offset. This means one consumer group can be reading old messages while another is processing the latest data.
Pull-based system: Consumers pull messages from a topic at their own pace, rather than having messages pushed to them. 


A Kafka ZooKeeper, Fasani, is diving into integration. What is Fasani's main role?
orchestrating Kafka broker management and metadata



curl -L https://releases.conduktor.io/quick-start -o docker-compose.yml && docker compose up -d --wait && echo "Conduktor started on http://localhost:7080"





http://localhost:7080/console/cdk-gateway/brokers


https://conduktor.io/get-started



kafka-server-start.bat D:kafkaconfigserver.properties

kafka-storage.bat random-uuid | % {kafka-storage.bat format -t $_ -c D:kafkaconfigserver.properties }


kafka-storage.bat random-uuid

 
kafka-storage.bat format -t ib9wx6U2S_uK7f-PZ_E2_w -c D:kafkaconfigserver.properties


kafka-server-start.bat D:kafkaconfigserver.properties


## Run with power shell
Remove-Item -Recurse -Force "D:\kafka\data\kafka"
mkdir D:\kafka\data\kafka

## Generate cluster ID	
 kafka-storage.bat random-uuid
## Format storage	
kafka-storage.bat format -t ZshuTiIDSjSko_xY0qSu5Q -c D:\kafka\config\server.properties

## Start Kafka  
kafka-server-start.bat D:\kafka\config\server.properties

## Fix Log4j	
setx KAFKA_LOG4J_OPTS "-Dlog4j.configurationFile=D:kafkaconfiglog4j2.yaml"
setx KAFKA_LOG4J_OPTS '-Dlog4j.configurationFile=D:kafkaconfiglog4j2.yaml'
echo $env:KAFKA_LOG4J_OPTS

## List topics
- ``` kafka-topics.bat --list --bootstrap-server localhost:9092 ```

## Create topics
kafka-topics.bat --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1 

kafka-topics.bat --create --topic third-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 

#producing
kafka-console-producer.bat --bootstrap-server localhost:9092 --topic first_topic 

# producing with properties
kafka-console-producer.bat --bootstrap-server localhost:9092 --topic first_topic --producer-property acks=all

# producing to a non existing topic, get diff behavior 
kafka-console-producer.bat --bootstrap-server localhost:9092 --topic new_topic


# our new topic only has 1 partition
kafka-topics.bat --bootstrap-server localhost:9092 --list
kafka-topics.bat --bootstrap-server localhost:9092 --topic new_topic --describe


kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic new_topic --from-beginning
kafka-console-producer.bat --broker-list localhost:9092 --topic new_topic


https://github.com/conduktor/conduktor-platform

redpanda-0:9092

###  docker compose path that was using conduktor
D:LearningLinkedin Learn Ex Folderdocker-compose.yml


## To run in conduktor that was using docker 
## https://conduktor.io/get-started
```
curl -L https://releases.conduktor.io/quick-start -o docker-compose.yml && docker compose up -d --wait && echo "Conduktor started on http://localhost:7080"

docker-compose.yml && docker compose up -d --wait && echo "Conduktor started on http://localhost:7080"

docker exec -it redpanda-0 bash
rpk topic list
```


``` 
kafka-topics.bat --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3 --replication-factor 1

```

# Describe a topic
``` 
	kafka-topics.bat --bootstrap-server localhost:9092 --topic first_topic --describe
	
	## failed cannot create replication-factor higher than the broker
	kafka-topics.bat --bootstrap-server localhost:9092 --topic first_topic --create --partitions 3 --replication-factor 2
	
	```
	
# Delete a topic # in windows cannot delete
kafka-topics.bat --bootstrap-server localhost:9092 --topic first_topic --delete
# (only works if delete.topic.enable=true)


# produce with keys
kafka-console-producer --bootstrap-server localhost:9092 --topic first_topic --property parse.key=true --property key.separator=:


#produce one partition at a time, do not used in prod, just for demostration, if you have 3 partition, msg will be keep on each partition,
# at the time when consume, msg will not be order coz of of producing one partition at a time
kafka-console-producer --bootstrap-server localhost:9092 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner --topic second_topic


# consuming, stop and run again
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic second_topic

kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic second_topic --from-beginning



# display key, values and timestamp in consumer
kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --from-beginning
kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --formatter 'kafka.tools.DefaultMessageFormatter' --property 'print.timestamp=true' --property 'print.key=true' --property 'print.value=true' --from-beginning

#working
kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --formatter org.apache.kafka.tools.consumer.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --from-beginning

kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic first_topic --formatter org.apache.kafka.tools.consumer.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true --property print.offset=true --from-beginning


# Consumer in group
https://www.linkedin.com/learning/complete-guide-to-apache-kafka-for-beginners/kafka-consumers-in-groups?autoSkip=true&resume=false&u=116771770


# Create topic with 3 Partition
 kafka-topics.bat --create --topic third-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# start a console consumer with consumer group
msg will show in each partion assigned, msg spread across all consumer
if creaton with 3 partion, but create 4 consumer, one consumer stop getting msg
kafka-console-consumer --bootstrap-server localhost:9092 --topic third-topic --group my-first-application
kafka-console-consumer --bootstrap-server localhost:9092 --topic third-topic --group my-first-application
kafka-console-consumer --bootstrap-server localhost:9092 --topic third-topic --group my-first-application


kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic third-topic --group my-first-application --from-beginning

# does't read all msg from beginning
# --from-beginning only applies to the first time a consumer group reads a topic.
# After that, Kafka remembers the group’s offset
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic third-topic --group my-first-application --from-beginning

#produce one partition at a time, do not used in prod, just for demostration, if you have 3 partition, msg will be keep on each partition,
# at the time when consume, msg will not be order coz of of producing one partition at a time
kafka-console-producer --bootstrap-server localhost:9092 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner --topic third-topic


#Check topic list
kafka-topics --bootstrap-server localhost:9092 --list


# describe the group again
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-first-application


#Consumer group management

# list consumer groups
kafka-consumer-groups --bootstrap-server localhost:9092 --list
 
# describe one specific group
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-second-application

# describe another group
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-first-application

# start a consumer
kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --group my-first-application

# describe the group now
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-first-application

# describe a console consumer group (change the end number)
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group console-consumer-10592

# start a console consumer
kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --group my-first-application

# describe the group again
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group my-first-application


# reset the offsets to the beginning of each partition
kafka-consumer-groups --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --topic third-topic


# execute flag is needed
kafka-consumer-groups --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --topic third-topic --execute

# consume from where the offsets have been reset
kafka-console-consumer --bootstrap-server localhost:9092 --topic third_topic --group my-first-application


#create topic and then send the msg from producer using java
kafka-topics.bat --create --topic demo_java --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 

#consume msg 
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic demo_java --from-beginning


#Kafka Producer Java API Call back
- confirm partition and offset the message was sent using Callbacks
- StickyPartitioner


# Kafka Producer : Java API with keys
- Send Non-null keys to Kafka Topics
- Same key = same partition, 

# Kafka Consumer
- Consumer can received up to 1 mb of data at a time from broker
- re-joining the group, rejoining previous offset 


# Kafka Consumer - graceful shutdown


#Java API consumer groups
- observe parition rebalance mechanisms
- running two consumer - 
  - Request joining group due to: group is already rebalancing
  - Adding newly assigned partitions: [demo_java-0, demo_java-1] => Adding newly assigned partitions: [demo_java-2]
  - 3 consumer running => partition rebalance
  - 1 conumer shutdown => partition rebalance
  
# Consumer group and partition rebalance strategy
## Eager rebalance => default behavior
- All consumer stop, give up their parition
- rejoin consumer group and get new parition assignment
- during a short period of time, entire consumer group stop processings (stop the world event)
- no gurantee consumer don't get back the same parition as they are used to
  - can't get consumer to get the same parition
  - can't consume when the consumer stop 
  
## To solve Eager rebalance Cooperative Rebalance (Incremental rebalance)
- Reassigning a small subset of paritions from one consumer to another
- consumer don't have reassigned partitions, can still processed the data uninterrupted, can go several iterations to find stable assignment, called incremental
- avoid stop the world event where all consumer stop processing data

### Eager rebalance => how to use
- partition.assignment.strategy 
 - RangeAssignor - assign paritions on a per-topic basis ( can lead to imbalance) => default 
 - RoundRobin - assign paritions across all topics in round-robin fashion, optimal balance
 - StickyAssignor - like round-robin and minimize parition movements when consumer join/leave group in order to minimize movements
### Cooperative rebalance => how to use 
- partition.assignment.strategy 
 - CooperativeStickyAssignor => like StickyAssignor but support Cooperative rebalance and therefore consumer can keep on consuming from Topics
 
 
 Kafka connect default => RangeAssignor
 Kafka Streams default => StreamsPartitionAssignor
 
### Static Group Memebership
- default, when consumer leaves a group its partitions are revoke and re-assigned
- if it joins back,it will have new Memeber id and new parition assigned
- if you specify group.instance.id it makes the consumer a static member
- upon leaving, consumer has up session.timeout.ms to join back and get back its parition(else they will reassigned) without trigger a rebalance
- helpful when consumer maintain in local state and cache(to avoid rebuilding cache)

- default first use RangeAssignor
- partition.assignment.strategy = [class org.apache.kafka.clients.consumer.RangeAssignor, class org.apache.kafka.clients.consumer.CooperativeStickyAssignor]

to get when consumer leave don't change assignment

## Auto offset commit behavior
- in java consumer api, offsets are regularly commited
- this enable at least once reading scenario by default(under conditions)
- Offsets are commited when you call .poll and auto.commit.interval.ms has elapsed
- examples => auto.commit.interval.ms=5000 and enable.auto.commit=true will commit every five seconds

Make sure all msg are successfully processed before you call .poll again
- in rare case, disable auto.commit 
if you are on processing a separate thread, then from time to time call .commitSync() or .commitAsync() with correct offset manually (Advance)

- auto.commit.interval.ms=5000 and enable.auto.commit=true 
.poll() => start timer
.poll() => 3 sec later
.poll() => 3 sec later (6s total)
behind the scene => .commitAsync() more than five seconds have elapsed, offset are auto commit and then timer start again


## to learn advance https://docs.conduktor.io/learn/programming/advanced-kafka-consumer-java



## Ref link 
https://stream.wikimedia.org/v2/stream/recentchange

https://codepen.io/Krinkle/pen/BwEKgW?editor=1010

https://esjewett.github.io/wm-eventsource-demo/

D:Projectskafka-beginner


kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic wikimedia.recentchange --from-beginning



kafka-topics.bat --create --topic wikimedia.recentchange --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 


## Producer Acknowledgement
producer send data into cluster
prodcer send data into broker, can choose to receive ack of data rights

### acks=0 => won't wait for ack (possible data loss)
- producer consider msg as "written successfully", msg was sent without waiting for broker to accept it at all
- producer send data to leader => broker (leader) => writes
  - broker goes offline or exception occurs, will loss data
  - useful that is ok for data loss such as metric collection
  - produces highest throghput setting coz the network overhead is minimized
  

### acks=1 => wait for leader ack ( limited data loss)
- producer consider msg as "written successfully", when msg as ack only by leader
- default (kafka version 1.0 to 208)
- producer send data to leader => broker (leader) => ack to producer => leader write
 - leader response is requesed, but replication(background process) is not gurantee as it happens in background
 - if broker leader goes offline unexpectedly but replicas haven't replicated the data yet, data loss
 - if ack is not received, producer may retry the request
 - no gurantee data is successfully replicated

### acks=all (acks=-1) => leader+replicas ack (no data loss)
- producer consider msg as "written successfully", when msg is accepted by All-in-sync replicas (ISR)
- default (kafka v3.0+)
 - producer send data to leader => broker (leader) => send to replicas1 and 2 =>  ack of writes => leader ack to producer

### acks=all (acks=-1) && min.insync.replicas
- leader replica for partition checks to see if there are enough in-sync replicas for safe writing msg (controlled by broker setting min.insync.replicas)
- min.insync.replicas=1 => only one broker leader need to successfully ack
- min.insync.replicas=2 => at least broker leader and one replica need to successfully ack
- if ack failed prouducer have to wait for replicas to come back up b4 sending data

### Kafka Topic Availabity
- RF=3 
 - acks=0 and acks=1 if one parition is up considers an ISR, topic available for write
 - acks=all and min.insync.replicas=1 (default) => topic must have at least 1 parition up as an ISR(that include reader), can 
 - acks=all and min.insync.replicas=2 => topic must have at least 2 ISR up, can tolerate one broker down(in case replica factor 3)
 gurantee for every write, data will be at least written twice
 - acks=all and min.insync.replicas=3 => not make sense for replica factor 3, can't tolerate any broker being down
  - min.insync.replicas should be replica factor - 1
  
  
## Producer retries
in case of transient failures, developer are expected to handle exception, otherwise data will be loss
eg. of transient failure:
- NOT_ENOUGH_REPLICAS ( due to min.insync.replicas setting)
retries setting
0 default kafka <=2.0
2147483647 (max of int) default kafka >=2.1
retry.backoff.ms => 100 ms default, how much time to wait before next retry

## Producer timeout
if retries >0. eg. retries=(2^31), retried are bounded by timeout
delivery.timeout.ms=120000 => 2min
records will be failed if they can't be ack within delivery.timeout.ms
if you are not using idempotent producer (not recommended - old kafka)
- in case of retries, there is a chance that msg will be sent out of order (if a batch has failed to be sent)
- if you are using key-based ordering, that can be old issue (old version)
- for this can set the setting while controls how many prouce requests can be made in parallel ( max.in.flight.requests.per.connection
 - default 5
 - set it to 1 if you need ordering in old version
In kafka >= 1.0.0 there's a better solution with idempotent producers


## Idempotent Producer
- Producer can introduce duplicate msg in kafka due to network errors
if we have a bad request or duplicate requests
- producer produce => kafka => commit msg on the log and send back ack => ack never reachs out producer coz of network error
- producer retry
- producer pov, just one request but kafka commited two msg (for this to take over idepempotent producer come in place)


- in kakfa >= 0.11, can define "idempotent producer" won't introduce duplicates on network error
- producer produce => kafka => commit msg on the log and send back ack => ack never reachs out producer coz of network error
- producer retry => kafka detect duplicate, don't commit twice, but will send back ack
- are gurantee a stable and safe pipeline
- idempotent producer are default since kafka 3.0, recommended to use them
 - retries (2^31) 
 - max.in.flight.requests=1 ( kafka =0.11) or
 - max.in.flight.requests=5 ( Kafka >= 1.0 - higher performance and keep ordering - KAFKA-5494)
 - acks=all 
 - above settings are automatically applied if you set 
 producerProps.put("enable.idempotent", true)
 

## Kafka Producer default
- Since Kafka 3.0, producer is safe by default
- acks=all (-1)
- enable.idempotent=true ( duplicates are not introduced due to network retries)
- use min.insync.replicas=2(broker topic level) - ensure two brokers in ISR at least have data after ack
- use reties=MAX_INT (producer level) - retry until delivery.timeout.ms is reached
- delivery.timeout.ms=120000 - failed after retrying 2 minutes
- use max.in.flight.requests.per.connection=5 - ensure max performance while keeping msg ordering

- With kafka 2.8 and lower, default comes with
- acks=1
- enable.idempotent=false


## Message compression at the producer level
- producer usually sent data is text-based, for eg. json data
- compression can be applied at producer level, doesn't require config changes in broker or in consumer
- compression.type can be none(default), gzip, lz4, snappy, zstd (kakfa 2.1)
- compression is more effective the bigger batch of msg being sent to kafka
- https://blog.cloudflare.com/squeezing-the-firehose/

## the compressed batch has advantages
- much smaller proudcer request size ( compression ratio up to 4x)
- Faster to transfer data over the network => less latency
- Better throughput
- Better disk utilization in Kafka(store msg on disk are smaller)

## the compressed batch has disadvantages
- proudcer must commit cpu cycle to compression
- consumer must commit cpu cycle to decompression

## overall
- snappy or lz4 for optimal speed, compression ratio
- tweaking linger.ms and batch.size to have bigger batches, and therefore more compression and higher throughput
- use compression in production

- compression can be happens at broker level(all topics) or topic level
- compression.type= producer (default) , broker takes compressed batch from producer client and write it direcly to topics' log file without recompressing the data
- compression.type=none => all batcher are decompressed by broker (a bit efficient)
- Compession.type=lz4 (eg)
 - if compression type set on topic is equal to the producer setting, data is not going to be recompressed
 - if compression type diff settings, batches are decompressed by the broker and then re-compressed using the compression algorithm specified
 
Note=> warning => if you enable broker side compression, will consume extra CPU cycyles

- use compression setting on producer and leave the broker compression type default to producer

 
## linger.ms & batch.size ( batching mechanisms)
- default producer try to send records ASAP
- will have up to max.in.flight.requests.per.connection=5 meaning up to 5 msgs batche being in flight(being send bet producer in the broker) at most ( some parallelism)
- After this, if more msg must be sent whild others are in flight, kafka is smart and will start batching them before the next batch send
- smart batching help increase throughput while maintaining very low latency
- added benefits: batches have higher compression ratio so better effiency
- two setting to inflence the batching mechanisms
 - linger.ms (default 0), in Kafka 4 (default 5) how long to wait until we sent a batch, adding small no. (5ms) helps add more msg in the batch at the expense of latency
 - but kafka producer have to wait 5ms to add more msg in the batch b4 sending
 - batch.size : if batch is filled b4 linger.ms, increase the batch size
 
 prodcer going to send 3 msg, going to wait for linger.ms to close the batch, this allow producer keep on adding msg into the batch
 - and then after linger.ms is attained send one batch/ one request ( max size is batch size)
## batch size default 16kb
- max no. of bytes will included in batch
- increasing a batch size to eg(32kb or 64kb) can help increasing compression, throughput, effiency of requests
- if any msg bigger than batch size will not be batched
- a batch is allocated per partition, so make sure that don't set to no.that is too high, otherwise waste memory
( Note: monitor average batch size metric using Kafka producer metrics)

## High throughput Producer
- increase linger.ms and producer will wait few ms for the batch to filled up before sending them 
- if sending full batches and have memory to spare, can increase batch.size and send larger batches
- producer-level compression


## High throughput Producer demo/
- add snappy msg compression in our Producer
- snappy is helpful if your msg are text based, eg. log lines or JSON documents
- snappy has good balance of cpu,compression ratio
- increase batch.size to 32 kb and add delay to linger.ms 20 ms
- even setup at producer level, consumer work equally

kafka-topics.bat --create --topic wikimedia.recentchange --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1 

kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic wikimedia.recentchange --from-beginning


## Producer default partitioner when key!= null
- data is going through partitioner logic which decides how a record assigned to partition
- process is called key hashing
- process of determining mapping of key to partition
- in default Kafka partitioner, keys are hashed using murmur2 algorithm
` targetPartition = Math.abs(Util.murmur2(keybytes) % (numPartition -1)`
- same key goes to same partition and adding partition to topic completely alter the formula


## Producer default partitioner when key= null
- default partitioner
- Round robin - kafka2.3 and belows
- Sticky partitioner - Kafka 2.4 and above
 - improves the performance of producer especially when high throughput when the key is null
 
## Round Robin partitioner
- if key is null, Kafka <= 2.3, default partioner send data in round robin
- this result in more batches (one batch per partition) and smaller batches(eg. with 100 partitions)
- smaller batch leads  to more requests as well as higher latency

## Sticky partitioner
- Kafka >= 2.4, all records sent to single partition and not multiple to improve batching
- stick to partition until the batch is full or linger.ms has elapsed
- after sending the batch, partition that is sticky changes
- larger batches and reduces latency(bcoz larger requests, batch.size more likely to be reached)
- over time, records are still spread evenly across partition ( same as round robin again)

## max.block.ms and buffer.memory
- if producer produces faster than the broker can take, the records will be buffered in memory
- buffer.memoy=33554432 (32MB). the size of send buffer
- that buffer will fill up over time and empty back down when the throughput to broker increase(broker is less busy)
- if buffer is full(32MB) then .send() method will start to block( won't start async)
- max.block.ms=60000 (60 seconds), .send() will block on 60s until throwing exception. exceptions are thrown when
 - producer has filled up its buffer, (buffer under
 - broker is not accepting any new data
 - buffer is still not empty after 60ms elapsed
- if you hit an exception, broker are down or overloaded as they can't respond to requests


## Open Search Consumer and consumer configurations (OpenSearchConsumer project class Name)
- https://bonsai.io/
- java lib - open search rest high level client
download
https://github.com/opensearch-project/opensearch-build/blob/main/docker/release/dockercomposefiles/docker-compose-3.x.yml
D:Projectskafka-beginnerkafka-consumer-opensearchopen-search-docker
add .env file and add OPENSEARCH_INITIAL_ADMIN_PASSWORD=
docker-compose up
https://docs.opensearch.org/latest/security/configuration/demo-configuration/
http://localhost:5601/app/login?

https://sprout.bonsai.io/signup/sandbox

bonsai.io > pricing > subscribe free and create using open search

# index a doc
POST index/my-first-index
{
  "body": "here"
}

PUT /my-first-index/_doc/1
{
  "description": "This is a test"
}

GET /my-first-index/_doc/1

GET /wikimedia/_doc/rh1UZJsBdhgpTLs_UNR0

kafka-server-start.bat D:kafkaconfigserver.properties

# to check lagging in consumer group
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group consumer-opensearch-demo


## Delivery Semantics - At most once
- offsets are commited as soon as the msg batch is received. if the processing goes wrong, the msg will be lost ( won't read again
- consumer group read batch and then commited offset, and start processing data (sending email) at that time consumer group crashed, unprocessed data won't read again
- consumer group read data after the commited offsets

## Delivery Semantics - At least once
- offsets are commited after the msg is processed. if processing goes wrong, the msg will be read again. this result in duplicate processing of msgs.
- make sure your processing is idepempotent (i.e. processing the msg again won't impact your systems)
- consumer group read batch and start processing data (sending email) and then commited the offsets
- consumer group read batch and start processing data (sending email) and consumer group crashes, going to restart and then reading again from where the offsets have 
- last commited


## Delivery Semantic consumer - summary
### At-Most-Once
 - Consumer commits offset first
 - Then processes the message
 - If processing fails → message is lost
 ``` enable.auto.commit=true
     auto.commit.interval.ms=1000 
 ```
- Metrics collection
- Log aggregation
- Non-critical data
 
 
### At-least-once (DEFAULT & most common)
- Consumer processes the message
- Commits offset after successful processing
- If consumer crashes before commit → message is reprocessed
``` enable.auto.commit=false
```
- Order processing
- Payments (with idempotency)
- Most business systems
 

### Exactly-once (EOS)
- Message is processed once
- Offset commit + result write happen atomically
- ⚠️ Only guaranteed when:
- Kafka → Kafka
- Using transactions
```
enable.idempotence=true
transactional.id=my-tx-id
isolation.level=read_committed
```
- Typical flow
- Consumer reads message
- Producer processes & sends result
- Producer commits transaction
- Offsets are committed as part of transaction
- Stream processing
- Kafka Streams
- Financial-grade pipelines
- can be achieved for kafka => kafka workflows using transactional API(kafka Streams API)
- Sink Workflows, use an idempotent consumer


## Consumer offset commit strategies
- 2 strategies
 - easy -> enable.auto.commit=true & sync processing of batches
 - medium -> enable.auto.commit=false & manual commit offsets

### Kafka consumer - Auto Offset Commit Behaviour
- Java Consumer API - offsets are regulary commited
- Enable at least once reading scenario by default (under conditions)
- Offsets are commited when you call .poll() and auto.commit.interval.ms has elapsed
eg. auto.commit.interval.ms=5000 and enable.auto.commit=true will commit every 5 sec

- Make sure msg are all successfully processed b4 you call poll() again
 - if don't, at least once reading will not work
 - rarecase, disable enable.auto.commit and most likely processing to separate thread, then time to time call .commitSync()
 - or .commitAsync() with correct offsets manually(advanced)
 
- auto.commit.interval.ms 5000 => .poll() start timer => .poll 3sec later => .poll 3 sec later (6s total) => .commitAsync() coz 5+s has been elapsed (behind the scene)

### enable.auto.commit=true && sync processing of batches
- if you don't use sync processing, you will be "at-most-once" behavior coz offsets will be commmited b4 your data is processed

### enable.auto.commit=false  && sync processing of batches
- =>.poll and doSycn(batch) and .commitAsync() or .commitSync() manually
- eg. accumulating records into a buffer and then flushing the buffer to db + commiting offsets async

### enable.auto.commit=false  && storing offsets externally (Advanced)
- assign partitions to your consumers at launch manually using .seek() API
- need to model and store your offsets in db for eg
- need to handle cases where rebalances happen (ConsumerRebalanceListener interface)
- examples
 - if you need exactly once processing and can't find any way to do idempotence processing, then
 - process data + commit offsets as part of single transaction


### enable.auto.commit=false and describe consumer-group doesnot change lagging

## Consumer offset reset behavior
- consumer is expected to be read from log continuously
- if your consumer app has bug, consumer can be down
- Kafka have retention of 7 days, your consumer is down for more than 7 days, offsets are invalid
- auto.offset.reset=latest will read from end of the log
- auto.offset.reset=earliest will read from start of the log
- auto.offset.reset=none throw the exception if no offset is found

### consumer offset can be lost 
- if consumer has't read new data in 1 day (kafka <2.0)
- if consumer has't read new data in 7 day (kafka >=2.0)
- can be controlled by broker setting offset.retention.minutes (most user set it a month)

### there was no offset since consumer is down, consumer can read new data right? coz producer was not down
- Offsets expired + latest	Reads only new data
- Offsets expired + earliest	Reads old + new data
- Messages already expired	Cannot read old data ❌


### Replaying data for Consumers
- To replay data for consumer group
 - take all the consumers from specific group down
 - use kafka-consumer-groups command to set offset to what you want
 - restart consumer
- Note: 
 - suggest to set proper data retention period & offset retention period
 - Ensure the auto offset reset behavior is the one you expect
 - use replay capalibility in case of unexpected behavior
 
- reset shify by -500 (current committed offset Move it back by 500)
```
kafka-consumer-groups --bootstrap-server localhost:9092 --group consumer-opensearch-demo --reset-offsets --shift-by -500 --topic wikimedia.recentchange --execute

kafka-consumer-groups --bootstrap-server localhost:9092 --group consumer-opensearch-demo --reset-offsets --to-earliest --topic wikimedia.recentchange --dry-run
kafka-consumer-groups --bootstrap-server localhost:9092 --group consumer-opensearch-demo --reset-offsets --to-earliest --topic wikimedia.recentchange --execute
kafka-consumer-groups --bootstrap-server localhost:9092 --group consumer-opensearch-demo --reset-offsets --to-latest --topic wikimedia.recentchange --execute

help me add shift by -500
kafka-consumer-groups --bootstrap-server localhost:9092 --group consumer-opensearch-demo --reset-offsets --to-latest --topic wikimedia.recentchange --execute


kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group group-name --reset-offsets --to-earliest --topic topic-name --execute

kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group consumer-opensearch-demo
```

## Advance Setting to be modified if your consumer maxes out on throughput already

## Controlling Consumer Liveliness
- consumer in consumer group talk to consumer group coordinator (acting broker)
- heart beat thread is going to be your consumer sending msg to broker once in while saying they are alive
- poll thread is going to be other broker thinking consumer are still alive coz they are still requesting data from Kafka
- main things is to process data fast and poll often versus the opposite

### consumer group coordinator (acting broker)
- detect consumer are down, (heart beat and poll mechanisms)

### Consumer heart beat thread
``` heartbeat.interval.ms (default 3 seconds) ```
- send hearbeat to kafka
- usually set on 1/3rd of session.timeout.ms
- session.timeout.ms ( default 45 seconds Kafka 3.0+, before 10 seconds)
- hearbeats are sent periodically to the broker
- if no heart beat is send during the period , consumer is considered dead
- set session timeout to lower for faster consumer rebalances in case consumers are existing group

unexpectedly stop
### Take-away- mechanisms is used to detect a consumer app being down

### Consumer Poll thread
``` max.poll.interval.ms (default 5 min) ```
- max amount of time bet .poll() calls before declaring consumer dead
- relevant for big data frameworks like spark in case processing takes time
- if your data processing take more than 5 mins, Kafka going to think consumer is dead
- Take-away- mechanisms is used to detect a consumer app being down (consumer is stuck)
- in this case adjust max.poll.interval.ms based on data processing time

``` max.poll.records (default 500) ```
- control how many records to receive per poll request
- increase if your msgs are very small and have alot of available RAM
- Good to monitor how many records are polled per request
- if lower it take much time to process records


### Consumer Poll behavior
``` fetch.min.bytes (default 1)  ```
- control how much data you want to pull at least on each Request
- help improving throughput and decreasing request number
- if you increase this, improve throghput by decreasing the request no. at the cost of latency 
- eg, give me 1mb of data b4 return data to my consumer

``` fetch.max.wait.ms (default 500) : ```
- max amount of time kafka broker block b4 answering fetch request if there is not sufficient data to immediate satisfy the requirement
- given by fetch.min.bytes (eg. if there are not enough byte to fill the min.bytes (eg. 1mb)
- means that until reqirement of fetch.min.bytes to be satisfied, have to wait 500 ms of latency b4 fetch return the data to consumer
(eg. introducing a potential delay to be more efficient in requests)

``` max.partition.fetch.bytes (default 1 MB) ```
- max amount of data per partition the server will return
- if you read from 100 partitions, will need alot of memory(RAM) (eg. 100 MB)

``` fetch.max.byte (default 55 MB) ```
- max data returned by each fetch request
- if you have available memory, try increasing bytes to allow consumer to read more data in each requests


## Default Consumer behavior with partition Leader
- consumer rad by default from leader broker for partition
- higher latency (multiple data center + high network charges if consumer is in different different data center
- eg. data center = Availabity Zone(AWS) , pay for cross AZ network charges ( no cost)
  - if data center is in different zone, will be cost
  
## Kafka Consumer Replica Fetching ( Kafka v2.4+)
- possible to configure consumer to read from closed replica
- improve latency, decrease network cost if using the cloud

## Conumer Rack Awareness(v 2.4+) Setup
- Broker Setting (v2.4+)
- rack.id config must be set to data center id (ex: AZ id in AWS)
- eg (rack.id=usw2-az1)
- replica.selector.class must be set to org.apache.kafka.common.replica.RackAwareReplicaSelector

## Consumer Client Setting
- set client.rack to data center id the consumer is launch on

## Kafka Connect 
- write a way to get data out or Twitter?
- to send data from kafka to postgres/es/mongo db?
- Kafka connect is all about code & connector re-use
- import data from same resource
- eg (db,couchbase,SAP HANA, BlockChain, Cassandra, FTP, IOT, MQTT, RethinkDB, Salesforce,Solr,SQS, Twitter, Golder Gate...)
- store data in same sinks:
 - S3, ES, HDFS, JDBC, SAP HANA, BlockChain, Cassandra, Document DB, HBase, Redis,SOlr,Splunk, Twitter...)
- Kafka connect SSE source Connector - Kafka connect es sink - es
- connect cluster made of workers

## Kafka Connect high level
- souce connector to get data from common data sources
- sink connector - to publish that data in common data sources
- make it easy for non-exp dev to quickly get their data reliabliy into Kafka
- part of your ETL pipeline
- scaling make easy from small pipeline to company-wide pipelines
- achieve fault tolerance, idempotence, distribution, ordering


## Kafka Connect Deomo
- XXX -> kafka connect wikimedia source connector -> XXX -> kakfa Connect ES Sink -> opensearch
- ``` https://www.confluent.io/hub ``` over 80 connectors available
- kafka connect wikimedia souce connector https://github.com/conduktor/kafka-connect-wikimedia
``` 
	download 
	
	https://www.confluent.io/hub/confluentinc/kafka-connect-elasticsearch
	
	https://github.com/conduktor/kafka-connect-wikimedia/releases
	
	https://github.com/conduktor/kafka-connect-wikimedia/blob/main/connector/wikimedia.properties
	
	create folder connectors under D:kafka
	create folder kafka-connect-wikimedia under D:kafkaconnectors
	copy Kafka-connectet-wikimedia.jar to D:kafkaconnectorskafka-connect-wikimedia
	unzip  confluentinc-kafka-connect-elasticsearch-15.1.0 and paste into D:kafkaconnectorskafka-connect-wikimedia
	
	D:kafkabinwindowsconnect-standalone.bat D:Projectskafka-beginnerconnect-standalone.properties
	create file connect-standalone.properties
	
	D:kafkabinwindowsconnect-standalone.bat D:Projectskafka-beginnerconfigconnect-standalone.properties D:Projectskafka-beginnerconfigwikimedia.properties
	it was failed prebuild https://github.com/conduktor/kafka-connect-wikimedia/releases don't have the propery of user agent
	
	D:kafkabinwindowsconnect-standalone.bat D:Projectskafka-beginnerconfigconnect-standalone.properties D:Projectskafka-beginnerconfigelasticsearch.properties
	
	GET /wikimedia.recentchange/_search
```


## Kafka Streams demo
- count the no. of times a change was created by bot versus human
- analyse the no. of changes per web site
- no. of edits per 10 seconds as a time series
- With Kafka producer and consumer, can achieve very low level coding, not devloper friendly
- so Kafka Streams can cope
- Easy data processing and transformation lib within kafka
 - data transformation
 - data enrichment
 - fraud detection
 - monitoring and alerting
- Exactly once capalibility
- One record at a time processing(no batching)
- D:LearningLinkedin Learn Ex FolderEx_Files_Learn_Apache_Kafka_for_Beginnerskafka-beginners-course
- https://github.com/conduktor/kafka-beginners-course/tree/main/kafka-basics/src/main/java/io/conduktor/demos/kafka/advanced

```
kafka-topics.bat --create --topic wikimedia.recentchange --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1  

kafka-topics.bat --bootstrap-server localhost:9092 --topic wikimedia.recentchange --describe
 
kafka-topics.bat --bootstrap-server localhost:9092 --list
  
kafka-topics.bat --bootstrap-server localhost:9092 --topic wikimedia.stats.bots --describe

kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group wikimedia-stats-application

kafka-topics --bootstrap-server localhost:9092 --topic wikimedia.stats.bots --describe

```

# Need for Schema registry
- Kafka take bytes as input and publishes them
- No data verification
- Producer produces series of 0 and 1 and then consumer read those 0 and 1 (serializer and de-serializer)
- if proucer sends bad data, field get rename, data format changes from one day to another, then consumer break coz their deserializer doesn't know format was changed
- data to be self describable
- able to evolve data without breaking downstream consumer
- so need Schema and Schema registry
- Kafka Broker
 - donesn't parse or read your data (no cpu usage)
 - take bytes as input without even loading them into memory( zero copy)
 - distribute bytes
 - doesn't know if your data is string or integer, etc
- schema registry must be separate Components
- Producer and consumer need to be able to talk to Schema registry
- Schema registry must be able to reject bad data before it was sent to kafka
- A common data format must be agreed upon
 - support schemas
 - support evolution
 - to be lightweight
- Schema registry, Apache Avro as data format (Probuff, JSON)
- PIPE line without schema registry ( source -> Producer -> Kafka -> Consumer -> Target) 


# Schema registry purposes
- Store and retrieve schemas for Producer/consumer
- enforce backward/ forward / full compatibility on Topics
- decreases the size of payload of data sent to kafka
- PIPE line with schema registry ( source -> Producer -> Schema registry ->validate with Kafka -> then producer send Avro data  -> Kafka -> send Avro to Consumer -> retrive schema to do de-serializer -> target)
- Apache Avro recommeded,
- Confluent schema registry is free and source available


# Schema registry demo
- Start kafka with schema registry and Conduktor using docker
- Create Schema
- Send data using Producer
- Consume data using consumer

```
kafka-topics.bat --create --topic demo-schemaregistry --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1  


curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" 
      --data "{"schema": $(cat schema.avsc | tr -d 'n')}" 
      http://localhost:8081/subjects/Payment-value/versions
	  

# Register a new version of a schema under the subject "Kafka-key"
$ curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" 
    --data '{"schema": "{"type": "string"}"}' 
    http://localhost:8081/subjects/Kafka-key/versions
  {
	"type": "record",
	"name": "MyRecord",
	"namespace": "com.mycompay",	
	"fields": [ {"name": "id", "type" : "long"} ]
  }
  
  {
	"type": "record",
	"name": "myRecord",
	"fields": [ {"name": "f1", "type" : "string"} ]
  }

```

## Installing Conduktor Platform and testing of schema
```
git clone https://github.com/confluentinc/cp-all-in-one 
cd cp-all-in-one/cp-all-in-one 
docker-compose up -d

## Enter Kafka broker container
docker exec -it broker bash
kafka-topics --bootstrap-server localhost:9092 --list

#Create topic
kafka-topics --bootstrap-server localhost:9092 --create --topic demo-schemaregistry --partitions 3 --replication-factor 1

## Verify schema registered
http://localhost:8081/subjects

	
## Enter the schema-registry container
docker exec -it schema-registry bash

## Create schema value with topic demo-schemaregistry and Produce, A schema is auto-registered when you produce Avro data to a topic using an Avro producer.
kafka-avro-console-producer --bootstrap-server broker:9092 --topic demo-schemaregistry --property schema.registry.url=http://schema-registry:8081 --property value.schema='{"type":"record","name":"myRecord","fields":[{"name":"f1","type":"string"}]}'
{"f1":"hello world"}
{"f1":"hello world1"}

## Verify schema is registered
http://localhost:8081/subjects
should see below message
[
"demo-schemaregistry-value"
]

## Check schema details
http://localhost:8081/subjects/demo-schemaregistry-value/versions/latest

## Consume Avro message
docker exec -it schema-registry bash
kafka-avro-console-consumer --bootstrap-server broker:9092 --topic demo-schemaregistry --from-beginning --property schema.registry.url=http://schema-registry:8081


curl -X POST http://localhost:8081/subjects/demo-schema3registry-value/versions -H "Content-Type: application/vnd.schemaregistry.v1+json" -d '{"schema": "{"type":"record","name":"myRecord","fields":[{"name":"f2","type":"string"}]}"}'

```
	


## Which API is right
- source db => kafka => think kafka-connect-source
- produce data => kafka => kafka producer
- kafka to kafka transformation => Kafka Stream or KSql
- send data => db, analyse later => Kafka connect sink

 

## Partition count and replication factor
- two important parameters when creating Topics
- impact performance and durability of overall system
- eg. one topic with 2 partitions, 2 replication-factor
- if partition count increase during a topic lifecycle, that will break your key ordering guarantees if you are using key based messaging
- if replica factor increases during a topic lifecycle, that will put more pressure on your cluster, can lead to unexpected performance decreases

## Choosing Partition count
- each partition can handle a throughput of few MB/s( measure it for setup)
- more partitions means
 - better parallelism, better throghput
 - Ability to run more consumers in group to scale(max as many consumer per group as partition)
 - Ability to leverage more brokers if you have large cluster
 - but more elections to perform for Zookeeper
 - but more files opened on Kafka
### Guide lines
- partition per topic = Million Dollar question
 - (Intuition Small cluster(<6 brokers): 3x broker => 18 brokers?
 - (Intuition BIG cluster(<12 brokers):  2x broker => 24 brokers?
- Adjust the no. of consumer to run in parallel at peak throughput


## Choosing Replication Factors 
- At least 2, 3 or max 4
- higer replica Factors
 - better durability (N-1 broker can fail)
 - better Availabity (N-min.insycn.replicas if producer acks=all)
 - but more replication (higher latency if acks=all)
 - but more space (50% more if RF is 3 instead of 2)
### Guide lines
- Set it to 3 (you must have at least 3 brokers)
- if replication performance is issue, get a better broker instead of less RF
- Never set it to 1 in prod

## Cluster Guidelines
- total no. of partitons in cluster:
 - kafka with zookeeper => max 200, 000 partitions(Nov 2018) - Scaling limit (recommed max of 4, 000 partitions per brokers soft limit)
- Kafka with Kraft => millions of partitions
if need more partitoins in your cluster, add broker instead
if need more than 200, 000 partitions in your cluster (take time to get there), follow netflix model and create more kafka cluster
- don't need a topic with 1000 partitions to achieve high througput, set reasonable no.


## Topic Naming convention
- course suggest <message type>.<dataset name>.<data name>.<data format> ( dataformat avro,json,text,....)
- better to enfore guidelines in your cluster to ease management
- free to come up with your own guideline
- https://cnr.sh/posts/2017-08-29-how-paint-bike-shed-kafka-topic-naming-conventions/
- <message type>.<dataset name>.<data name>


## Video Analytics - MovieFlix
- to watch tv shows and movies on demand,
- user can resume video where they are left if off
- build user profile in real time
- recommend the next show to user in real time
- store all data in analytic Store
- D:\Projects\kafka-beginner\Video_Analytics_MovieFlix_Archi.png
- show-position topic 
 - is a topic that have Multiple producers
 - highly distributed if high volume > 30 partitions
 - key userId 
- recommendation topic-name
 - streams recommendation engine may source data from analytical store for histroical training
 - low volume topics 
 - key userId
 
 
## IOT examples - GetTaxi
- allows people to match with taxi driver on demand,
- user should match with close by driver
- pricing should "surge" if no. of drivers are low or no. of user is high
- All position data before and during the ride should be stored in analytic store, so cost can be computed accrately


driver => update location => driver location service => producer
user => update location => user location service => producer 
driver => update Availabity => Availabity Service => producer
match service => consumer
pricing => consumer => consume both user location and driver location => Kafka Streams
Analytics Conumer => Kafka Connect => Analytics Store (S3)


## CQRS (Common Query Reponsibility Segregation) - My Social Media - responsibiltiy are segrated, can called it CQRS
- allow people to post image and other to react by like and comment
- user able to post, like and comment
- user should see total no. of likes and comments per post in real time
- high volume of data is expected on 1st day of launch
- user should be able to see "trending" posts
- with kafka decouple the prod data to acutal agregation of data
- data itself in Kafka should be formatted as event
- eg. user123 created a post_id 456 at 2pm
 
user => post => producer
user_react_post => producer

user_status consumer =>   Kafka Streams => posts_with_count
user_trending_posts_consumer => Kafka Streams => trending_post
trending feed service => Kafka Connect


## Finance App ( My Bank)
- allows real time banking for its user. to alert users in case of large transaction
- txn data alreay exists in db
- threadsholds can be defined by user
- Alerts must be sent in real time to the user

kafka-connect source (CDC Connector (have alot)- Change data capture Debizium) => kafka streams + consume threadsholds => producer => alert_txn
user defined threadsholds => producer => should be event eg. user Enable threadsholds at $100 at 12 pm on 12 july 2026
Notification service(Consumer) 

## Big Data Ingestion
- common to have "genric" connectors or solutions to offload data from Kafka to HDFS, S3,Es for eg.
- kafka server as "speed layer" for real time app, while having a "slow layer" which help with data ingestion into store for later anlytics
- kafka as front to big data ingestion is common pattern in Big Data to provide "ingestion buffer" in front of some stores
- D:\Projects\kafka-beginner\big_data_ingestion.png


## Logging and metric aggregation
- ingest logs and metric from various apps
- want high throghput, has less restriction regarding data loss, replication of data, etc
- app logs can end up in your fav logging solution such as Splunk, CloudWatch, ELK, etc.. 

App1 => log_forwarder (producer) -> app_logs (topics) => kafka connect sink => splunk
App1 => metric collector (producer) -> app_metrics (topics) => kafka connect sink => splunk


## Kafka Cluster Setup - High Level Architecture
- Multiple brokers in different data centers (racks) to distribute your load. 
- want a cluster of at least 3 Zookeepers (if using Zookeepers)
- In AWS
 - us-east-1a (two Kafka brokers)
 - us-east-1b (two Kafka brokers)
 - us-east-1c (two Kafka brokers)
- not easy to setup a Cluster
- want to isolate each Zookeeper and broker on separate server
- monitoring need to implement
- operations must be mastered
- need an excellent Kafka Admin
- Alternative => managed "Kafka as a service" offerings from various companies
 - Amazon MSK, Confluent Cloud, Aiven, CloudKarafka, Instaclustr, Upstash, etc, ...)
 - no operation burdens (updates, monitoring, setup, etc...)
- how many brokers?
 - compute throughput, data retention, and replic factor
 - test your use case
 
## Other Components to properly setup
- Kafka Connect Clusters
- Kafka Schema Registry: make sure to run two for high Availabity
- UI tools for ease of Administration
- Admin Tools for automated workflows

## Kafka Monitoring and operations
- Kafka exposes metric through JMX.
- these metrics are highly important for monitoring kafka, ensuring the systems are behaving correctly under load
- common places to host kafka metrics:
 - ELK (ElasticSearch + Kibana)
 - Datadog
 - NewRelic
 - Confluent Control Center
 - Prometheus
 - Many Others...
 - Under Replicated partitions: Number of partitions are have problems with ISR(in-sync replicas) indicates high load on the system
 - RequestHandlers - Utilization of threads for IO, network,... overall utilization of Apache Kafka Broker
 - Request Timing - how long it takes to reply to requests. Lower is better, as latency will be improved
### Refs
- https://docs.datadoghq.com/opentelemetry/integrations/kafka_metrics/?tab=host
## Kafka operations
- Roling restart of brokers
- Updating config
- Rebalancing partitions
- Increasing replication factor
- Adding a Broker
- Replacing a Broker
- Removing a Broker
- Upgrading a Kafka cluster with zero downtime


## Needs for Kafka Security
- any client can access your kafka cluster(authenication)
- client can publish/consume any topic data (authorization)
- All data being sent is fully visible on network(encryption)
- sone can intercept data being sent (9092
- sone can publish bad data/steal data
- sone could delete topics
- need to study security and authenication model

## In Flight encryption in Kafka
- encryption in kafka enusre that data exchanged bet clients and brokers is secret to routers on the way
- similar concepts to https website
- ssl based encryption and declare new port 
- performance improvement - if you are using jdk 11

## Authenication (SSL & SASL) in Kafka
- Authenication in kafka ensures that only clients that can prove their identity can connect to our kafka Cluster
- basic auth (userName, password)
- SSL Auth - using SSL cert
- SASL/ plain text - 
 - client authenicate userName/passwod (weak  - easy to setup)
 - enable ssl encryption broker-side as well
 - Changes in password require broker reboot( good for dev only)
- SASL/SCRAM
 - userName/password with challenge(salt), more secure
 - enable SSL encryption broker side as well
- SASL/GSSAPI (Kerberos)
  - Such as Microsoft Active Directory ( Strong - hard to setup)
  - Very secure and enterprise friendly
- SASL/OAUTHBEARER
 - Leverage OAUTH2 tokens for authenication
 
## authorization in Kafka
- once a client is authenicated, Kafka can verify its identity
- needs to be combined with authorization, so kafka know what can do 
- ACL (Access Control list) - must be maintained by admin to onboard new users
- best support for kafka security is with java clients, librdkafka have good support for security 

## Kafka Multi Cluster && Replication
- operate well in single region
- A replication application at its core is just consumer + Producer
- different Tools
 - Mirror Maker2 - opensource kafka connector that ships with kafka
 - Netflix use Flink - they wrote their own application
 - Uber - ureplicator- addresses performance and operations issues with MM
 - Comcast - own open source kafka connect source
 - Confulent - own kafka connect source (paid)
- replication doesn't preserve offsets, just data, Data at an offset in one cluster is not sames as the data at same offset in another Cluster

## Kafka Multi Cluster & Replication - Active / Active
- two kafka cluster - setup two way replication bet two cluster - can write to both clusters
- Adv 
 - ablility to server users from nearby data center, have performance benefits
 - Redundancy and resilence - since every data center has all functionality . if one data center is unavailable you can direct users to remaining data center
- disadvantages
 - main drawback is the challenges in avoiding conflicts when data is read and updated async in multiple locations
- Other way to replication (Active - Passive)
- producer producing to active cluster that was replicated to passive cluster
- Adv
 - No need to worry about access to data, handling conflicts, other Architecture complexities
 - good for cloud migration
- disadvantages
 - waste of good cluster
 - currently not possible to perform cluster failover in kafka without either losing data or having duplicated events
- Ref
 - D:\Projects\kafka-beginner\ref_cluster_replicatoin.png

## Understand communicatin bet client and Kafka
- Advertised Listener is most important setting of Kafka
- client and Kafka broker are on different machines and kakfa broker has public ip or private ip so called Advertised listener (Adv host: ip)
- kafka client connect to broker via public ip
- adv listener so called req to kafka client use adv host
- if kafka client is on same private network, it is ok

- if adv host is set to public ip (OK) 
- if adv host is set public ip -> after restart public ip can be change if using dynamic dns
- if client are on private network, Set adv ip - internal private ip  -  private DNS hostname => only client on same private network can access
- if client are on public network, set external public ip or external public host name


## Topics config
- broker have default for all topic config params
- config params impact performance and topic behavior
- some topic need diff values than default
 - replica Factors
 - # of partitons
 - message size
 - compressoin level
 - log clean up policy 
 - min insync replicas
 - other config
- ref:: https://kafka.apache.org/41/configuration/broker-configs/

```
kafka-topics --bootstrap-server localhost:9092 --create --topic demo-schemaregistry --partitions 3 --replication-factor 1
kafka-topics --bootstrap-server localhost:9092 --topic demo-schemaregistry --describe

kafka-configs --bootstrap-server localhost:9092 --entity-type topics --entity-name demo-schemaregistry --describe
kafka-configs --bootstrap-server localhost:9092 --entity-type topics --entity-name demo-schemaregistry --alter --add-config min.insync.replicas=2
kafka-configs --bootstrap-server localhost:9092 --entity-type topics --entity-name demo-schemaregistry --alter --delete-config min.insync.replicas
```

## Partitions and Segment
- Topics are made of partitions
- Partitions are made of ...segments(files)!
- each segment have a range of offsets
- seg0 => 0-957,....
- the last segment is called active segment
- any one time for partition , only one segement is active (the one data is written to)
- Two segment settings:
 - log.segment.bytes: the max size of single segment in bytes(default 1gb)
 - log.segment.ms: the time kafka wait before commiting(closing) the segment if not full(1 week)
				   - after 1 week kafka will close the segment coz it is been a week and open new segment
				   
### Segment and Indexes
- segment comes with two indexes files:
 - an offset to position index: helps kafka find where to read from to find a message
 - A timestamp to offset index: helps kafka find messages with specific timestamp
 - Position index and timestamp index
 
- if you set a smaller log.segment.bytes(size, default 1gb) means:
 - more segments per partition
 - log compaction happens more often
 - but kafka keep more files opened (Error: too many open files)
#### How fast will have new segments based on throghput?
- A smaller log.segment.ms(time, default 1 week) means
 - You set a max frequency for log compaction (more frequent triggers)
 - You want daily compaction instead of weekly?
### How often do i need log compaction to happen?


## Log cleanup policies
- Many kafka cluster make data expire, according to a policy
- That concept is called log clean up
### Policy 1 - log.cleanup.policy=delete - default for all user topics
- delete based on age of data (default is a week)
- delete based on max size of log (default -1=infinite)
### Policy 2 - log.cleanup.policy=compact - default for topics __consumer_offsets)
- delete based on keys of your msgs
- will delete old duplicate keys after the active segment is commited
- infinite time and space retention
```
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic wikimedia.recentchange --from-beginning

kafka-topics --bootstrap-server localhost:9092 --describe --topic __consumer_offsets
```
### Log clean up why and when?
- deleting data from Kafka allows you to:
 - control the size of the data on the disk, delete obsolete data
 - overall: limit maintenance work on the Kafka Cluster
- How often does log clean up happen?
 - log clean up happens on partition segement!
 - smaller / more segments mean that log clean up will happen more often!
 - log clean up shouln't happen too often => take cpu and ram resources
 - the cleaner check every 15 seconds ( log.cleaner.backoff.ms)

### Log clean up policiy : delete
- log.retention.hours:
 - no. of hours to keep data for (default is 168 - one week)
 - higher number means more disk space
 - lower no. means less data is retained(if your consumer are down for too long, they will miss data)
 - other params allow: log.retention.ms, log.retention.minutes ( smaller unit has precedence)
- log.retention.bytes:
 - Max size in Bytes for each partition( default -1 - infinite)
 - useful to keep the size of log under threshold
Two common pair of options:
1. 1 week of retention
- log.retention.hours=168 and log.retention.bytes=-1
2. Infinite time retention bounded by 500 mb
- log.retention.ms=-1 and 	log.retention.bytes=524288000

### Log clean up policiy : compact
- ensures that your log contains at least the last know value for a specific key within a partition
- very useful if we require snapshot instead of full history (such as for data table in db)
- ideas is that we only keep latest "update" for key in our log
eg.
 - topic => employee_salary
 - keep most recent salary for our employees
seg 0=> key=> john(8000), key=Mark(9000), key=Lisa(10000)
seg 1=> key=> Lisa(11000), key=John(10000)
After compaction, one new segment will be created
new seg =>  key=Mark(9000), key=> Lisa(11000), key=John(10000)
- log compaction doesnot change the offset, delete keys when there are newer keys available

### log compaction gurantees
- Any consumer that is reading from the tail of a log(most current data) will still see all the msg sent to topic
- Ordering of msg it kept, log compaction only removes some msg, but does not reorder them
- the offset of msg is immutable(it never change). Offsets are just skipped by consumer if a msg is missing
- Deleted records can still be  seen by consumers for period of delete.retention.ms (default 24 hours)

### Log compaction Myth Busting
- doesn't prevent you from pushing duplicate data to kafka
 - de-duplication is done after a segment is commited
 - your consumer still read from tail as soon as data arrives
- doesn't prevent you from reading duplicate data from kafka
 - Same point as above
- Log compaction can fail from time to time
 - is an optimization and it the compaction thread might crash 
 - make sure you assign enough memory to it and that it gets triggered
 - restart Kafka if log compaction is broken
You can't trigger log compaction using API call(for now)

log compaction log.cleanup.policy=compact is impacted by:
 - segment.ms (default 7 days): Max amount of time to wait to close active segment
 - segment.bytes (default 1gb) - max size of segment
 - min.compaction.lag.ms(default 0)- how long to wait before a msg can be compacted
 - delete.retention.ms(default 24 hours) - wait before deleting data marked for compaction
 - min.cleanable.dirty.ratio(default 0.5) - higher=> less more efficient cleaning. Lower = > opposite - more cleaning, less efficent


### Log compaction practice
- Log compaction guarantees that duplicate data pushed into Kafka is automatically deduplicated.



D:\Projects\kafka-beginner\log_compaction.png


### unclean.leader.election.enable
- if all your insync replica go offline(but you stil have out of sync replica up).
 - Wait for ISR to come back online(default)
 - Enable unclean.leader.election.enable=true and start producing to non isr partitions
 
- if you enable unclean.leader.election.enable=true, you improve Availabity, but you will lose data bcoz other msg on ISR will be dicarded when they come back online and
replicate data from new leader
- overall, this is very dangerous setting, must be understood b4 enable it 
- usecase include metric collection, log collection, and other cases where data loss is accepable, at the trade-off of Availabity


## Large msg in kafka
- Kafka has default 1MB per msg in topics, as large msg are considered inefficient and anti-pattern
- Two approces to sending large msg:
 - using external store: store msg in HDFS,S3, google cloud storage, etc... and send ref to that msg to Apache kafka
 - modify kafka param, must change broker, producer, and consumer setting
 
1. Large msg usin external store
- Store large msg (video, archive, ...) outside of kafka
- send ref of that msg to Kafka
- Write custom code at producer, consumer level to handle this pattern
2. Large msg using param
- Topic wise, Kafka side, set max msg size to 10MB.
 - Broker side: modify message.max.bytes
 - Topic side: modify max.message.bytes
- Broker-wise, set max replication fetch size to 10MB
 - replica.fetch.max.bytes=10495880( in server.properties)
- Consumer side: increase fetch size of consumer will crash:
 - max.partition.fetch.bytes=10495880
- Producer side: increase max request size
 - max.request.size=10495880


build your data pipeline and App
- kafka Developer
 - connect
 - streams
 - Confluent ksqlDB
 - Confluent Schema Registry
- Kafka Admin 
 - 

https://www.linkedin.com/learning/complete-guide-to-apache-kafka-for-beginners/log-compaction-theory?autoSkip=true&resume=false&u=116771770