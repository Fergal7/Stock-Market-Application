# Assignment 1: DUQ

In this first assignment, you will be creating the basic infrastructure of our own (very) basic message queue: the Data Utility Queue (DUQ). In assignment 2, you will be extending your code by adding networking and threading support to this message queue. In the final assignment, you will be using the message queue and networking functionality to build a simulation of a (simplified) stock market. Since each assignment is a continuation of the previous one, you will work on a single project called `stocks`.

Note that this first assignment also serves as the introductory assignment to the course. As such, this assignment is not a representative indication of the difficulty of the next two assignments; these will require significantly more work. Before you start, read through the Testing chapter of the reader. Throughout this course we expect you to use JUnit 5.

To start, clone your repository, checkout the `duq` branch, and open the given project in an IDE of your choice (*cough* IntelliJ *cough*). Most modern IDEs allow you to open the project by opening the `pom.xml` file (pay attention that you open the `pom.xml` file in the `stocks` directory, not any of the `pom.xml` files in the subdirectories of this directory). Opening the `pom.xml` file means you won't have to create a new project yourself, so this saves us some effort.

## Important

Object-oriented programming is about managing complexity, so good code quality is crucial. Several automated checks performed on your commits that ensure that your code quality meets a certain standard. **Without passing these checks, your submission will not be accepted**. A failed check can be compared to passing 0 test cases on Themis. The checks are only performed on commits that are part of an open pull request.

The performed checks are quite rigorous, so it is important that you frequently run `mvn clean test`. Note that this is slightly different from `mvn clean compile` in that it will also run all the tests. If any of the tests fail, so will the build.

Details about which checks are performed can be found at the end of this document. This table also contains links to the documentation of each of the checks. The documentation often contains examples of code that fails the check. Since the majority of the errors indicate the exact file and line number where the check failed, you should be able to fix the vast majority of the errors produced by this automatic check very quickly. The usual exceptions to this are the checks on the file length and the method length since these often indicate deeper design issues. If you get stuck on a given check and don't know how to fix it (**after reading the documentation!**), don't hesitate to ask your TA.

