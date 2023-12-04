# 🚀 Stonks 🚀

After a long wait, we have finally arrived at the epic finale of the ACU (AOOP Cinematic Universe). With the first two assignments done, we should now have a message queue that works over the network. In this assignment, we will be using this message queue to create a simulation of the stock market. Whereas in the previous assignment, the design decisions were served on a silver platter, the silver platter will be (almost) empty for this assignment. We will give you a number of requirements, and it is up to you to make a working program from these requirements. This means that any design decisions are up to you, provided that the requirements are satisfied.

## Deadlines

For this assignment, there are three deadlines:

- **Intermediate deadline**

  If you create a pull request, we expect you to show up for the corresponding demo and this will be your opportunity to get some feedback on your progress. Indicate whether you want a demo as well and whether you would like a code review. Also, indicate specifically what you would like feedback on. Again, this deadline is optional, but you are highly recommended to make use of it due to the complexity of the assignment. Try to complete as much as possible before this deadline. Your pull request doesn't need to pass the checks, since your pull request will be closed afterwards.

- **Soft Deadline**

	If you fully complete your project and create a valid pull request before this deadline, you will automatically receive a 0.5 bonus. Note that this is in addition to the potential bonus point, so you can earn 1.5 bonus points in total. You should aim for this deadline, as the hard deadline is during the exam week.

- **Hard Deadline**
	This is the final deadline. Your entire program should be done at this point.
___

# Introduction

Throughout this assignment, we use both the terms "stocks" and "shares". A stock is a generic term for the collection of shares for a single company. The shares are the actual "ownership pieces" of that company you can buy.

Traders can trade shares by sending buy or sell orders to the exchange. The exchange keeps track of all the buy and sell orders (the bids and asks).

In the real world, there are many order types, but for this assignment, the order types you will be implementing are limit orders. An order contains information about which stock to buy, how many shares, and - in the case of limit orders - at what price. The role of the price in a limit order depends on whether it is a buy or a sell order:

 - For buy orders, the price in a limit order denotes the maximum share price the trader wishes to buy their shares at.
 - For sell orders, the price in a limit order denotes the minimum share price a trader wishes to sell their shares for.

When a new order comes in, it will first try to match it against existing orders. For sell orders, this means finding the buy order with the highest price (provided that this price is higher than the limit price). For buy orders, this means finding the sell order with the lowest price (provided that this price is lower than the limit price). The exchange will keep trying to do this until the order is resolved. When no matching orders can be found (anymore), the buy/sell order should be added to the bids/asks.
Each time an order is (partially) resolved, the transaction is added to the transaction history of the trader who placed the order. A transaction contains information on how many of which stock was traded for how much.

The price of a stock is updated whenever a sell order for that stock (partially) resolves. The price is then updated to the price that was paid at the latest transaction. As an example, suppose we have a sell order in our asks for 10 shares at \$208. This means the seller is prepared to sell the stock for a minimum of \$208. When a new buy order comes with a limit price of \$204, the order cannot be matched, since the buyer is only prepared to pay at most \$204. As such, this buy order is added to the bids. When another buy order comes in for 10 shares with a price limit of \$209, it will be matched with the existing sell order for the price of the sell order. This means the order will be resolved for \$208 dollars and the price of the stock will be updated to \$208 (since this is the price of the latest transaction). Similarly, if a sell order for \$202 would come in, it would be matched against the buy order of \$204, and the price of the stock is updated to \$204.

While you only need to worry about limit orders, you should design your program in such a way that adding support for more order types is relatively easy.

Concretely, the above means that a stock exchange is keeping track of the following:

- Information about the stocks.
- Information about the orders (bids and asks).
- Information about each trader.

## Assignment information

The stock market you will be simulating is a heavily simplified version of how the stock market functions in the real world. In this assignment, the entire stock market consists of only two parts: the **stock exchange** and the **traders**.

The stock of a company has the following information available:

  - **Symbol**: A unique identifier for the stock. Often resembles the name of the company.
  - **Name**: The name of the corporation behind the stock.
  - **Shares Outstanding**: The number of shares that are held by all its shareholders.
  - **Price**: The price of a single share. As detailed later on, this is the price of its latest transaction.
  - **Market Capitalization**: The total market value of the company's outstanding shares. Calculated as `market cap = shares outstanding * price`.

