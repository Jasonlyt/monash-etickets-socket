package fb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketHandler extends Thread {
	//private final int BUFFER_SIZE = 2048;
	
	/**
	 * incoming used to accept from client
	 * distribute used to send request to each airline
	 */
	Socket incoming;
	BufferedReader reader,ceaReader;
	PrintStream writer,ceaWriter;
	OutputStream outstream;

	private Socket ceaSocket;
	
	public SocketHandler(Socket incoming) {
		this.incoming = incoming;
	}	
	
	@Override
	public void run() {
		try {
			
			InetAddress address = InetAddress.getByName("localhost");
			ceaSocket = new Socket(address,FlightBookingConstants.PORT2);
			
			
			/**
			 * accept part
			 */
			reader = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			outstream = incoming.getOutputStream();
			writer = new PrintStream(outstream);
			writer.println("Flight Booking request options: "
					+ FlightBookingConstants.LIST + " | "
					+ FlightBookingConstants.QUERY + " | "
					+ FlightBookingConstants.TIME + " | "
					+ FlightBookingConstants.ORDER + " | Enter "
					+ FlightBookingConstants.QUIT + " to exit.");
			
			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}
				System.out.println("Received request: " + line);
				
				if(line.startsWith(FlightBookingConstants.LIST)) {
					listRequest();
				} else if (line.startsWith(FlightBookingConstants.QUERY)) {
					queryResponse(losePrefix(line,FlightBookingConstants.QUERY));
				} else if (line.startsWith(FlightBookingConstants.QUIT)){
					break;
				} else {
					writer.print(FlightBookingConstants.ERROR + FlightBookingConstants.CR_LF);
				}
			}
			incoming.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void queryResponse(String cities) {
		try {
			ceaReader = new BufferedReader(new InputStreamReader(ceaSocket.getInputStream()));
			ceaWriter = new PrintStream(ceaSocket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ceaWriter.print(FlightBookingConstants.QUERY+" "+cities+FlightBookingConstants.CR_LF);
		String line = null;
		while(true){
			try {
				line = ceaReader.readLine();
				writer.print(line+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} if(line.equals(""))
				break;
			System.out.println(line);
		}
	}

	private String losePrefix(String str, String prefix) {
		int index = prefix.length();
		String ret = str.substring(index).trim();
		return ret;
		
	}

	private void listRequest() {
		
		try {
			ceaReader = new BufferedReader(new InputStreamReader(ceaSocket.getInputStream()));
			ceaWriter = new PrintStream(ceaSocket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ceaWriter.print(FlightBookingConstants.LIST+FlightBookingConstants.CR_LF);
		//System.out.println("----distribute:listRequest()");
		String line = null;
		
		while(true){
			try {
				line = ceaReader.readLine();
				writer.print(line+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} if(line.equals(""))
				break;
			System.out.println(line);
		}
	}
}