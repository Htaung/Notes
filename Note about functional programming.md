mostly-adequate.gitbooks.io/mostly-adequate-guide/ch03.html#oh-to-be-pure-again

//	associative
add(add(x,	y),	z)	===	add(x,	add(y,	z));


//	commutative
add(x,	y)	===	add(y,	x);


//	identity
add(x,	0)	===	x;


//	distributive
multiply(x,	add(y,z))	===	add(multiply(x,	y),	multiply(x,	z))

X(Y+Z) ==> XY + XZ



add(multiply(flockB,	add(flockA,	flockC)),	multiply(flockA,	flockB));


add(flockA,	flockC) ==> Identity ==> coz Flock C is 0 => so add(flockA,0) is equal to flockA

//	Original	line
add(multiply(flockB,	add(flockA,	flockC)),	multiply(flockA,	flockB));


//	Apply	the	identity	property	to	remove	the	extra	add
//	(add(flockA,	flockC)	==	flockA)
add(multiply(flockB,	flockA),	multiply(flockA,	flockB));


A =>4 
B=>2

BA +AB
B(A+A)
2AB

//	Apply	distributive	property	to	achieve	our	result
multiply(flockB,	add(flockA,	flockA));

multiply(2,multiply(flockA,flockB)


impure portion 
it	depends	on	system	state(mutable variable)	which	is	disappointing	because	it
increases	the	cognitive	load	by	introducing	an	external	environment.


A	side	effect	is	a	change	of	system	state	or	observable	interaction	with	the	outside
world	that	occurs	during	the	calculation	of	a	result.

The	philosophy	of	functional	programming	postulates	that	side	effects	are	a	primary
cause	of	incorrect	behavior.


A	function	is	a	special	relationship	between	values:	Each	of	its	input	values	gives	back
exactly	one	output	value


Functions	can	be	described	as	a	set	of	pairs	with	the	position	(input,	output):		[(1,2),	(3,6),
(5,10)]		(It	appears	this	function	doubles	its	input).


Functions	can	be	described	as table, graph
in graph x as input and y as output


const	toLowerCase	=	{
		A:	'a',
		B:	'b',
		C:	'c',
		D:	'd',
		E:	'e',
		F:	'f',
};
toLowerCase['C'];	//	'c'
const	isPrime	=	{
		1:	false,
		2:	true,
		3:	true,
		4:	false,
		5:	true,
		6:	false,
};
isPrime[3];	//	true

Pure	functions	are	mathematical	functions	and	they're	what
functional	programming	is	all	about

Portable	/	Self-documenting
Pure	functions	are	completely	self	contained.


//	impure
const	signUp	=	(attrs)	=>	{
		const	user	=	saveUser(attrs);
		welcomeUser(user);
};
//	pure
const	signUp	=	(Db,	Email,	attrs)	=>	()	=>	{
		const	user	=	saveUser(Db,	attrs);
		welcomeUser(Email,	user);
};

pure	form	is	much	more	informative	than	its	sneaky	impure
counterpart	which	is	up	to	who	knows	what.

In	a	JavaScript	setting,	portability	could	mean	serializing	and	sending	functions	over	a
socket.	It	could	mean	running	all	our	app	code	in	web	workers.	Portability	is	a	powerful	trait.

in	imperative	programming	rooted	deep	in
their	environment	via	state,	dependencies,	and	available	effects,	pure	functions	can	be	run
anywhere	our	hearts	desire


search try	Quickcheck	-	a	testing
tool	that	is	tailored	for	a	purely	functional	environments

quirk   /kwɜk/ /kwɝk/  noun   [ C  ] 
an unusual part of someone's personality or habit, or something that is strange and unexpecte 

Chapter	04:	Currying
Can't	Live	If	Livin'	Is	without	You
You	can	call	a	function	with	fewer	arguments	than	it	expects.	It
returns	a	function	that	takes	the	remaining	arguments

curry
//	curry	::	((a,	b,	...)	->	c)	->	a	->	b	->	...	->	c
const	curry	=	(fn)	=>	{
		const	arity	=	fn.length;
		return	function	$curry(...args)	{
				if	(args.length	<	arity)	{
						return	$curry.bind(null,	...args);
				}
				return	fn.call(null,	...args);
		};
};


$ used in variable
bind used in js

currying is	the	ability	to	"pre-load"	a	function	with	an	argument	or	two	in
order	to	receive	a	new	function	that	remembers	those	arguments

Higher order function is a function that takes a function or return a function

Pure function => 1 input to 1 output

Partial Application ===>
process of applying function to some of its arguments(predefined arguments)
applying arguments to a function (pre defined argument)

Composition	is	associative,	

the	slightly	more	complicated	variadic	definition	is	included	with	the
support	libraries	for	this	book	and	is	the	normal	definition	you'll	find	in	libraries	like	lodash,
underscore,	and	ramda.

If familiar	with	Fowler's	"Refactoring",	one	might	recognize	this	process	as	"extract
method"...except	without	all	the	object	state	to	worry	about.

Pointfree
