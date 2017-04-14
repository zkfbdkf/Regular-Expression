Regular Expression to DFA
--------------------------
This program is simple program that convert Regular Expression to DFA.
After that, the program receives string to respond this string is accepted or not by Regular Expression.



How to Run?
__________________________
1. Open the cmd and then set the directory to folder that has build.xml and src folder.

2. Type ant -Darg0="argument" run
User needs to put Regular Expression in the argument.
ex)ant -Darg0=([123]|[45])" run

3. Type string that want to check whether accept or reject.



Other command
__________________________
1. ant compile: compile the source code in the src folder. Compiled class files go to build/classes.

2. ant jar: make jar file that can execute. Jar file goes to build/jar folder.

3. ant delete: delete the build folder.



Arguments
__________________________
The argument need to be the regular expression.
Form of the grammar is at below.
- space (which stands for epsilon)
- [a1a2...an]
- [^a]
- (RE.RE)
- (RE|RE)
- (RE)*

Input string and regular expression only can input digits (0,1,2,3,4,5,6,7,8,9)
