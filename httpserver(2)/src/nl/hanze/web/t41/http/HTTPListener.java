package nl.hanze.web.t41.http;

import java.net.ServerSocket;
import java.net.Socket;

public class HTTPListener {
	private int portnumber;	
	private HTTPHandler httpHandler;
	
	public HTTPListener(int port, HTTPHandler hh) throws Exception {
		if (port < HTTPSettings.PORT_MIN || port > HTTPSettings.PORT_MAX) 
			throw new Exception("Invalid TCP/IP port, out of range");
		this.portnumber=port;
		this.httpHandler=hh;
	}
	
	public void startUp() throws Exception {
		ServerSocket servsock=new ServerSocket(portnumber);
		System.out.println("Server started");
		System.out.println("Waiting requests at port " + portnumber);
		
		while (true) {
			Socket s=servsock.accept();
			httpHandler.handleRequest(s.getInputStream(), s.getOutputStream());
			s.close();
		}		
	}
}
