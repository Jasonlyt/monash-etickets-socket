package fb.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import fb.common.FlightBookingConstants;

public class ProxyServer {
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(FlightBookingConstants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Proxy server is running.");
		
		while(true){
			Socket incoming = null;
			try {
				incoming = server.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			new ProxySocketHandler(incoming).start();
		}
	}
}
