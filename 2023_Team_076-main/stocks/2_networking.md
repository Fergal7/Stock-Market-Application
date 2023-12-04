# Assignment 2: Networking

In this assignment, we will be adding networking functionality to the message queue. The main focus of this assignment is on good design and the decoupling, reusability, and maintainability of the individual components. This means that complexity-wise, this assignment is quite a bit more complex than the first one. As such, be sure to start early.

Before you start, make sure that you have fully finished assignment 1, since this assignment is a continuation of it. Also familiarize yourself with the content on networking, concurrency, and the command pattern. **Read through the entire assignment description first**, so that you have an idea of what you have to do. Everything is not necessarily described in a step-by-step way. Try to first really understand everything; if necessary draw out all the components you will need. As with the previous assignment, we expect you to use test-driven development.

## Deadlines

This project has two deadlines:


- **Intermediate deadline**

  This is an optional deadline and provides you with an extra opportunity to get feedback on your code. If you create a pull request, we expect you to show up for the corresponding demo and this will be your opportunity to get some feedback on your progress. Again, this deadline is optional, but you are highly recommended to make use of it due to the complexity of the assignment. Try to complete as much as possible before this deadline. Your pull request doesn't need to pass the checks, since your pull request will be closed afterwards.

- **Final Deadline**

  This is the final deadline. Your entire program should be done at this point.

___

# Assignment Description

___

## Part 1 - Networking

Start by opening the project and navigating to the `networking` module.
We want to focus on decoupling our application as much as possible, so we are going to construct our networking in such a way that it is not tightly coupled with any other modules in our project. As a matter of fact, the networking module should not depend on any other module within our project in any way.

Networking is fundamentally about sending messages between client(s) and server(s). What to do with these messages has nothing to do with the networking, so we want to construct our networking infrastructure in such a way that we leave this up to whowever uses this networking module.

There are many different things we can send between client and server. One of the options is to send objects. This can be very useful, but also limits the usability of our networking. We will not be able to communicate with other applications that are not written in Java, as these objects are serialized by the JVM. Another option is to simply send strings. This gives us a lot more flexibility, since we can represent any object by a string (e.g. JSON, YAML, or XML) and the writing and reading of objects to and from the network are no longer dependent on the JVM serialization, but instead on how we implement the `object -> string` conversion (and vice versa). This conversion we can implement and utilize in various different applications, meaning that we can just as well communicate with applications written in other languages.

For the string representation of objects, we will eventually be using JSON. This is an incredibly versatile data format that is very easy to implement. In the networking part, we don't need to worry about that though. All we need to worry about here is creating a client-server infrastructure that allows for the sending of strings. Should we then ever decide to start using a different format (e.g. YAML or XML), then we don't have to change any of our networking.

Our networking will work over TCP and has the following requirements:

**Client**

- A client should be able to connect to a server using an `InetSocketAddress` provided in its constructor.
- A client should be able to send `String` messages to the server.
- Once a client is running, it should be able to continuously receive `String` messages from the server.

**Server**

- A server should be able to start up with a given port number.
- Once a server is running, it should be able to accept and handle multiple client connections.
- For each incoming client connection, the server should spawn a separate thread that handles the connection with that specific client. We will refer to this as a client handler.

**Client Handler**

- Once a client handler is running, it should be able to continuously receive `String` messages from its connected client.
- The client handler should be able to send `String` messages to its connected client.

### Design

You may have noticed that we didn't mention anything about what to do with the messages. This is because we want the networking to be completely decoupled from everything else. As mentioned before: what we want to do with the messages has nothing to do with the networking; that is extremely application specific.

So how do we keep our networking generic (not to be confused with generics), but still have a way of handling our messages? To do this we once again turn to the power of interfaces. Create an interface `MessageHandler` with a method `handleMessage(String message)`. This interface should be passed as a constructor argument to the client and client handler. That way, it is up to the developer to create an implementation of `MessageHandler`, which makes our networking completely decoupled from the functionality of handling the messages. As such, we can reuse this networking module in any application that works with sending String messages over the network; that's the dream!

