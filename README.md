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

	public MyRoom() {
	}
	
	public MyRoom(String name, Zone parentZone) {
		super(name, parentZone);
	}
	
	public MyRoom(String name, Zone parentZone, RoomSettings roomSettings) {
		super(name, parentZone, roomSettings);
	}
	
}
```
