Object-relational mapping is simply the process of persisting any Java object directly to a database table. 
Usually, the name of the object being persisted becomes the name of the table, and each field within that 
object becomes a column. With the table set up, each row corresponds to a record in the application.

Using a connection pool instead of  DriverManager would also help with this because most, if not all, connection pools 
automatically release resources on garbage collection

First, Hibernate provides cleaner resource management, which means that you do not have to worry about the 
actual database connections, nor do you have to have giant try/catch/finally blocks. Error conditions may occur such 
that you do need to handle them, of course; but these are exceptional conditions, not normal ones.

Hibernate supports the Java Management Extensions (JMX), J2EE Connector Architecture (JCA), and Java 
Naming and Directory Interface (JNDI) Java language standards. Using JMX, you can configure Hibernate while it is 
running. Hibernate may be deployed as a JCA connector, and you can use JNDI to obtain a Hibernate session factory 
in your application. In addition, Hibernate uses standard Java Database Connectivity (JDBC) database drivers to 
access the relational database. Hibernate does not replace JDBC as a database connectivity layer; Hibernate sits on a 
level above JDBC.

a  dialect  (which allows Hibernate to correctly 
produce SQL for each given database); 


Connection Pooling
Hibernate uses JDBC connections in order to interact with a database. Creating these connections is 
expensive—probably the most expensive single operation Hibernate will execute in a typical-use case.

Hibernate is designed to use a connection pool by default, an internal implementation. However, 
Hibernate’s built-in connection pooling isn’t designed for production use. In production, you would use an external 
connection pool by using either a database connection provided by JNDI (the Java Naming and Directory Interface) or 
an external connection pool configured via parameters and classpath

C3 p0 ( http://www.mchange.com/projects/c3p0/) is an example of an external connection pool.

What’s the difference between a load() method and a  get() method? 
First, when the given ID can’t be found, the  load() method throws an org.hibernate.ObjectNotFoundException exception, whereas the get() method returns a null object. 
Second, the  load() method just returns a proxy by default; the database isn’t hit until the proxy is first invoked. 
The get() method hits the database immediately. 

The  load() method is useful when you need only a proxy, not’ a database call. You need only a proxy in a given session 
when you have to associate an entity before persisting.

Hibernate have two way to define persistence layer
1st use hibernate-entitymanager ==> like EclipseLink, Toplink called using JPA Entity Manager
2nd ==>SessionFactory

EntityManagerFactory has the same role as  SessionFactory in Hibernate. It acts a factory class that 
provides the EntityManager class to the application. It can be configured either programmatically or by 
using XML. When you use XML to configure it, the file must be named  persistence.xml and must be 
located in the classpath

Two transaction types define transactional behavior: 
JTA and  RESOURCE_LOCAL. JTA is used in J2EE-managed applications in which the container is responsible for 
transaction propagation. For application-managed transactions, you can use  RESOURCE_LOCAL

ID gen
Sequence gen
increment gen
hilo gen
Table gen ==> customized gen used to save in table as idGen for customized pattern sequence
Enhance Table gen ==>

Composite id ==> create additional class entiy for composite key ==> like 
public class EmployeeId implements Serializable {
    Long department;
    Long idCard;

For Hibernate caching to work correctly, you need to override the  equals() and hashCode() methods of 
the custom ID class. The  equals() method is used to compare two objects for equality, and the  hashCode() 
method provides an object’s hashcode. You use EqualsBuilder and HashCodeBuilder to simplify the 
equals() and hashCode() implementations. These classes are provided by the Apache Commons Lang 
library; you can download it from http://commons.apache.org/proper/commons-lang/download_lang.cgi. 
After you download the library, include  commons-lang-2.1.jar in your project’s Java build path.

Composite ID as a Property Using a Component Type
using @Embeddable

SaveorUpdate() 
Hibernate checks to see whether the object is transient (it has no identifier property); if so, 
Hibernate makes it persistent by generating the identifier and assigning it to a session. If the object already 
has an identifier, it performs update()

Dynamic SQL Generation in Hibernate
On application startup, Hibernate creates SQL statements for each of its persistent classes. 
That means Hibernate creates SQL statements for the Create, Read, Update, and Delete (CRUD) operations, but doesn’t
execute these statements. So, on application startup, an insert statement (create), a  delete, a read, and 
an update are created. The  update statement is created to update every field. At runtime, if the value isn’t 
changed, it is updated with the old value. 
The CRUD statements are cached in memory by Hibernate.
Dynamic SQL generation means turning off this Hibernate feature. 
You may want to do so because the feature can mean a longer startup time for the application. 
The amount of time depends on the number of 
entity classes in the application. Caching these SQL statements in memory can impact the performance of 
sensitive applications more in terms of unnecessary memory.

The annotation used is  @DynamicInsert, with  @DynamicUpdate at the class level.
if dynamicInsert is not enable system insert all the column with null value or default value
if enable hibernate insert only modify data only

in stixcloud==> SALES_SEAT_INVENTORY @dynamicupdate is enable




/// Getters and Setters. These are automatically created
// since we annotated this class with @Data from Lombok library
@Data
@AllArgsConstructor
@NoArgsConstructor
class AA{}

Note that the  <composite-element> tag doesn’t allow  null values when the enclosing collection tag 
is a  <set> tag because for delete operations, Hibernate needs to identify each composite element to be able 
to delete that particular element.
composite element
@Embeddable
@Table (name="Address")
public class Address {
	
Does Hibernate support mapping a collection of dependent objects? 	 		
Hibernate provides the  <composite-element> tag for mapping a collection of components. The collection 
elements/tags <set>,  <list>,  <map>,  <bag>, and  <idbag> can accommodate the  <composite-element> tag to 
map a collection of dependent objects.

This  Customer  object has a set of addresses defined by the  Address type.  @JoinTable (name = "Address", 
joinColumns = @JoinColumn(name="Customer_ID")) defines the join column and the object to which the set 
maps. Here, the join column is the customer_id. When the address table is created, the customer_id  column is 
added, and the ID from the customer table is reference

@Embeddable
@Table (name="Address")
public class Address {
 @Parent
    private Customer parent;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="Customer")
public class Customer {
 @ElementCollection(targetClass=Address.class,fetch=FetchType.EAGER)
        @JoinTable (name = "Address", joinColumns = @JoinColumn(name="Customer_ID"))
        private Set<Address> contacts;


reading for hibernate recipe 4 ==> stop at Chapter 4 Inheritance and Custom Mapping        
reading for hibernate recipe 4 ==> stop at Chapter 5 Inheritance and Custom Mapping        

==> NOT RECOMMEND <==
To use the table per subclass strategy, specify a new inheritance approach: instead of InheritanceType.
SINGLE_TABLE , use InheritanceType.JOINED. When the tables are created, one table is created for each type 
in the hierarchy; the superclass’ table contains all attributes managed by the superclass, and each subclass’ 
table has a foreign key pointing to the superclass.
This strategy has an advantage in that it’s normalized; conceivably, it uses less space in the database 
because there aren’t wasted fields ( VideoDisc doesn’t have  trackCount, for example, and  AudioDisc doesn’t 
have director.) Changing the data model affects only those subclasses being changed.
The table per subclass strategy can have a performance disadvantage compared with some other 
strategies when relying on polymorphism; it also means that the database runs a join for each query.


@MappedSuperclass
is recommended for inheritance