> Incorrect formatting can produce a lot of checkstyle violations. To prevent this, frequently use the [reformat code](https://www.jetbrains.com/help/idea/reformat-and-rearrange-code.html) functionality of IntelliJ.

To ensure the checks always run properly, you are not allowed to modify the `.checkstyle` and `.github` directories. Additionally, you are also not allowed to modify the `pom.xml` file. Making modifications to these files/directories will result in your pull request being rejected.

> Pay attention that you don't make any accidental changes to them (or to the general directory structure). Should this happen, please inform your TA and they will help you revert the file to a previous commit.

## Project structure

The project you will be working with throughout the course is a Maven project. You may have noticed that there are two `pom.xml` files in the code structure we provided to you. That is because this project is divided into separate modules. You can think of a module as a sort of sub-project that contains a number of related packages. The advantage of using modules is that it allows us to split very large and complex projects into smaller - more manageable - chunks.

Modules provide very strict encapsulation in the sense that packages within a module are only visible to other modules when it explicitly exports them. This has the advantage that it forces you to think very clearly about what parts of your module you expose, and which modules depend on what other modules. This forces you to think better about the coupling between modules and generally improves the design of your program.

Exposing parts of a module to another module is done via a file called `module-info.java` that resides in every module. In this first assignment, you do not need to modify the project structure. You shouldn't add new modules, and you shouldn't modify the `module-info.java` or any of the `pom.xml` files. In short, you don't need to alter the module structure for this assignment. More information on modules will follow in later lectures/tutorials.

## Message queues

![Message Queue](images/mq.png)

In this assignment, you will be creating a simple message queue. Message queues are widely used in cloud applications. Think of a large number of services (components/applications/nodes, whatever you want to call them) that are all connected and communicating with each other in various ways via a network. Message queues can be inserted into this web of components to simplify the communication and improve performance. There are numerous other benefits to using a message queue, which you can read more about [here](https://aws.amazon.com/message-queue/benefits/).

What you should understand at a basic level is that a message queue is an intermediary service whose main job is to take messages from one or more components (publishers/producers) and reliably deliver them to one or more other components (subscribers/consumers). Some of the services that are widely used as message queues nowadays are [Apache Kafka](https://kafka.apache.org/intro), [RabbitMQ](https://www.rabbitmq.com/tutorials/tutorial-one-java.html), and [IBM MQ](https://www.ibm.com/docs/en/ibm-mq/8.0?topic=overview-introduction-mq).

A message queue generally consists of at least 4 parts:

- Messages
- A queue
- Producers
- Consumers

There are many different possible implementations of message queues and they generally provide much more functionality than just the delivery of messages. In this assignment, you will be creating a relatively simple poll-based message queue. This message queue has a producer-consumer structure where producers can `put` something in the message queue and consumers can `poll` the message queue to retrieve the first message in the queue. The main purpose of the message queue implementation in this course is the decoupling of components and simplification of the networking.

## Assignment Description

The aim of this assignment is to be an introduction to testing. That means that we expect you to use test-driven development. That means that you should divide this assignment into four stages:

1. Reading the requirements and thinking of which components you need (drawing a diagram helps);
2. Writing the tests for all the classes you need;
3. Writing the implementations;
4. Refactoring.

The most important thing is creating the tests first and writing the corresponding implementation later. One of the reasons for this is that it forces you to think of your tests in terms of concrete input and output, and not implementation-dependent details.

Below we will give the requirements of the message queue. It is up to you to produce a program from these requirements. In some cases, we will suggest you to use a certain existing class/interface. In those cases, we will not provide an explanation of how to work with them. This is something that you have to (learn to) figure out yourself.

> Make sure to test every method! Try to think of various test cases. Think of what happens when e.g. an invalid argument is given to a method or what if you execute a method multiple times, etc. Be sure to follow the proper JUnit conventions as described in the reader. This includes making all of your test methods `public`!

**Messages**

- A message has a `header`, a `body`, and a `timestamp`. Both the `header` and `body` are strings. For the `timestamp`, you can use the `LocalDateTime` class. Note that the header is not necessarily a unique identifier; it is perfectly fine to have two messages with the same `header` in the same queue.
- Messages must be immutable. That means that it should not be possible to modify their contents after creation.
- Create a constructor that only takes a `header` and `body`. The timestamp should be set to the current time.

**Queues**

- Any message queue should expose at least the following three methods:

	- `enqueue`: allows users to add a message to the message queue.
	- `dequeue`: returns (and removes) the oldest message in the message queue.
	- `getSize`: returns the number of messages currently in the message queue.

- Just as the `List` interface has various implementations, such as `LinkedList` and `ArrayList`, we also want our message queue to have various implementations. There should be two different queue implementations:

	- **An "unordered" message queue**: in this queue, messages should be ordered *based on when they arrive*.


	- **An ordered message queue**: in this queue, messages are ordered *by their timestamp*. That means that messages are not necessarily always inserted at the end of the queue.

In the end, we should be able to do something along the lines of:
```java
MessageQueue unorderedQueue = new UnOrderedMessageQueue();
```
or
```java
MessageQueue orderedQueue = new OrderedMessageQueue();
```

Of course, the implementations need some sort of data structure to store the messages. You could use a `List` for this (as you are probably used to), but this can be quite tedious. Instead, you can use the built-in [Queue](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Queue.html) data structure (e.g. with a [LinkedList](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/LinkedList.html) implementation) to store the messages in the unordered message queue and the [SortedMap](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/SortedMap.html) data structure, (e.g. with a [TreeMap](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeMap.html) implementation) to store the messages in the ordered message queue.

> Hint: if you're using a map, think about what happens if you add a message whose key already exists in the queue, and how you should deal with this.

Note that operations on the queues should be checked for edge cases; it should not be possible to insert `null`, for example.

### Producer Consumer

- Create two interfaces: `MQProducer` and `MQConsumer`. An `MQProducer` should be able to `put` and an `MQConsumer` should be able to `poll`.
- Create two simple implementations (one implementation for each of the two interfaces) that communicate with a `MessageQueue` simply through a reference (i.e. have a `MessageQueue` as constructor argument).

# Questions

For every assignment, there are a number of questions that you have to answer. These can be found in a file called `1_questions.md`. The quality of your answers influences your grade. You should provide your answers in the `1_questions.md` file as well. These questions might also be asked during the demos and can alter your obtained grade!

# Peer Evaluation

For us to get an idea of how the group work went, you need to fill in a peer evaluation form. In this form, you will evaluate both yourself and your partner. We will look at this form to determine whether there are any significant issues with the group work. As a general tip: communicate clearly and often with each other and try to work together in person whenever you can.

You can find this form on BrightSpace. Filling in this form is mandatory, so don't forget it. The deadline for filling in this form is one day after the deadline of the assignment.

# Handing in + Grading

To hand in your work, you have to create a pull request in your repository on GitHub. However, before you do this, verify the following:

- All your work is on GitHub;
- You followed the project structure we provided correctly;
- Your test methods are `public`;
- Running `mvn clean test` builds your project successfully and runs all your tests.

Once you have verified all of the above, you can create a pull request from the `duq` branch into the `main` branch. Make sure that a green checkmark appears next to your pull request.

The point distribution for your grade will look as follows:

| Category 		    							| Max points 	|
| ------------------------------|:-----------:|
| Free point										| 1						|
| Functionality									| 2						|
| Testing 											| 4						|
| Documentation 								| 1						|
| Code Quality									| 1						|
| Answers to the questions			| 1						|

More information about handing in can be found in the `GitHub Manual` on BrightSpace.

# Checkstyle

Below you can find a table with all of the checks performed by GitHub. Your code should pass all of these checks for your pull request to be accepted. You do not need to read through every single check, but you can use this table to quickly find the documentation when you encounter a given checkstyle violation. In addition to the checks below, it will also check whether your program actually compiles properly and passes all tests.

> One final warning: don't postpone running these checks until a few minutes before the deadline. It might take quite some time to fix all of them.

## General

| Performed Check  | Notes |
| ------------- | ------------- |
| [LineLength](https://checkstyle.sourceforge.io/config_sizes.html#LineLength) | Lines cannot be longer than 120 characters |
| [FileLength](https://checkstyle.sourceforge.io/config_sizes.html#FileLength) | Files cannot be longer than 400 lines. Note that this is the absolute maximum. Without including Javadoc, strive for at most 200.|

## Code Quality

| Performed Check  | Notes |
| ------------- | ------------- |
| [MethodLength](https://checkstyle.sourceforge.io/config_sizes.html#MethodLength) | Methods can have a maximum length of 30 lines. Note that this is the absolute maximum. You should strive for a maximum of ~20 lines. |
| [LambdaBodyLength](https://checkstyle.sourceforge.io/config_sizes.html#LambdaBodyLength) | Lambda bodies can have a maximum size of 15 lines.  |
| [ArrayTypeStyle](https://checkstyle.sourceforge.io/config_misc.html#ArrayTypeStyle) |  |
| [EmptyBlock](https://checkstyle.sourceforge.io/config_blocks.html#EmptyBlock) |  |
| [LeftCurly](https://checkstyle.sourceforge.io/config_blocks.html#LeftCurly) |  |
| [RightCurly](https://checkstyle.sourceforge.io/config_blocks.html#RightCurly) |  |
| [NeedBraces](https://checkstyle.sourceforge.io/config_blocks.html#NeedBraces) |  |
| [DeclarationOrder](https://checkstyle.sourceforge.io/config_coding.html#DeclarationOrder) |  |
| [FallThrough](https://checkstyle.sourceforge.io/config_coding.html#FallThrough) |  |
| [IllegalCatch](https://checkstyle.sourceforge.io/config_coding.html#IllegalCatch) |  |
| [IllegalThrows](https://checkstyle.sourceforge.io/config_coding.html#IllegalThrows) |  |
| [IllegalToken](https://checkstyle.sourceforge.io/config_coding.html#IllegalToken) |  |
| [IllegalType](https://checkstyle.sourceforge.io/config_coding.html#IllegalType) |  |
| [InnerAssignment](https://checkstyle.sourceforge.io/config_coding.html#InnerAssignment) |  |
| [MultipleStringLiterals](https://checkstyle.sourceforge.io/config_coding.html#MultipleStringLiterals) |  |
| [NoClone](https://checkstyle.sourceforge.io/config_coding.html#NoClone) |  |
| [OneStatementPerLine](https://checkstyle.sourceforge.io/config_coding.html#OneStatementPerLine) |  |
| [PackageDeclaration](https://checkstyle.sourceforge.io/config_coding.html#PackageDeclaration) |  |
| [ReturnCount](https://checkstyle.sourceforge.io/config_coding.html#ReturnCount) |  |
| [SimplifyBooleanExpression](https://checkstyle.sourceforge.io/config_coding.html#SimplifyBooleanExpression) |  |
| [SimplifyBooleanReturn](https://checkstyle.sourceforge.io/config_coding.html#SimplifyBooleanReturn) |  |
| [StringLiteralEquality](https://checkstyle.sourceforge.io/config_coding.html#StringLiteralEquality) |  |
| [UnnecessarySemicolonAfterOuterTypeDeclaration](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonAfterOuterTypeDeclaration) |  |
| [UnnecessarySemicolonAfterTypeMemberDeclaration](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonAfterTypeMemberDeclaration) |  |
| [UnnecessarySemicolonInEnumeration](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonInEnumeration) |  |
| [UnnecessarySemicolonInTryWithResources](https://checkstyle.sourceforge.io/config_coding.html#UnnecessarySemicolonInTryWithResources) |  |
| [UnnecessaryCheckstyleLink](https://www.youtube.com/watch?v=dQw4w9WgXcQ) |  |
| [InnerTypeLast](https://checkstyle.sourceforge.io/config_design.html#InnerTypeLast) |  |
| [InterfaceIsType](https://checkstyle.sourceforge.io/config_design.html#InterfaceIsType) |  |
| [MutableException](https://checkstyle.sourceforge.io/config_design.html#MutableException) |  |
| [OneTopLevelClass](https://checkstyle.sourceforge.io/config_design.html#OneTopLevelClass) |  |
| [ModifierOrder](https://checkstyle.sourceforge.io/config_modifier.html#ModifierOrder) |  |
| [RedundantModifier](https://checkstyle.sourceforge.io/config_modifier.html#RedundantModifier) |  |
| [MissingOverride](https://checkstyle.sourceforge.io/config_annotation.html#MissingOverride) |  |
| [EmptyForInitializerPad](https://checkstyle.sourceforge.io/config_whitespace.html#EmptyForInitializerPad) |  |
| [EmptyForIteratorPad](https://checkstyle.sourceforge.io/config_whitespace.html#EmptyForIteratorPad) |  |
| [EmptyLineSeparator](https://checkstyle.sourceforge.io/config_whitespace.html#EmptyLineSeparator) |  |
| [GenericWhitespace](https://checkstyle.sourceforge.io/config_whitespace.html#GenericWhitespace) |  |
| [MethodParamPad](https://checkstyle.sourceforge.io/config_whitespace.html#MethodParamPad) |  |
| [TypecastParenPad](https://checkstyle.sourceforge.io/config_whitespace.html#TypecastParenPad) |  |
| [RedundantImport](https://checkstyle.sourceforge.io/config_imports.html#RedundantImport) |  |
| [UnusedImports](https://checkstyle.sourceforge.io/config_imports.html#UnusedImports) |  |
| [Indentation](https://checkstyle.sourceforge.io/config_misc.html#Indentation) |  |
| [CommentsIndentation](https://checkstyle.sourceforge.io/config_misc.html#CommentsIndentation) |  |
| [ConstantName](https://checkstyle.sourceforge.io/config_naming.html#ConstantName) |  |
| [DefaultComesLast](https://checkstyle.sourceforge.io/config_coding.html#DefaultComesLast) |  |
| [EmptyCatchBlock](https://checkstyle.sourceforge.io/config_blocks.html#EmptyCatchBlock) |  |
| [EqualsHashCode](https://checkstyle.sourceforge.io/config_coding.html#EqualsHashCode) |  |
| [InvalidJavadocPosition](https://checkstyle.sourceforge.io/config_javadoc.html#InvalidJavadocPosition) |  |
| [MethodName](https://checkstyle.sourceforge.io/config_naming.html#MethodName) |  |
| [NestedIfDepth](https://checkstyle.sourceforge.io/config_coding.html#NestedIfDepth) | Maximum of 3 nested if-statements. |
| [TypeName](https://checkstyle.sourceforge.io/config_naming.html#TypeName) |  |
| [ParameterNumber](https://checkstyle.sourceforge.io/config_sizes.html#ParameterNumber) | Maximum of 7 parameters. |
| [VisibilityModifier](https://checkstyle.sourceforge.io/config_design.html#VisibilityModifier) |  |
| [AvoidNoArgumentSuperConstructorCall](https://checkstyle.sourceforge.io/config_coding.html#AvoidNoArgumentSuperConstructorCall) |  |

## Javadoc

| Performed Check  | Notes |
| ------------- | ------------- |
| [AtclauseOrder](https://checkstyle.sourceforge.io/config_javadoc.html#AtclauseOrder) |  |
| [NonEmptyAtclauseDescription](https://checkstyle.sourceforge.io/config_javadoc.html#NonEmptyAtclauseDescription) |  |
| [JavadocStyle](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocStyle) |  |
| [SummaryJavadoc](https://checkstyle.sourceforge.io/config_javadoc.html#SummaryJavadoc) |  |
| [JavadocMethod](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocMethod) |  |
| [JavadocType](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocType) |  |
| [MissingJavadocType](https://checkstyle.sourceforge.io/config_javadoc.html#MissingJavadocType) |  |
| [MissingJavadocMethod](https://checkstyle.sourceforge.io/config_javadoc.html#MissingJavadocMethod) |  |
| [JavadocVariable](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocVariable) |  |
| [JavadocContentLocationCheck](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocContentLocation) |  |
| [JavadocBlockTagLocation](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocBlockTagLocation) |  |
| [JavadocMissingLeadingAsterisk](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocMissingLeadingAsterisk) |  |
| [JavadocMissingWhitespaceAfterAsterisk](https://checkstyle.sourceforge.io/config_javadoc.html#JavadocMissingWhitespaceAfterAsterisk) |  |
