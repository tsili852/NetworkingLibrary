Networking Library
=================

This networking library that I developed provides an API for utilization of TCP and UDP effectively and efficiently. It is an event-based library that makes listening for events on the server or client very simple. It is built ontop of a networking wrapper I developed which handles all server and client communication over TCP and UDP. To keep things organized and make applications more extendable the library is also modular.


This networking library runs on Desktop and on Android.

## Table of Contents
- [Starting A Server](#starting-a-server)
- [Zones and Rooms](#zones-and-rooms)
- [Starting A Client](#starting-a-client)
- [Modularity - Creating A Module](#modularity---creating-a-module)
- [Event Listeners and Event Handlers](#event-listeners-and-event-handlers)
- [NEPacket and NEObject](#nepacket-and-neobject)
- [Listening on the Client](#listening-on-the-client)
- [Responses and Requests](#responses-and-requests)
- [Predefined Event Names](#predefined-event-names)
- [Predefined Client-Side Events](#predefined-client---side-events)
- [Predefined Requests and Responses](#predefined-requests-and-responses)
- [Joining A Room and Zone](#joining-a-room-and-zone)
- [Custom Packets](#custom-packets)
- [Conclusion](#conclusion)

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

## Predefined Event Names
There are many predefined event names in the library that take care of some functionalities like joining a room. The NEEvent class contains all of the predefined packet names that are either sent to a client or used on the server:

- USER_JOIN_ROOM : Sent to a client when a new user joins their room.
- USER_LEAVE_ROOM : Sent to a client when a user leaves their room.
- USER_VARIABLE_UPDATE : Sent to a client when a user's variable is requested to be changed.
- ON_ROOM_JOIN : Sent to a client that requested to join a room and was successful.
- ON_ROOM_JOIN_ERROR : Sent to a client that requested to join a room and was unsuccessful.
- ON_ZONE_JOIN : Sent to a client that requested to join a zone and was successful.
- ON_ZONE_JOIN_ERROR : Sent to a client that requested to join a zone and was unsuccessful.
- NEW_USER_CONNECTED : Called on server when a new user connects.
- ERROR_MESSAGE : Not an event, a key used to get the error value of the ON_ROOM_JOIN_ERROR event and ON_ZONE_JOIN_ERROR event.


## Predefined Client-Side Events
There are also a few predefined client-side events that are included in the library. The events have been defined because they are either called a lot by the server or they will need to be used the same way in every application you make.

- OnJoinRoomEvent : Takes care of setting your user variables correctly on the client side
- OnUserVariableUpdateEvent : Called when a variable is requested to be changed on the client side using the event name NEEvent.USER_VARIABLE_UPDATE
- UserJoinEvent : Called when a user joins the clients room. Adds the user to the room on the client side.
- OnUserLeaveEvent : Called when a user leaves the clients room. Removes the user from the room on the client side.

You don't need to know much about these events. I just wanted to tell you about them so that you are aware of how some client-sided information is getting changed.

## Predefined Requests and Responses
Just like there are predefined events there are 2 predefined requests and responses:

- JoinZoneRequest and JoinZoneResponse
- JoinRoomRequest and JoinRoomResponse

They do exactly what they say. When a new JoinZoneRequest is created you must specify the zone name that is desired to be joined, the desired username, and the password. The response will handle the information and if the zone exists and the username is not already connected, it will be successful and send you an ON_ZONE_JOIN event. If it is not successful it will send you an ON_ZONE_JOIN_ERROR event. 

When creating and sending a new JoinRoomRequest you must specify the room name. The response will handle the information sent and if the user is in a zone, the zone contains the room, and the max amount of users isn't reached, it will send you a ON_ROOM_JOIN event. Otherwise it will send you a ON_ROOM_JOIN_ERROR event.


## Joining A Room and Zone
When wanting to join a room the first thing that needs to be done is to request to connect to the zone that the room is in. We are going to create a new JoinZoneRequest in the ClientManager once connected.
```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		
		addEventListener(new ClientEventListener());
		
		connect();
		if(client.isConnected()) {
			JoinZoneRequest jzr = new JoinZoneRequest("MyZone", "MyUsername", "MyPassword");
			try {
				jzr.send(client.getServerConnection());
			} catch (NEException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
}
```
This creates a new JoinZoneRequests with the specified zone name, username, and password, and sends it to the server. This time we are asking to join the zone "MyZone" which we created earlier. The server's JoinZoneResponse will handle the information and if it is successful it will send back an ON_ZONE_JOIN event. If it isn't it will send an ON_ZONE_JOIN_ERROR event. Lets create an event listener to wait for those two events on the client.

```java
public class ZoneJoinEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ZONE_JOIN.toString();
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		//Joined successfully. Lets request to join the room.
		System.out.println("Joined zone successfully.");
		JoinRoomRequest jrr = new JoinRoomRequest("MyRoom");
		try {
			jrr.send(user.getSession().getConnection());
		} catch (NEException e) {
			e.printStackTrace();
		}
	}

}
```

If this event is called then we automatically want to join the room so we send a request to join the room "MyRoom". Now lets just check to see if there is an error.

```java
public class ZoneJoinErrorEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ZONE_JOIN_ERROR.toString();
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket nePacket = (NEPacket) packet;
		String error = nePacket.vars.getString(NEEvent.ERROR_MESSAGE.toString());
		JOptionPane.showMessageDialog(null, error);
	}

}
```

If there is an error we are going to make a message dialog show up with the reason why. Finally lets add these events to the event handler:

```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		
		addEventListener(new ClientEventListener());
		addEventListener(new ZoneJoinEvent());
		addEventListener(new ZoneJoinErrorEvent());
		
		connect();
		if(client.isConnected()) {
			JoinZoneRequest jzr = new JoinZoneRequest("MyZone", "MyUsername", "MyPassword");
			try {
				jzr.send(client.getServerConnection());
			} catch (NEException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
}
```
If you run the server and then run the client you will see "Joined zone successfully" printed out. Now lets handle joining a room. It's the same thing as the zone so lets create listeners for the ON_ROOM_JOIN and ON_ROOM_JOIN_ERROR events:

```java
public class RoomJoinEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ROOM_JOIN.toString();
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		Room r = (Room) ne.vars.getObject("room");
        	System.out.println("Joined " + r.getName() + " successfully.");
	}

}
```

The Room that was joined is sent to the event with it's key as "room". For the error listener:
```java
public class RoomJoinErrorEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ROOM_JOIN_ERROR.toString();
	}

	@Override
	public void handlePacket(User user, Packet packet) {
        NEPacket nePacket = (NEPacket) packet;
		String error = nePacket.vars.getString(NEEvent.ERROR_MESSAGE.toString());
		JOptionPane.showMessageDialog(null, error);
	}

}
```

Lastly lets add these listeners to the event handler:

```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		
		addEventListener(new ClientEventListener());
		addEventListener(new ZoneJoinEvent());
		addEventListener(new ZoneJoinErrorEvent());
		addEventListener(new RoomJoinEvent());
		addEventListener(new RoomJoinErrorEvent());
		
		connect();
		if(client.isConnected()) {
			JoinZoneRequest jzr = new JoinZoneRequest("MyZone", "MyUsername", "MyPassword");
			try {
				jzr.send(client.getServerConnection());
			} catch (NEException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can not connect to server.");
		}
	}
	
}
```

Now, if you have followed everything correctly, if you start the server and then the client you should see the client print out "Joined zone succesfully." You should then see "Joined MyRoom successfully." If you try and connect another new client you should get an error because you already connected to the Zone with that username.

## Custom Packets
Say that you want to create a new packet that wants to hold specific information. Maybe a PositionPacket which will hold the x and y position specifically. There are a few things you need to do before you can start sending the packet back and forth from the server to client.

Lets start by creating our PositionPacket class.
```java
public class PositionPacket extends Packet implements Serializable {

	private static final long serialVersionUID = 1L;

	public int x, y;
	
	public PositionPacket() {
		super("positionPacket");
	}
	
	public PositionPacket(int x, int y) {
		super("positionPacket");
		this.x = x;
		this.y = y;
	}
	
}
```

The number one thing that you have to do for every packet you want to send over is extend the Packet class or a subclass of it. If your class doesn't extend it then the server will not recognize it as an event. You must also implement the Serializable interface so that your class can be transfered over streams. The "serialVersionUID" variable must be in every class that implements Serializable and Eclipse can import that variable for you. Also another important thing is a Packet must ALWAYS have a default constructor. So a constructor with no variables. If it doesn't then it will not be able to be sent over. One final important thing is if they are in separate projets they must be in the same exact package on both sides. If they are not it will throw an error.

When you want to send this packet over to the server and client, if the server and client projects are separate you need to have the exact same file in both projects. If you only have the packet's class on the server side and you send it to the client side which doesn't have it, the client won't recognized the object and it will throw an error.


Lets send this packet over to the server from the client. We are going to modify the ClientManager to do so.
```java
public class ClientManager extends NEClientManager {

	public ClientManager(String ip, int tcpPort, int udpPort) {
		super(ip, tcpPort, udpPort);
		
		addEventListener(new ClientEventListener());
		addEventListener(new ZoneJoinEvent());
		addEventListener(new ZoneJoinErrorEvent());
		addEventListener(new RoomJoinEvent());
		addEventListener(new RoomJoinErrorEvent());
		
		connect();
		if(client.isConnected()) {
			JoinZoneRequest jzr = new JoinZoneRequest("MyZone", "MyUsername", "MyPassword");
			try {
				jzr.send(client.getServerConnection());
			} catch (NEException e) {
				e.printStackTrace();
			}
			PositionPacket pp = new PositionPacket(20, 10);
			client.getServerConnection().sendTcp(pp);
		} else {
			System.out.println("Can not connect to server.");
		}
	}

}
```
After the JoinZoneRequest we create a new PositionPacket with x equal to 20 and y equal to 10 and we send it to the server. Now lets create a listener for it on the server side in MyModule.

```java
public class PositionEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "positionPacket";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		PositionPacket pp = (PositionPacket) packet;
		System.out.println(pp.x + " " + pp.y);
	}

}
```
Just like every event listener we must register it. We are going to do so in our module.

```java
public class MyModule extends NEServerModule {

	@Override
	public void init() {
		System.out.println("My Module loaded.");
		addEventListener(new MyEventListener());
		addEventListener(new AddResponse());
		addEventListener(new PositionEvent());
	}

}
```

Now if you re-export the module and run the server and client the server should print out "20 10".


## Connecting To a Database
Databases allow you to save information and retrieve it very efficiently and quickly. Every room and zone can have a database loaded onto it which can be used later to perform queries. I will show you how to load a database but performing queries is a whole different subject. There are many tutorials online that can show you how to perform queries and what each will do. 

I use Wamp Server to manage databases using PhpMyAdmin. To load a database make sure Wamp Server is running or another web development environment is. Inside of our room we are going to load a new database. 

```java
public class MyRoom extends Room {

	public MyRoom(String name, Zone parentZone) {
		super(name, parentZone);
		try {
			loadModule("modules/MyModule.jar", "com.net.MyModule");
		} catch (NEException e) {
			e.printStackTrace();
		}
		try {
			setDatabase(new Database("jdbc:mysql://localhost/", "MyDatabase", "root", ""));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
}
```

We added a new line of code surrounded by a try-catch clause that instantiates a new database and sets it to the room. When a new Database object is instantiated it takes the url to the database as the first parameter, the name of the database as the second parameter, the username of the database as third parameter, and the password of the database as the fourth. If all of the information is correct it will connect to the database and allow you to perform queries through the database object. 

## Conclusion
The library makes connecting to a server, sending packets, and listening for events very easy. Most of the work is done for you and the modularity aspect keeps the code organized and reusable. In the "examples" folder I have included an example of a chat room and registration application. They both connect to a database which is used to put in new user registrations and check to see if they exist when logging in. The check to see if a user exists on the chat room login side can always be changed to allow anyone to login regardless of whether they're registered or not. I'm sure you guys could figure this out (check the JoinZoneResponse on the server side. I removed the standard JoinZoneResponse and replaced it with a custom one.)

If you have any questions please post below! I will be more than happy to assist you in developing your own networked application using this library. You can also fork the repo and send in new pull requests with additions or fixes to the library.

Enjoy!
-Jon R