The code you will create for this simulation will not run as one application, but instead as two independent applications. The exchange should be one application and all the traders (bots) are run and managed by another application. This means that these components have their own `main()` methods. Running these two applications can easily be done via the [MultiRun](https://plugins.jetbrains.com/plugin/7248-multirun) plugin for IntelliJ.

The communication between the two applications will happen via networking. To do this, you will be making use of the message queue and the `networking` module you created in the previous assignment.

Before you start, we highly advise you to fully read through the assignment and create a simple diagram of how your program will work. This will make it significantly easier to properly structure your project. As this is a large project, it is very important to first think before you start coding.

We have provided two additional modules for you in this assignment: the `stock-market-ui` and the `util` modules. Other than this, you will have to create your own modules. When doing so, don't forget to create a `module-info` and a base package to work in.

___

# 1 - Stock Application Requirements

We will start with the application that is responsible for the stock exchange itself and handling incoming and outgoing messages. We will refer to this as the `Stock Application`. The requirements are as follows:

- The `Stock Application` should be able to start up a networked message queue.
	- If you did not manage to fully finish the previous assignment and do not have time to finish it, you can implement the assignment without the message queue. This will come with a penalty, but it should allow you to finish the assignment.
	- You are allowed to assume that the message queue will always run "locally" in the `Stock Application` (although it would be nice if you cannot manage to not make this assumption). This means that you can use your simple consumer implementation from the first assignment.
- All incoming stock orders are assumed to arrive via a message queue.
- The `Stock Application` should continuously poll the message queue.
- The `Stock Application` should have support for executing the following orders:
	- Buy limit orders
	- Sell limit orders
- The exchange should attempt to resolve the orders as detailed above.
- The `Stock Application` should keep track of all connected clients.
- The `Stock Application` should periodically send the following information to all connected clients:
	- The information of all the stocks. The traders need (at the very least) the following information of each stock:
		- The stock symbol
		- The price
	- The information of the trader in question. This includes (at the very least) the following:
		- Their name
		- Their id
		- The stocks that they own and how many
		- Their available funds
	- These updates should be send roughly every second.
	> In a web application, the info of all users is not stored in your browser. It is stored on a server somewhere and your own info is sent to you when you log in. We follow a similar approach in this assignment. The info of all the traders is stored in the stock application and the info of a particular trader is periodically sent to them after they connect.
- The port number(s) that the `Stock Application` uses it should get from an environment variable. This can be done, for example, as follows:
	```java
	port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
	```
	If the environment variable does not exist, you can fall back on a pre-determined (hard-coded) port.
	> You don't need to actively do anything with the environment variables yet; just make sure your code supports them.
- The `Stock Application` should initialize the existing stocks from the provided `stocks.yaml` file. You are free to move the file anywhere or change its format (or add more data), as long as the data somehow ends up in your application.
- The `Stock Application` should initialize a number of traders from the provided `trader.yaml` file (provided in the `2023_Assignments` repository or create your own). You are free to move the file anywhere or change its format (or add more data), as long as the data somehow ends up in your application.

### Notes on loading from YAML

To load in YAML files, you can use [Jackson](https://github.com/FasterXML/jackson). How to use this was shown in one of the tutorials. Alternatively, you could look at the following [online tutorial](https://www.baeldung.com/jackson-yaml). Note that we have already provided a `YamlLoader` class in a `util` module that you can use for mapping the YAML file to an object.

Similar to Gson, in order to get Jackson to work with our module structure, we need to `open` the package of the class we are trying to load to the Jackson library. Suppose the class we are trying to load is located in a package called `stocks` in an `example` module, then we would have to add the following at the end of the `module-info.java` of the `example` module:

```java
opens nl.rug.aoop.example.stocks to com.fasterxml.jackson.databind;
```

### View

The exchange should have a very simple view that displays some information about each stock. To save you some time and effort on this, we have already provided a basic view implementation for you. To make use of this implementation, you need to implement the following interfaces from the `stock-market-ui` module:
- `StockDataModel`
- `StockExchangeDataModel`
- `TraderDataModel`

You can then start up the ui using the `SimpleViewFactory` class.

If you want, you can add more things to the view yourself. Do not spend too much time on this, as we won't be grading the visual quality of your view. Note that you don't necessarily need to keep or use all of the classes/interfaces we provide you. If you prefer to start from scratch then you are allowed to do so. For a more advanced (and fun) view, see the bonus.

___

# 2 - Trader Application Requirements

The other application we are creating is the `Trader Application`. This application is responsible for spawning a number of bots that interact with the `Stock Application` This has the following requirements:

- Each trader should continuously listen for messages from the `Stock Application`. The kind of messages that it should be able to handle are:
	- The information of all the stocks.
	- The information of the trader in question.
- Each trader keeps track of the following:
	- Their name
	- Their id
	- The stocks that they own and how many
	- Their available funds
- The trader information should be updated whenever a relevant update is received from the `Stock Application`
- Each trader bot should send a stock order to the message queue of the `Stock Application` every `x` seconds. How often exactly can be randomized; e.g. something between 1 and 4 seconds.
- The stock order that a trader bot made is determined by a trading strategy (more on this later).
- Trader bots should run on separate threads.
- To determine the port number(s) to connect to, the `Trader Application` should look at the environment variable with the same name as the ones used in the `Stock Application`. If this is not available, it should fall back on the pre-determined port(s).

> Hint: as various parts of your program run on different threads, it might happen that your program stalls at points without producing an error. If this is the case, try wrapping parts of your functionality in try-catch statements and logging any errors.

### Trade Strategies

Determining what kind of order can be made can be quite complicated. Luckily, you don't have to write a complicated algorithm for this; a simple random strategy is perfectly fine. An example strategy could be:

1. Choose randomly between buying and selling.
2. Choose a random stock.
3. Choose a random amount.

The limit price can be determined by slightly randomizing the current stock price. In the case of buy orders, traders should put the price slightly higher than the current price. In the case of sell orders, traders should put the price slightly lower than the current price.

Pay attention that the trader cannot sell stocks they do not own. When a trader makes a sell order, the shares will stay in their portfolio until that order is resolved and the trader is informed. As such, they should not be able to sell those same shares again while the order has not been resolved yet (as this would mean you could sell the same share twice). A trader bot should also not be able to buy stocks it does not have the money for.

> Important: while you only need to add a single trade strategy, you should design your program in such a way that it would be easy to add more strategies.

# 3 - Bonus

For the bonus, you should have a look at the `deployment` branch in your GitHub repository. If the branch is not there yet, it will be added in due time.

# Final Check

Verify that stock prices updates and that your program does not randomly crash or produce errors. Also, make sure to test your program thoroughly. Try to aim for high test coverage (cover as many methods as possible) and for the more important methods also try to cover most of the edge cases.

# Report

Usually, any GitHub repository is accompanied by a `README.md` which explains some of the basic stuff about the code in the repository. Up to now, the `README.md` files have been mostly used as the assignment descriptions. As we want you to have an idea of how to do this and prepare you for the future, you will be required to have such a file in your repository (more specifically, in the directory of this assignment).

We have already done some of the work for you, so you can find a template markdown file in your repository. Please fill in any sections indicate with a comment. We included some expected lengths with each section, but these are by no means set in stone. The general rule is that the section should be long enough so that it discusses what is asked in that specific section. Don't forget that you can (and might want to) add subsections.
If you are not familiar with markdown, don't worry as it is extremely easy to learn. A very useful guide can be found [here](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

### Additional info

The template of your README file was taken from [here](https://github.com/othneildrew/Best-README-Template/blob/master/BLANK_README.md). We slightly adjusted this to better fit this assignment. Be aware that we also removed some stuff that is not very relevant for this assignment (although normally you would still want this in your readme!). There are many different ways to structure your README and this is by no means the perfect one.

The purpose of this is not to have you spend a bunch of time on creating the perfect README, but rather to prepare you for future projects where you actually have to add such a file. Hopefully, this gives you an idea of what kind of stuff is usually done in the README. Try to focus primarily on the sections mentioned above, as those are the most important for this assignment.


# Peer Evaluation

For us to get an idea of how the group work went, you need to fill in a peer evaluation form. In this form, you will evaluate both yourself and your partner. We will look at this form to determine whether there are any significant issues with the group work. As a general tip: communicate clearly and often with each other and try to work together in person whenever you can.

You can find this form on BrightSpace. For this assignment, there are two forms: one for the intermediate deadline and one for the final deadline. Filling in **both** of these forms is **mandatory** (regardless of whether you decide to make use of the optional deadline or not), so don't forget it. The deadline for filling in the first form is one day after the intermediate deadline, and the deadline for the second form is one day after the final (hard) deadline.

# Handing in + Grading

To hand in your work, you have to create a pull request in your repository on GitHub. However, before you do this, verify the following:

- All your work is on GitHub;
- You followed the project structure we provided correctly;
- Your test methods are `public`;
- Running `mvn clean test` builds your project successfully and runs all your tests.

Once you have verified all of the above, you can create a pull request from the `stonks` branch into the `main` branch. Make sure that a green checkmark appears next to your pull request.

The point distribution for your grade will look as follows:

| Category 		    								| Max points 	|
| ------------------------------	|:-----------:|
| Functionality										| 4						|
| Design													| 3						|
| Testing 												| 1						|
| Documentation & Code Quality		| 1						|
| Report(readme)									| 1						|
| Bonus														| 1						|
| Delivering before soft deadline	| 0.5					|

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