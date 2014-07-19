Networking Library
=================

This networking library that I developed provides an API for utilization of TCP and UDP effectively and efficiently. It is an event-based library that makes listening for events on the server or client very simple. It is built ontop of a networking wrapper I developed which handles all server and client communication over TCP and UDP. To keep things organized and make applications more extendable the library is also modular.


This networking library runs on Desktop and on Android.

## Starting A Server

When you want to start a new server you need to extend the abstract class "NEServerManager" which will handle all threads and prep the server for incoming clients. 

```java
public class ServerManager extends NEServerManager {
  
	public ServerManager(int tcpPort, int udpPort) {
		super(tcpPort, udpPort);
  	}
  
}
```

This will begin running a basic server on the TCP and UDP ports specified in the constructor.  To actually start the server you need to create a static main method to start the application.

```java
public static void main(String[] args) {
	new ServerManager(4395, 4395);
}
```

## Zones and Rooms

One of the core features I have implemented in the library is the Zone and Room architecture. Zones are containers for the rooms you add to your application. They manage the connected users and handle all incoming events. They also have their own EventHandler which handles important events like a User joining a Room in the Zone. Rooms allows clients to see eachother and interact whether it be through a chat room, a game world, or a puzzle. The interactions between users, like sending messages back and forth, must be programmed into the room using events and packets but some events have already been added for you.

Creating a Zone and Room is very simple and there are two ways you can create them. You can either create a new class that extends them or just instantiate a new Zone or Room object. To keep things organized I will be creating classes that extend Zone and Room.

Zone:
```java
public class MyZone extends Zone {

	public MyZone(String name) {
		super(name);
	}

}
```

Room:
```java
public class MyRoom extends Room {

	public MyRoom(String name, Zone parentZone) {
		super(name, parentZone);
	}
	
}
```

From this point we want to add the Room to the Zone to let the Zone handle it. In our Zone class we will do this:
```java
public MyZone() {
	super("MyZone");
	MyRoom room = new MyRoom("MyRoom", this);
	try {
		addRoom(room);
	} catch (NERoomExistsException | NEMaxRoomLimitException e) {
		e.printStackTrace();
	}
}
```
This creates a new MyRoom object and adds it to the zone. Now the zone will handle all incoming packets and send them to the room. You'll also notice I took out the constructor parameter and placed "MyZone" in the place of it so that the name of the zone is static. When you add a room to a zone there is a chance that either the max amount of rooms in the zone was reached or the room was already added to the zone. If one of those two cases is true it will throw an exception. We now just need to create a new MyZone object in the ServerManager to instantiate it.

```java
public class ServerManager extends NEServerManager {
  
  	private final MyZone myZone;
  
	public ServerManager(int tcpPort, int udpPort) {
		super(tcpPort, udpPort);
		myZone = new MyZone();
		System.out.println("Server Started.");
  	}
  
}
```

When a new Zone is instantiated it is automatically added to the ZoneManager which will be used by the server when receiving events.

Our basic server is officially finished. We created a Zone and a Room, added the room to the zone, and are now just waiting for incoming events. When running this server application nothing special will happen. This is because we have no client connecting to the server and no packets being sent.

## Starting A Client

The client is the side of the program that will connect to the server to send and receive packets of information. Setting up the client is very simple and all you must do is specify the IP address, TCP port, and UDP port to connect to. We also need to create a new class that will extend "NEClientManager". NEClientManager is an abstract class that takes care of connecting to the server and starting the threads to handle incoming TCP and UDP information.

```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		connect();
		if(client.isConnected()) {
			System.out.println("Connected to server.");
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
}
```
In the background this will create a new Client and connect to the server. It will then check to see if we are connected or not. We also need to create a main method to instantiate the ClientManager and start the client.

```java
public static void main(String[] args) {
	new ClientManager("localhost", 4395, 4395);
}
```
We are now done with our basic client. If you have followed along correctly you should be able to start a server and connect to it from the client side. It doesn't do much right now but I will introduce modularity and show you how to create new events and send them to the client and server. 

