# Compilers HomeWork2 Spring 2019

## Intrο

The purpose of this project is building a semantic analysis tool for a subset
of Java, MiniJava, given the grammar it supports in JavaCC form and using JTB
tool to convert it into class hierarchies. After that, a series of visitors will
inspect the input file and report any possible semantic errors/mismatches.

## Visitors

I decided to use two visitors.  
The first one will create and initialize the symbol table
using a bunch of java.util and user-defined data structures. These information will later on be passed
out to the next visitor, whose task is to type check on the input file.
In case of a flawless input, an appropriate message will be displayed or
else a message describing the kind and exact position, if possible, of the error.

### FirstVisitor

This visitor will implement the first pass on the input file, looking for errors like the
the following:

* **Uniqueness** of classes as well as uniqueness of fields and methods inside a class.
* Method **overriding**, not matching prototype definition.
* Extending a not previously declared class.

To build a nice LookUp Table holding all information necessary for each class, an appropriate structure,
ClassData, had to be defined, holding the meta data of class of the source. These are:

* Parent class name, in case the class extends another.
* A map with name-value pairs for each variable, where value is a pair holding the following:
  * The data **type** of the variable
  * and the exact **address** of the variable in the virtual memory.
* A map with name-value pairs for each member method, where value is a triplet holding the following:
  * The **return type** of the method
  * The exact **address** in virtual memory, where a pointer to the method is stored.
  * A **parameter list**, holding just the type for each parameter of the method, ordered.

### Second Visitor

After constructing a Lookup Symbol Table, we need to traverse the syntax tree once more and
inspect errors of the following type:

* Invalid Types in variable or method declarations
* Undeclared variables or methods
* Array's indexes and content not an integer
* Conditions not evaluating to boolean
* Logical operations on non-booleans
* Printing type other than int and boolean
* Comparison and arithmetic operations amongst non-integers
* Array methods applied on some other data-type

Any of the above, will raise an exception.  
If no error is detected during the second traversal, the semantic correctness of the input program is
unquestionable.

## Testing

Directory _tests_ contains all kinds of valid and invalid input files, taking into consideration
each and every possible error type. Run bash script _testing_.sh to make sure all test cases are passed.

Hopefully, this document makes things a bit more clear.  
Thank you.