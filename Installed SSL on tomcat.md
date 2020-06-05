REF=>
https://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html

Installed SSL by self signed certificate


<code>"%JAVA_HOME%\bin\keytool" -genkey -alias tomcat -keyalg RSA</code>


This command will create a new file, in the home directory of the user under which you run it, named ".keystore".

C:\Users\USER\.keystore


In tomcat config > server.xml =>

<!-- Define an SSL Coyote HTTP/1.1 Connector on port 8443 -->
<code>
<pre>
<Connector
   protocol="org.apache.coyote.http11.Http11NioProtocol"
   port="8443" maxThreads="200"
   scheme="https" secure="true" SSLEnabled="true"
   keystoreFile="${user.home}/.keystore" keystorePass="changeit"
   clientAuth="false" sslProtocol="TLS"/>
</pre>
</code
