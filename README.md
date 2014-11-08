# CA448 Compiler Construction 1 Assignment

## Assignment 1: A Lexical Analyser for the simpL language

This project is my own work. I have not recieved assistance beyond what is normal, and I have cited any sources from which I have borrowed. I have not given a copy of our work, or a part of my work, to anyone. I am aware that copying or giving a copy may have serious consequences.

Author: Ian Duffy (11356066)

### Introduction

The aim of this assignment was to implement a lexical analyser using JavaCC

The Analyser had to recognize the following tokens:

#### Keywords

and, bool, const, do, else, false, if, int, main, not, or, real, return, string, then, true, var, void, while.

Implemented this as follows:

```
TOKEN :
{
	<AND : "and">
	| ....
	| WHILE : "while"
}
```

This was decided on based on seeing examples within the CA448 lecture notes[1] and [2]

#### Identifiers

Any other string of letters, digits or underscore character ('_') beginning with a letter.

For this I implemented some private helper tokens:

```
TOKEN :
{
    < #DIGIT : (["0" - "9"]) >
    | < #DIGITS : (<DIGIT>)+ >
    | < #LETTER : (["a" - "z", "A" - "Z"]) >
    | < #LETTERS : (<LETTER>)+ >
    | < #UNDERSCORE : ("_") >
}
```

I was then able to use this within the defination for identifier:

```
TOKEN :
{
	< IDENTIFIER : (<LETTER>) (<DIGIT> | <LETTER> | <UNDERSCORE>)*
}
```

I believe this means the requirements of the grammar, this is. A letter followed by a digit, or a letter or an undescore zero or more times.

### Strings

As in C string are delimited by double quotes. Strings may contain any alphanumeric found on a standard keyboard or punctuation characters listed below. Strings may contain quotes, backslashes or newlines only if escaped by a backslash.

I wanted a comprehensive handler for strings, I didn't feel like typing out every single character on my Irish keyboard. 

While researching I came across the strings token within [2].

```
TOKEN :
{
	<STRINGS: "\"" (~["\"","\\","\n","\r"] | "\\" (["n","t","b","r","f","\\","\'","\""] | ["0"-"7"] (["0"-"7"])?
                                                                            | ["0"-"3"] ["0"-"7"] ["0"-"7"]))* "\"">
}
```

I thought this was interesting as I figured it was comprehensive solution for strings that would cover all cases.

I thought it was interesting so began reading [4] to understand it.

First of all " is a special character in javacc, thus it must be escaped hence "\"" is just a single quote.

The tilde character in javacc means not.

The first list covers escaped quotes, escaped escapes, new line(both unix and dos)

The second list covers escaped escaped new lines, tabs, escape escape, escape quote, etc.

The last bit puzzled me, I figured it was some sort of character encoding, I emailed my lecturer to clarify, he pointed out that it referred to ASCII character codes in octal format. I confirmed this by looking at [5]. What does it mean? It means my paser will accept any ASCII character.


#### Numbers

A string of (decimal) digits. Real numbers are represented by a string of digits, a period character "." and a string of digits. Examples of valid numbers are 123, 0.123 and 1.23. Numbers such as 123. and .123 are invalid.

I used the above shown private token DIGITS or DIGITS followed by . DIGITS

#### Operators, relations and punctuation marks

+  -  *  /  %  =  !=  <  >  <=  >=  (  )  {   }   ,  ;  :  .  :=  ?  !

This was implemented again like the keywords. A name was assigned to each character.

#### Comments
In simpL can appear between any two tokens. There are two forms of comment: one is delimited by "/*" and "*/" and can be nested; the other begins with -- and is delimited by the end of line and this type of comments may not be nested.

Solution for this was baed of the reference notes [1].

### Validation

I modified my Java section to print out all tokens and the type they were matched too.

I knew if I ran the example files and I got a "other" token I've made an error. 

Just to make this easy to see, I added all found token kinds to a hashset, if "other" was found within the hashset a error was printed.

### References

[1] http://www.computing.dcu.ie/~davids/courses/CA448/CA448_JavaCC_2p.pdf

[2] https://javacc.java.net/doc/JavaCC.html

[3] http://www.ibm.com/developerworks/data/library/techarticle/dm-0401brereton/

[4] http://www.ascii-code.com/

[5] http://www.engr.mun.ca/~theo/JavaCC-FAQ/