## Modularity - Creating A Module
Modules are similar to plugins in which they allow you to add new functionality to your applications. They are exported as .jar files and loaded at runtime by your application. Modules not only make the code very organized but they also make the code reusable. You can make a registration module for one application and go and use it in another without there being any errors. To create a module you need to extend the abstract NEServerModule class which makes you override the abstract init method. The init method is the starting point for the module and is where you will put any events you want to register and code you want to run. Keep in mind when creating a module it needs to be in a separate project from your Server and Client because it needs to be exported as a .jar file. 
```java
public class MyModule extends NEServerModule {

	@Override
	public void init() {
		System.out.println("My Module loaded.");
	}

}
```

It is as easy as that. Now when the module is loaded it will call the init method and print out "My Module loaded." Modules can be attached to Rooms or Zones and will be ran inside of them. Before we can load it onto a Zone or Room we need to export the project as a .jar file. For Eclipse users you can right click on the project, go to export, go to Java, and then go to Jar. Make sure you DO NOT go to "Runnable Jar". Loading a module is very simple and the only thing you have to specify is the location of the .jar file and the class that extends NEServerModule. Inside of the MyRoom class we will load the module and have it run in the Room.

```java
public class MyRoom extends Room {

	public MyRoom(String name, Zone parentZone) {
		super(name, parentZone);
		try {
			loadModule("modules/MyModule.jar", "com.net.MyModule");
		} catch (NEException e) {
			e.printStackTrace();
		}
	}
	
}
```
I placed my module's jar file in a folder called "modules" within my Server project. The location of my MyModule class is in the package "com.net" which was why I placed that infront of the class name. Now when you run the Server it should print out "My Module loaded." 

Soon, using these modules, you will be able to listen to incoming events and handle them accordingly. 

## Event Listeners and Event Handlers
The library is an event-based system so event listeners and event handlers manage the core functions of the library. The system works on the principle that every packet sent to the client or server has a packet name. The client/server waits for these packets and sends them to the event handlers. The event handlers find all of the event listeners waiting for the incoming packet name and send the packet to the listeners. From here the listeners handle the packet and do what they want with them. Every Zone, Room, and Module contains an event haddler which you can add event listeners to. This is where you will create your server and client communications.

The first thing we will do is create a new event listener for the server. To create an event listener you need to have a class implement the IEventListener interface. This will have you implement the methods "getListeningPacketName()" and "handlePacket(User user, Packet packet)". We are going to create a new class called MyEventListener inside of our module and have it implement IEventListener. 

```java
public class MyEventListener implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "myPacket";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		System.out.println("Got the packet!");
	}

}

```
You will see that in the getListeningPacketName() method that it returns "myPacket". This is the name of the packet that we will be waiting to get sent to the server. When the packet comes in the handlePacket method is called and is passed two parameters, the User the packet came from and the Packet itself. For now we will just have it print out "Got the packet!". All we need to do now is add the event listener to the module which we will do in the MyModule class.

```java
public class MyModule extends NEServerModule {

	@Override
	public void init() {
		System.out.println("My Module loaded.");
		addEventListener(new MyEventListener());
	}

}
```
To update the code on the server you need to re-export it as a .jar file. To see some results we will send a packet from the client to the server with the name "myPacket". This should then trigger the event listeners "handlePacket" method to be called on the server side.

```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		connect();
		if(client.isConnected()) {
			System.out.println("Connected to server.");
			Packet packet = new Packet("myPacket");
			client.getServerConnection().sendTcp(packet);
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
}
```
After the portion where it checks if the client has connected you will see I added two new lines. The first line we created a new Packet object with the name being set as "myPacket". This is the most basic Packet object that can be created and doesn't hold any information other than the name. I will introduce more advanced Packets later. On the second line we use the Client object to get the server connection. Then we send it to the server over TCP.

Now if you run the server and then run the client after the server side will print out "Got the packet!" Congratulations on creating your first basic networking application.

## NEPacket and NEObject


