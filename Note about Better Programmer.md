code reformatting tool

Robert Martin's advice and honour
Boy Scout Rule

Every day, leave your code a little better than it was. Remove
redundancy and duplication as you find it.


YAGNI => You Aren't Gonna Need It

that would be needed in the future, so decided to code it now,

That isn’t YAGNI. If you don’t need
it right now, don’t write it right now.

there are interesting cautionary lessons to be learnt
from our old code if you take the time to look at it.

Chapter 6 
Navigating a Route


Is there a CI (continuous integration) build server that
continually ensures that all parts of the code build successfully? Are there published
results of any automated tests?

A healthy build runs in one step, with no manual interven‐
tion during the build process


Look for tests: unit tests, integration tests, end-to-end tests, and the like. Are there
any? How much of the codebase is under test? Do the tests run automatically, or do
they require an additional build step? How often are the tests run? How much
coverage do they provide? Do they appear appropriate and well constructed, or are
there just a few simple stubs to make it look like the code has test coverage?


Static Analysis
 Doxygen can also pro‐
duce very usable class diagrams and control flow diagrams.

Requirements
Is there a project wiki
where common concepts are collected?

Thread handling

Exception Handling


Lay Trap
Diagnostic logs and assertions may be valid to leave in the code after you’ve found and
fixed the problem.

Add assertions or tests to verify the system invariants—the facts that must hold for the
state to be correct.

Assertions and logging (even the humble printf) are potent de‐
bugging tools. Use them often.

Learn to Binary Chop
Rather than single-stepping through code paths, work out the start of a chain of events,
and the end. Then partition the problem space into two, and work out if the middle
point is good or bad.


Test, Test, Test
As you develop your software, invest time to write a suite of unit tests
Once you have a suite of tests, consider employing a code coverage tool to inspect how
much of your code is actually covered by the tests


Let's try it out
The git bisect tool automates this binary chop for you, and is worth keeping in your toolbox if you’re a
Git user.


broken window theory

Chapter 10 Bug Hunting -> page 79
