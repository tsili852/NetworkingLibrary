NetworkingLibrary
=================

This networking library that I developed provides a very simple API for utilization of TCP and UDP effectively and efficiently. It is built ontop of a networking wrapper I developed which handles all server and client communication over TCP and UDP.


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

This will begin running a basic server on the TCP and UDP ports specified in the constructor. 
