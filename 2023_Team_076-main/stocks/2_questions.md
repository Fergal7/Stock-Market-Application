# Question 1

In the assignment, you had to create a `MessageHandler` interface. Please answer the following two questions:

1. Describe the benefits of using this `MessageHandler` interface. (~50-100 words)
2. Instead of creating an implementation of `MessageHandler` that invokes a command handler, we could also pass the command handler to the client/server directly without the middle man of the `MessageHandler` implementation. What are the implications of this? (~50-100 words)

___

**Answer**: 1: It serves as a layer of abstraction that decouples the clinet/server from the command handling logic. It simplifies unit testing since you can easily substitute different MessageHandler implementations during testing allowing to help ensure the message handling process is isolated and tested independently. The interface also promotes modular design by seperating the message handling from the command execution. This makes it easier to add and modify message handling implementations without affecting the core logic.

2:Bypassing MessageHandler may simplify the initial setup but it can hinder modularity, testability and maintainability in the long run. It reduces flexibility as if you wanted to switch to a different message handling mechanism or support multiple communication protocols you might have to refactor a lot of client/server side code which can lead to some code duplication.

___

# Question 2

One of your colleagues wrote the following class:

```java
public class RookieImplementation {

    private final Car car;

    public RookieImplementation(Car car) {
        this.car = car;
    }

    public void carEventFired(String carEvent) {
        if("steer.left".equals(carEvent)) {
            car.steerLeft();
        } else if("steer.right".equals(carEvent)) {
            car.steerRight();
        } else if("engine.start".equals(carEvent)) {
            car.startEngine();
        } else if("engine.stop".equals(carEvent)) {
            car.stopEngine();
        } else if("pedal.gas".equals(carEvent)) {
            car.accelerate();
        } else if("pedal.brake".equals(carEvent)) {
            car.brake();
        }
    }
}
```

This code makes you angry. Briefly describe why it makes you angry and provide the improved code below.

___

**Answer**: The code angers me slightly because it very un-scalable not very maintainable and not nice to look at. This is because it uses a long series of if-else statements to map strings to car actions which in my opinion is just asking for errors.

Improved code:

```java

import java.util.HashMap;
import java.util.Map;

public class ImprovedImplementation {

   private final Car car;
   private final Map<String, Runnable> eventActions;

   public ImprovedImplementation(Car car) {
      this.car = car;
      this.eventActions = initializeEventActions();
   }

   private Map<String, Runnable> initializeEventActions() {
      Map<String, Runnable> actions = new HashMap<>();
      actions.put("steer.left", car::steerLeft);
      actions.put("steer.right", car::steerRight);
      actions.put("engine.start", car::startEngine);
      actions.put("engine.stop", car::stopEngine);
      actions.put("pedal.gas", car::accelerate);
      actions.put("pedal.brake", car::brake);
      return actions;
   }

   public void carEventFired(String carEvent) {
      Runnable action = eventActions.get(carEvent);
      if (action != null) {
         action.run();
      }
   }
}


```
___

# Question 3

You have the following exchange with a colleague:

> **Colleague**: "Hey, look at this! It's super handy. Pretty simple to write custom experiments."

```java
class Experiments {
    public static Model runExperimentA(DataTable dt) {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new RemoveCorrelatedColumnsCommand())
            .setNext(new TrainSVMCommand());

        Config config = new Options();
        config.set("broadcast", true);
        config.set("svmdatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("svmmodel");
    }

    public static Model runExperimentB() {
        CommandHandler commandSequence = new CleanDataTableCommand()
            .setNext(new TrainSGDCommand());

        Config config = new Options();
        config.set("broadcast", true);
        config.set("sgddatatable", dt);

        commandSequence.handle(config);

        return (Model) config.get("sgdmodel");
    }
}
```

> **Colleague**: "I could even create this method to train any of the models we have. Do you know how Jane did it?"

```java
class Processor {
    public static Model getModel(String algorithm, DataTable dt) {
        CommandHandler commandSequence = new TrainSVMCommand()
            .setNext(new TrainSDGCommand())
            .setNext(new TrainRFCommand())
            .setNext(new TrainNNCommand());

        Config config = new Options();
        config.set("broadcast", false);
        config.set(algorithm + "datatable", dt);

        commandSequence.handle(config);

        return (Model) config.get(algorithm + "model");
    }
}
```

> **You**: "Sure! She is using the command pattern. Easy indeed."
>
> **Colleague**: "Yeah. But look again. There is more; she uses another pattern on top of it. I wonder how it works."

1. What is this other pattern? What advantage does it provide to the solution? (~50-100 words)

2. You know the code for `CommandHandler` has to be a simple abstract class in this case, probably containing four methods:
- `CommandHandler setNext(CommandHandler next)` (implemented in `CommandHandler`),
- `void handle(Config config)` (implemented in `CommandHandler`),
- `abstract boolean canHandle(Config config)`,
- `abstract void execute(Config config)`.

Please provide a minimum working example of the `CommandHandler` abstract class.

___

**Answer**:

1. The other pattern being used is the Factory Method pattern, the getModel method in the Processor class acts as afactory method that creates instances of different model training command based on specified 'algorithm'.
There are many advantages to this, one being encapsulation which hides the details of the construction of command objects. Factory method reduces code duplication and also allows for extensibility as it promotes creating new command classes for new algorithms and extending factory method without affecting existing code, this follows the principle of encouraging code to be extended but closed for modification.
2.
    ```java
 
     public abstract class CommandHandler {
            private CommandHandler nextHandler;

    public CommandHandler setNext(CommandHandler next) {
        this.nextHandler = next;
        return next;
    }

    public void handle(Config config) {
        if (canHandle(config)) {
            execute(config);
        } else if (nextHandler != null) {
            nextHandler.handle(config);
        }
    }

    abstract boolean canHandle(Config config);

    abstract void execute(Config config);
    }


	```
___
