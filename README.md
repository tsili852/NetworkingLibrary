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
The library is an event-based system so event listeners and event handlers manage the core functions of the library. The system works on the principle that every packet sent to the client or server has a packet name. The client/server waits for these packets and sends them to the event handlers. The event handlers find all of the event listeners waiting for the incoming packet name and send the packet to the listeners. From here the listeners handle the packet and do what they want with them. Every Zone, Room, and Module contains an event handler which you can add event listeners to. This is where you will create your server and client communications.

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
You will see that in the getListeningPacketName() method that it returns "myPacket". This is the name of the packet that we will be waiting to get sent to the server. When the packet comes in the handlePacket method is called and is passed two parameters: the User the packet came from and the Packet itself. For now we will just have it print out "Got the packet!". All we need to do now is add the event listener to the module which we will do in the MyModule class.

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
After the portion where it checks if the client has connected you will see I added two new lines. The first line we created a new Packet object with the name being set as "myPacket". This is the most basic Packet object that can be created and doesn't hold any information other than the name. I will introduce more advanced Packets later. On the second line we use the Client object to get the server connection and send it to the server over TCP.

Now if you run the server and then run the client after the server side will print out "Got the packet!" Congratulations on creating your first basic networking application.

## NEPacket and NEObject
NEObject is a class that lets you store and retreive custom variables. You can put new objects into the NEObject using the method "put(String key, Object value)." This will store the values and allow them to be sent over to the client or server. This is how you will transmit information back and forth very easily. 

NEPacket is the more advanced and powerful packet which lets you send over any amount of information you want. The NEPacket extends the Packet object and contains a public NEObject variable inside of it. Using the NEObject you can store specific information from the client side and send it to the server side or vice-versa.

Lets go back to the client side and send an NEPacket to the server. We will need to edit and add a few things.

```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		connect();
		if(client.isConnected()) {
			System.out.println("Connected to server.");
			NEPacket nePacket = new NEPacket("myPacket");
			nePacket.vars.put("key1", "Hello");
			nePacket.vars.put("key2", "World");
			client.getServerConnection().sendTcp(nePacket);
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
}
```

You will notice that I removed the Packet object we created earlier and replaced it with a NEPacket but kept the packet name the same. From there we accessed the NEPacket's NEObject variable called "vars" and put in two new values. The first value we put in was "Hello" with "key1" as the key. The second value we put in was "World" with "key2" as the key. After that we sent the packet to the server. 

All that needs to be done now is receive it from the server side. Lets go ahead and make a few changes to MyEventListener in MyModule.

```java
public class MyEventListener implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "myPacket";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket nePacket = (NEPacket) packet;
		String key1 = nePacket.vars.getString("key1");
		String key2 = nePacket.vars.getString("key2");
		System.out.println("Got: " + key1 + " " + key2);
	}

}
```
You will see many changes in the "handlePacket" method. The first thing we did was cast the Packet object to a NEPacket because NEPacket extends Packet. After that we accessed the NEObject and used the "getString" method to get the value of the "key1" key. We then did the same to get the value of the "key2" key.  Finally we printed out what was held in those values. If all went well it should print out "Got; Hello World."

## Listening on the Client
Waiting for packets to be sent to the client works the same way as the server except the event listener's are not contained in a separate module. First we are going to send a response to the client from the server once the "myPacket" packet is received.

```java
public class MyEventListener implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "myPacket";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket nePacket = (NEPacket) packet;
		String key1 = nePacket.vars.getString("key1");
		String key2 = nePacket.vars.getString("key2");
		System.out.println("Got: " + key1 + " " + key2);
		NEPacket responsePacket = new NEPacket("serverPacket");
		responsePacket.vars.put("response", "How are you today?");
		user.sendTCP(responsePacket);
	}

}
```
I added in a few lines that will send a response to the client. First I created a new NEPacket with the name "serverPacket". I put a "response" key into the packet's NEObject with the value of "How are you today?" Finally we sent it back to the user over TCP.

Lets move to the client side now and create a new event listener. I am going to name the class ClientEventListener.