In some cases, you might want to send messages back as a response. Think of how you can augment the `handleMessage` method so that any implementation of `MessageHandler` will also be able to send messages back.

> Don't just blindly implement all the above. Try to understand why we do this!

___

## Part 2 - Message Queue Networking

Now it is time to integrate networking with our message queue. For this, we need a few ingredients. Once we have these ingredients, it is up to you to cook them into a beautiful network-flavored message ~~stew~~ queue.

The requirements for this part are actually very simple:

- Allow a producer to put a message in a message queue running on a different machine.
- The messages should be communicated in JSON format using TCP.

The difficulty lies in designing this in such a way that it is easy to use, easy to extend and easy to maintain. Everything beyond this point is there to help you design it in the best way possible. Before we start with the details of how to implement everything, let's first go over what we want to do. The names below are of course only a suggestion; you are free to come up with your own names. Here is how we want the interaction with the networked message queue to be.

![Beautiful Image](images/networking_queue.png)

> The response procedure is not displayed in the above image, since for this assignment we are not sending any responses. However, think about which steps it should go through.

Note that the messages technically do not go directly to the server, but to the corresponding client handler instead.

It is important to remember that we are still setting up the infrastructure. Think of it as if we are creating a library that other people will use eventually. That means that we are not creating any `main` methods in this assignment (just like it doesn't make sense for a data structure library to have a `main` method). That said, it is good to have an idea of what using our library would look like (roughly) from a user perspective.

On the message queue side (server-side), a user should be able to write the following to start up a networked message queue:

```java
MessageQueue messageQueue = new PriorityBlockingMessageQueue();
NetworkServer server = new NetworkServer(..., port);
new Thread(server).start();
```
where `...` is an implementation of the `MessageHandler` interface (more on this later).
On the producer side (client-side), a user should be able to write the following to start up a networked producer:

```java
NetworkClient client = new NetworkClient(..., serverPort);
new Thread(client).start();
Producer producer = new NetworkProducer(client);
producer.put(new MqMessage("Test Header", "Test Body"));
```
Here `...` is once again an implementation of the `MessageHandler` interface.
The consumer side will be running in the same application as the message queue, so for that, we will be using the simple consumer implementation from the previous assignment:

```java
Consumer consumer = new SimpleConsumer(messageQueue);
MQMessage message = consumer.poll();
```
Ultimately, by doing it this way, we now have a message queue that works over the network, while it doesn't change how the user interacts with it.

> Again, the code snippets above are just rough suggestions of what we **eventually** want to be able to do. In this assignment we are still setting up the infrastructure, so there is nothing to run yet. That means we do not expect any `main` methods in your program.

> Since there is not `main` method and no concrete way to test if everything works together, consider writing a simple integration test as demonstrated in the tutorial.

### JSON Messages

Let's start by thinking of what messages we want to send. Our `networking` module allows for the sending of strings. We are going to be sending JSON representations of objects over the network. Throughout this project, all messages sent over the network will be of the message class type that you implemented in the first assignment.

- Create a method in your message class that converts it to a JSON string representation.
- Create a method in your message class that creates a new message object from a JSON string.
- You do not have to implement the JSON conversions yourself (although you can if you want to). Instead, you can use [Gson](https://sites.google.com/site/gson/gson-user-guide) for this. Note that Gson can have some difficulties with records and the `LocalDateTime` class, so this is not as trivial as it was intended to be sadly. On the upside, this is now an opportunity to learn something about custom JSON serialization/deserialization. You can find an example in the 2022_Assignments repository.

### Thread-safe queue

Since we are now working over the network, a queue may have multiple producers/consumers that can try to enqueue/dequeue at the exact same time. To prevent any issues with this, create one more implementation of `MessageQueue`. It should work exactly the same as the `OrderedMessageQueue` from the previous assignment (so messages should be ordered by their timestamp), except that it is now thread-safe. To do this, you can use the [PriorityBlockingQueue](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/PriorityBlockingQueue.html) data structure.

Note that any elements that we want to store in this priority queue should implement the `Comparable` interface. This is used to determine where to place the elements in the priority. Have a look at the documentation of [Comparable](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Comparable.html) and make your message class implement it so that messages can be ordered by their timestamp.

### Network Producer

At this point, we have the networking infrastructure in place to send strings over the network, we have a way of converting our messages into strings and we have a queue that is thread-safe to put the messages into. In other words, have almost all the ingredients.

We do not want the user to be aware of any of the details that are going on behind the scenes when communicating with the message queue. It should not matter to the user whether the message queue is run locally or whether all communication is done over the network. Luckily, we have already defined the behavior of producers/consumers in the previous assignment via an interface. In other words, all we have to do is create a new implementation of this interface; this time one that does not work with a local reference, but over the network instead.

For simplicity's sake, we will only be implementing the producer side over the network. That is: the producer will be communicating with the message queue over the network, and the consumer will still be communicating with the message queue via a reference. Implementing networking on the consumer side will be a bonus since this is significantly less trivial.

**module-info**

Now might be a good time to have a look at some of the `module-info` files. These files list the required dependencies of a module and also which parts of the module are available to other modules.
There are two important keywords here:
- If you want to use the `networking` package in the module `messagequeue`, then use the `exports` keyword and add the following to the `module-info` of the `networking` module:
	```java
	exports nl.rug.aoop.networking to messagequeue;
	```
	This does not include sub-packages. If your `networking` package has a sub-package, e.g. `util`, then you need to export that separately:
	```java
	exports nl.rug.aoop.networking.util to messagequeue;
	```
	If anyone should be able to use the package in question, you can omit the `to ..` part.
- If a module requires reflection, then you need to `open` the package to said module. This is the case for Mockito and Gson:
	```java
	opens nl.rug.aoop.messagequeue to com.google.gson;
	```
	There are some comments in the `module-info` files as well that should help you with this. In general, if something goes wrong with the modules, look clearly at the error. This often suggests what is going wrong (e.g. module .. does not export .. to ..). IntelliJ sometimes also suggests fixes when you intend to use classes from a different module while you don't have the correct `module-info` config yet.

Sometimes you can end up with module cycles. This means that module `A` requires module `B`, but module `B` also requires module `A`. If you encounter this, it generally indicates an issue in your design. If you are attempting to resolve this, make sure to update both the `module-info` and the `pom` file of a module if you are removing dependencies.

### Handling incoming messages

Let's start with the receiving of messages. Both the network client and server are continuously listening for incoming messages. We already have a `MessageHandler` interface that we have to pass to the client/server that allows the user to specify how the messages should be handled. A very common pattern with networking is that different messages should be handled in different ways (i.e. we are executing different pieces of code depending on the contents of a message).

We could of course create an implementation of `MessageHandler` with a gigantic switch statement, but it goes without saying that that has numerous negative downsides. Luckily, there is a very useful design pattern that can help us with this: the command pattern. The command pattern will allow us to execute different pieces of code depending on the contents of the message.

#### Command Module

Start by creating a generic (again, not to be confused with generics) implementation of the command pattern in the `command` module (not to be confused with this [command module](https://airandspace.si.edu/collection-objects/command-module-apollo-11/nasm_A19700102000)). This means there should be a command handler and a `Command` interface. Give the `Command` interface a method `void execute(Map<String, Object> params)`. The idea behind the parameter map is that it consists of key-value pairs similar to JSON. It can be used to pass various arguments to the command. It is then up to the implementation of the command to extract the required parameters and cast them appropriately. Make sure to - as always - start with the tests first.

The commands that the command handler should be defined by whoever uses this class. This will allow for easy testing of the class and makes the command handler very flexible. Think of a way how you can allow this.

The `command` module will not contain much. Eventually, the actual implementation of the commands should not reside in this module, but in the module that the commands relate to. For example, once we start adding commands that work on the message queue, these commands should reside in the `message-queue` module. In other words: we only want other modules to depend on `command`; we do not want `command` to depend on other modules. That way the `command` module is an independent and reusable module.

#### Command Implementation

Now that we have our `command` module, start by creating an implementation of `MessageHandler` that takes as its input a command handler. The `handleMessage` method should first convert the `String` message (a JSON message) into an actual message object. Once that is done, it can forward it to the command handler. The header of the message is then used as the name of the command.

One of the issues usually encountered with the command pattern is that different commands might need different parameters. To handle this, we use the aforementioned parameter map that maps `String`s to `Object`s (i.e. maps the name that the parameter would otherwise have to the actual parameter value).

The parameter map should contain the following:
- The header of the message
- The body of the message
- A reference to an interface that eventually allows the logic in the command to send back a message (remember that both a client and a client handler have the ability to send a message). This is where the extra parameter of the `handleMessage` comes in.

In the case of the message queue (server-side), we only need a single command: `MqPutCommand`. This command is responsible for putting the `body` of the message into the message queue. We will assume the body of the message to again be a JSON string representation of a message (why we do this will become clear later), so the responsibility of the `MqPutCommand` is converting the `body` of the message into a message object and then enqueuing said message into the message queue. On the client side, we don't need any commands for now.

### Producing over the network

So how do we go about invoking this `MqPutCommand`? The goal is to put a message in the message queue. We somehow need to let the server know that it should put the message in the message queue. We can do this by wrapping the message that we want to send in another message. This header then contains the name of the command:

```java
public NetworkMessage createPutMessage(MqMessage message) {
	return new NetworkMessage("MqPut", message.toJson());
}
```
This `NetworkMessage` also has a `header`, and a `body` and can be converted to/from JSON. The difference is that this class is not designed to be put in the message queue, but to be sent over the network. The advantage of using a separate class for this is not just that it is more readable, but also that we can augment it with more information that might be relevant to send over the networking without affecting the message queue messages.

### Final Check

If you have tested everything thoroughly, you should now have a working message queue that works over the network. Have a look at the requirements at the beginning of Part 2 to see whether your implementation can do this.

Some of you may also get itchy to run some stuff. This is not necessarily a bad idea. while unit tests are useful to test the individual components, they do not test the actual integration between the components. We do not expect you to write integration tests of any sort, but it might be worth running the message queue and consumer in one application, and the producer in another application. Verify that the producer can put something in the message queue.

## Bonus

For the bonus, implement a `NetworkConsumer` that communicates with the message queue over the network.

You can also earn a 0.5 bonus by implementing some basic integration testing.

# Questions

For every assignment, there are a number of questions that you have to answer. These can be found in a file called `2_questions.md`. The quality of your answers influences your grade. You should provide your answers in the `2_questions.md` file as well. These questions might also be asked during the demos and can alter your obtained grade!

# Peer Evaluation

For us to get an idea of how the group work went, you need to fill in a peer evaluation form. In this form, you will evaluate both yourself and your partner. We will look at this form to determine whether there are any significant issues with the group work. As a general tip: communicate clearly and often with each other and try to work together in person whenever you can.

You can find this form on BrightSpace. For this assignment, there are two forms: one for the intermediate deadline and one for the final deadline. Filling in **both** of these forms is **mandatory** (regardless of whether you decide to make use of the optional deadline or not), so don't forget it. The deadline for filling in the first form is one day after the intermediate deadline, and the deadline for the second form is one day after the final deadline.

# Handing in + Grading

To hand in your work, you have to create a pull request in your repository on GitHub. However, before you do this, verify the following:

- All your work is on GitHub;
- You followed the project structure we provided correctly;
- Your test methods are `public`;
- Running `mvn clean test` builds your project successfully and runs all your tests.

Once you have verified all of the above, you can create a pull request from the `networking` branch into the `main` branch. Make sure that a green checkmark appears next to your pull request.

The point distribution for your grade will look as follows:

| Category 		    							| Max points 	|
| ------------------------------|:-----------:|
| Functionality									| 3						|
| Design												| 3						|
| Testing 											| 2						|
| Documentation & Code Quality	| 1						|
| Answers to the questions			| 1						|
| Bonus													| 1						|

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
