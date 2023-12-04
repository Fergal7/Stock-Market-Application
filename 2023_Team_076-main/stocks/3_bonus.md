# Bonus 1: Diagram

**Possible bonus: 0.5 points**

If you submit a clear, nicely formatted UML-like diagram that illustrates the design and architecture of your application, you can earn 0.5 bonus points. You can add the diagram as part of your report.

It is important to note that the goal of this diagram is **not** to show of all your classes/interfaces and every single field & method they have, but primarily which components your applications consist of and how these components depend on each other. It is also nice to highlight any design patterns in the diagram.

# Bonus 2: Deployment

**Possible bonus: 0.5 points**

In this bonus, you will be deploying your application using Docker. The idea is that you are essentially running your program as it would in a production environment. Docker allows us to run applications in isolated sandboxes called **containers**. You can think of these containers as very lightweight virtual machines. As such, running an application in a container isolates it from other running processes and ensures consistency. In other words, you should no longer end up with the often-heard complaint: "but it works on my machine" (although reality is often disappointing).

We have already done the basic Docker setup for you.
- We added two Dockerfiles: `stockApp.Dockerfile` and `traderApp.Dockerfile`. These specify how to build the containers of the Stock Application and Trader Application respectively.
- We added a `docker-compose.yml` file. This file specifies how the containers should be orchestrated. It contains information about how the different containers should be started up and communicated.

This bonus is (despite what its name suggests perhaps) quite trivial since most of the setup has already been done for you. As such, you are highly advised to do it. Not only does it give you some slight experience with Docker, but it also gives you a much, much nicer GUI.

### Important

You are free to complete this bonus after the soft deadline and still get the soft deadline bonus. If you submitted your program before the soft deadline and want to work on this bonus afterward, create a new branch and work on the deployment there. That way, any new commits will not be added to your pull request for the soft deadline. If you do not do this, you may not get the soft deadline bonus.

### Steps

1. Merge the `deployment` branch into your `stonks` branch.
2. Implement the additional of `TraderDataModel`.
3. Install the latest version of [Docker](https://www.docker.com/)
4. Install the latest version of [Docker-Compose](https://docs.docker.com/compose/install/)
5. Make sure your `Stock Application` is started on the port specified in the environment variable: `STOCK_EXCHANGE_PORT`.
6. Make sure your `Trader Application` connects to the stock exchange using:
	- The hostname stored in the environment variable `STOCK_EXCHANGE_HOST`
	- The port stored in the environment variable `STOCK_EXCHANGE_PORT`
7. Instead of using `SimpleViewFactory`, use the newly provided `WebViewFactory`.
8. Add the following plugin to the `<build>` section in the parent (root) `pom.xml` file:

	```xml
	  <plugin>
	    <artifactId>maven-assembly-plugin</artifactId>
	    <executions>
	        <execution>
	            <phase>package</phase>
	            <goals>
	                <goal>single</goal>
	            </goals>
	        </execution>
	    </executions>
	    <configuration>
	        <descriptorRefs>
	            <descriptorRef>jar-with-dependencies</descriptorRef>
	        </descriptorRefs>
	    </configuration>
	</plugin>
	```

9. Modify the `stockApp.Dockerfile` so that it calls the correct `Main` class for the Stock Application. This should be done in the last parameter of the `ENTRYPOINT` command.
10. Modify the `traderApp.Dockerfile` so that it calls the correct `Main` class for starting up the Trader Application. This should be done in the last parameter of the `ENTRYPOINT` command.
11. Call `docker-compose up` in the terminal from the directory that contains the `docker-compose.yml` file.
12. Open [localhost:3000](http://localhost:3000) in your web browser and enjoy the GUI.

### Notes

- Running `docker-compose up` produces a lot of output. Most of this is stuff that you can ignore, but any logging output produced by your Java applications will also be in there. Always make sure that your Java applications are started up properly and that there are no exceptions in this output.
- `docker-compose up` does not automatically rebuild your Java applications. As such, if you made a change to one of your applications and you want that change to reflect in the Docker-compose environment, make sure to run `docker-compose up --build`.

### Finally

In order to get this bonus, you should be able to run `docker-compose up` during the demo and demonstrate your program using the web GUI.