```java
public class ClientEventListener implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "serverPacket";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket nePacket = (NEPacket) packet;
		String response = nePacket.vars.getString("response");
		System.out.println("Got: " + response);
	}

}

```
At this point I hope you can understand what is happening. We are waiting for the "serverPacket" to be received and when it is we retrieve the response value and print it out. All we have to do is add the event listener to the client.

```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		
		addEventListener(new ClientEventListener());
		
		connect();
		if(client.isConnected()) {
			System.out.println("Connected to server.");
			NEPacket nePacket = new NEPacket("myPacket");
			nePacket.vars.put("key1", "Hello");
			nePacket.vars.put("key2", "World");
			client.getServerConnection().sendTcp(nePacket);
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
	public static void main(String[] args) {
		new ClientManager("localhost", 4395, 4395);
	}
	
}
```
In the beginning of the constructor we called the "addEventListener" and passed it a new ClientEventListener instance. Now if you re-export the MyModule and run the server and client the server should print "Got: Hello World" and then the client should print "Got: How are you today?"

## Responses and Requests
Requests and responses work together to send information from the client side and receive it from the server side. You may be wondering what the difference is between that and just sending packets back and forth. Well response and requests are predefined and can't just be created during runtime like normal packets can. This is useful when you need to send information in many different places the same exact way. Examples of this are JoinZoneRequests and JoinRoomRequests which are already defined in the library and will be explained later. Requests are created and sent from the client side and Responses are created and receive requests on the server side. 

To create a new request you need to extend the abstract class BaseRequest. This class contains an NEPacket to be sent and 3 abstract methods that will be inherited. Lets create a new request by creating a new class on the client side called AddRequest.

```java
public class AddRequest extends BaseRequest {

	private int a, b;
	
	public AddRequest(int a, int b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public String getName() {
		return "addRequest";
	}

	@Override
	protected void validate() throws NEException {
		if (a == 0 || b == 0)
			throw new NEException("A or B can't be set to 0.");
	}

	@Override
	protected void execute(Connection con) {
		packet.vars.put("a", a);
		packet.vars.put("b", b);
		con.sendTcp(packet);
	}

}
```
You will see the inherited methods "getName()", "validate()", and "execute(Connection con)". You will also see that I created a constructor for the class which asks for two integers as parameters. Creating a constructor is not necessary but I added one because this request will ask the server to add two numbers together. The "getName" method returns the name of the packet that will be sent to the server. This is the name the server will be waiting for. The validate method is called before the execute method is called and is used to check for any possible cases that have not been filled that need to be. If one of the cases is not filled you can throw a new NEException. In this case if "a" or "b" equals zero then it will throw a new exception. In the execute method we put the values "a" and "b" in the packet's NEObject and send it to the connection specified in the parameter.

Now that that is finished lets go to the ClientManager and use it.

```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		
		addEventListener(new ClientEventListener());
		
		connect();
		if(client.isConnected()) {
			System.out.println("Connected to server.");
			AddRequest addRequest = new AddRequest(5, 7);
			try {
				addRequest.send(client.getServerConnection());
			} catch (NEException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
}
```
We removed the NEPacket that was being sent to the server and replaced it with our request. We first created a new AddRequest with 5 and 7 as the two numbers to add together. We then sent the AddRequest using the "send" method. We had to use a try and catch clause because the "validate" method has a possibility to throw a NEEexception. 

When running this it wont do anything because we haven't added a response on the server side so lets do that now. Responses are created by making a class extend the abstract class BaseResponse. It will provide you the same methods as an IEventListener which will be very familiar.  We are going to create a new class in MyModule called AddResponse which will extend BaseResponse 

```java
public class AddResponse extends BaseResponse {

	@Override
	public String getListeningPacketName() {
		return "addRequest";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket nePacket = (NEPacket) packet;
		int a = nePacket.vars.getInt("a");
		int b = nePacket.vars.getInt("b");
		System.out.println(a + " + " + b + " = " + (a + b));
	}

}

```

This is pretty straight forward. It simply waits for the request to come in and adds the number sent in the packet. Now we need to add the response to the module event handler.

```java
public class MyModule extends NEServerModule {

	@Override
	public void init() {
		System.out.println("My Module loaded.");
		addEventListener(new MyEventListener());
		addEventListener(new AddResponse());
	}

}
```
If you re-export and run your server and client the server should print out "5 + 7 = 12".

## Predefined Requests and Responses
