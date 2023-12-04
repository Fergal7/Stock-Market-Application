<br />
<p align="center">
  <h1 align="center">Stock Market Simulation</h1>

  <p align="center">
     A real-time stock trading simulation platform 
  </p>
</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [Running](#running)
* [Modules](#modules)
* [Notes](#notes)
* [Evaluation](#evaluation)
* [Extras](#extras)

## About The Project
This project is a stock market simulation, we have two applications interacting with each other
one acting as a central stock exchange and the other acting as stock market users by spawning in bots to interact with the stock exchange with a basic strategy.

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
* [Maven 3.6](https://maven.apache.org/download.cgi) or higher

### Installation

1. Navigate to the `stocks` directory
2. Clean and build the project using:
```sh
mvn clean compile
```

### Running

1. Run ApplicationMain
2. Run TraderApplicationMain

## Modules
 The two main modules for this assignment were 'stock-application' and 'trader-application' as the other modules were from the previous assignments we will only briefly cover them.
### Stock Application
This module is all about making a StockMarket app work. You can create a StockApplication class using a StockExchange. This app also comes with a built-in server, so Traders can connect with their usernames. Once they're in, they can place Buy or Sell orders using the LimitBuyCommand and LimitSellCommand. They just need to say how much of a particular stock they want to trade.

The StockMarket app will then find matching BuyOrders and SellOrders (Order class) for them. When there's a match, like a BuyOrder and a SellOrder connecting, the app handles the Trader's stocks and funds accordingly. Every change to a Trader's portfolio or funds is tracked in a Transaction history using the Transaction class, so they can keep tabs on their trading actions.

### Trader Application
The trader application module simulates the behavior of traders in the stock market.
We spawn multiple bots each acting as an independent trader all running on different threads. They trade using a random trading strategy which is just a basic strategy that allows the bots to buy and sell shares while making sure they cant buy if they dont have enough money, sell shares they dont own ect.
Each bot keeps a record of its stocks, funds and other details updating them as it executes trades or recieves updates from Stock application.
We use this in our application when we run 'TraderApplicationMain' which then runs a simulation of traders using the stock market.

### Networking
This was made in the networking assignment and put in place a sort of client-server architecture for us, where it allowed us to have multiple clients (which became trader bots) connect to a server(became stock market) and send and recieve. 
This allowed real time updates and important communication.

### Util
This contained a Loader for YAML files which allowed us to read data for stocks and trader information.

### Stock Market UI
This contains basic display features allowing us to have a visual of our stock market which was updated in real time allowing us to see shares, funds, market cap ect. 

### Message Queue
This handle communication, in relation to the stock market it could act as a layer to manage and order commands, updates or other operations. This also allowed us to decouple stock updates and communications this allows for future modification  and scaling independently.

### Command
This was made in a previous assignment which was meant for us to use a command patter. This allows us to register commands(Buy, Sell) and encapsulate operations by using these commands.
This is useful for decoupling  as in this case we decouple the object thar invokes the command (The application) from the object that knows how to execute the specific command.
## Design

We used the command pattern in our project. We used this to register Buy and sell orders. We have LimitSellCommand, LimitBuyCommand and MqPutCommand which are classes that show a command patter. Each of these classes encapsulates a specific operation.
In our Application class you can see we register commands that allows us to execute commands based on the name
```sh
stockCommandHandler.registerCommand("Buy", new LimitBuyCommand(bids, asks, stockExchange));
stockCommandHandler.registerCommand("Sell", new LimitSellCommand(bids, asks,stockExchange));
```

<!--
List all the design patterns you used in your program. For every pattern, describe the following:
- Where it is used in your application.
- What benefit it provides in your application. Try to be specific here. For example, don't just mention a pattern improves maintainability, but explain in what way it does so.
-->

## Evaluation
The stability is ok, however our testing coverage wasnt great in the beginning which led to lots of problems later on, we had to spend a lot of time refactoring code and fixes NullpointerExceptions and small things like this, having good tests in place from the start would have saved us a lot of time here. We were getting some bugs with the stock market and traders such as traders being able to buy the shares they put up for sale and gaining money (dont think thats allowed in the real world) that was easily fixed just by checking if the trader buying was the same as the one seilling. We also had a bug that allowed traders to enter the negative with their funds
this was because they would try to buy a share, and if it wasnt resolved immediatley their funds wouldnt change meaning they were able to spend money they didnt actually have, similarly with selling stocks the same sort of thing would happen. This was fixed by reserving funds or shares until the trade was excecuted or cancelled either releasing funds/stocks or executing the order.

Our test coverage is fairly solid we test our methods but we dont test really for any strange edge cases.
We think our two applications run well together so were happy with that. The traders easily interact with the market and you can see the stock market UI update live as its happening.
A big improvement we could make is decoupling the trading and stock applications to allow for easier modification without affecting our program as much
<!--
Discuss the stability of your implementation. What works well? Are there any bugs? Is everything tested properly? Are there still features that have not been implemented? Also, if you had the time, what improvements would you make to your implementation? Are there things which you would have done completely differently? Try to aim for at least 250 words.
-->

## Extras

No extras sorry! We went basic for this one.

___


<!-- Below you can find some sections that you would normally put in a README, but we decided to leave out (either because it is not very relevant, or because it is covered by one of the added sections) -->

<!-- ## Usage -->
<!-- Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources. -->

<!-- ## Roadmap -->
<!-- Use this space to show your plans for future additions -->

<!-- ## Contributing -->
<!-- You can use this section to indicate how people can contribute to the project -->

<!-- ## License -->
<!-- You can add here whether the project is distributed under any license -->


<!-- ## Contact -->
<!-- If you want to provide some contact details, this is the place to do it -->

<!-- ## Acknowledgements  -->
