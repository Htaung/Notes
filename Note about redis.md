Reis Server
Redis ဆိုတာဘာလဲ

Reis ဆိုတာ Key-Value ေတြကို Memory ေပါ္မွာ သိမ္းထားနိုင္တဲ့ Storage တစ္ခုသာသာပဲ
သူက ဘာေတြလုပ္ေပးနိင္လဲဆိုေတာ့
က်ြန္ေတာတို့ တစ္ခါတစ္ေလ မွာ  database က အျမဲတမ္းေခါ္သုံးျဖစ္တဲ့ data ေတြက္ု redis  server ေပါ္မွ သိမ္းထားျခင္းျဖင့္
database server ရဲ့ performance ကို ေလ်ာ့ခ်ေပးနိုင္တယ္ ဒီဟာက အျကမ္းအားျဖင့္ က်ြန္ေတာ္  ျမင္တဲ့အျမင္တစ္ခုပါ

ဟုတ္ျပီ အဲဒါဆိုရင္ redis မွာ ဘာေတြပါလဲ
Redis မွာ data structures 5 ခုပါတယ္

The building Block
က်ြန္ေတာ္တို သုံးေနက်တဲ့ Database ဆိုတာ application ေတြမွာ လိုအပ္တဲ့  data ကို တစ္ေနရာတည္းမွာစုစည္းထားျခင္းတစ္ခုပါပဲ
==> look like database have collection of table that tables are composed with fields
အဲဒီ စုစည္းထားတဲ့  data ကို တစ္ျခားapplication ကေန ျခားနားသြားေအာင္ name တစ္ခုအေနနဲ့ သတ္မွတ္လိုက္တာပါပဲ
 ==> look like creating database and give a name for that database

Redis မွာဆိုရင္ database ေတြကို number ေတြနဲ့ သတ္မွတ္တယ္
database တစ္ခု change မယ္ဆိုရင္  just type select 1
it will be look like in cmd or terminal ==>
127.0.0.1:6379[1]>
