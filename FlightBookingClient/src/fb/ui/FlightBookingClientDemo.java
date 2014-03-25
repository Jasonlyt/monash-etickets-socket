package fb.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import fb.common.FlightBookingConstants;

public class FlightBookingClientDemo {

	private final static String UI_LIST = "list";
	private final static String UI_QUERY = "query";
	private final static String UI_TIME = "time";
	private final static String UI_ORDER = "order";
	private final static String UI_QUIT = "quit";

	protected BufferedReader reader;
	protected PrintStream writer;
	protected Socket socket;
	protected BufferedReader console;

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: Client address");
			return;
		}
		FlightBookingClientDemo client = new FlightBookingClientDemo(args[0]);
		client.loop();
	}

	

	/**
	 * constructor with one parameter
	 * 
	 * @param server
	 */
	public FlightBookingClientDemo(String server) {
		
		InetAddress address = null;
		try {
			address = InetAddress.getByName(server);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		}

		socket = null;

		try {
			socket = new Socket(address, FlightBookingConstants.PORT);
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer = new PrintStream(socket.getOutputStream());

			console = new BufferedReader(new InputStreamReader(System.in));

		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		
		
		/**
		 * this is the first message that sends from proxy_server
		 */
		try {
			System.out.println(reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			String line = null;
			try {
				System.out.println("===============");
				System.out.println("Enter request: ");
				line = console.readLine();
				System.out.println("Request was " + line);
			} catch (IOException e) {
				e.printStackTrace();
				exit();
			}

			if (line.equalsIgnoreCase(UI_LIST)) {
				// list the available flights
				listRequest();
			} else if (line.toLowerCase().startsWith(UI_QUERY)) {
				// query<from,to>
				queryRequest(losePrefix(line, UI_QUERY));
			} else if (line.toLowerCase().startsWith(UI_TIME)) {
				// time<start,back>
			} else if (line.toLowerCase().startsWith(UI_ORDER)) {
				// order<fid,username>
				orderRequest(losePrefix(line, UI_ORDER));
			} else if (line.equalsIgnoreCase(UI_QUIT)) {
				exit();
			} else {
				System.out.println("Unrecognised command");
			}
		}

	}
	
	private void loop() {
		// TODO Auto-generated method stub
		
	}
	
	private void orderRequest(String str) {
		writer.print(FlightBookingConstants.ORDER+" "+str+FlightBookingConstants.CR_LF);
		String response = null;
		
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if(response.equals(FlightBookingConstants.ERROR))
			System.out.println("Error in ORDER request");
		else{
			System.out.println("ORDER successfully, your FID: "+str);
		}
	}

	private void queryRequest(String cities) {
		writer.print(FlightBookingConstants.QUERY+" "+cities+FlightBookingConstants.CR_LF);
		String response = null;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if(response.equals(FlightBookingConstants.ERROR)){
			System.out.println("Error in QUERY request");
		}
		else{
			System.out.println("The available flights are:");
		
			String line = null;
			while(true){
				try {
					line = reader.readLine();
				} catch (IOException e) {
					break;
				}
				if(line.equals(""))
					break;
				System.out.println(line);
			}
		}
	}

	private void listRequest() {
		writer.print(FlightBookingConstants.LIST+FlightBookingConstants.CR_LF);
		System.out.println("Listing the availiable flights: ");
		String line = null;
		while(true){
			try {
				line = reader.readLine();
			} catch (IOException e) {
				break;
			}
			if(line.equals(""))
				break;
			System.out.println(line);
		}
	}

	private void exit() {
		try {
			writer.print(FlightBookingConstants.QUIT
					+ FlightBookingConstants.CR_LF);
			reader.close();
			writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	/**
	 * Given that the string starts with the prefix, get rid of the prefix
	 * and any whitespace
	 * @param str
	 * @param prefix
	 * @return
	 */
	public String losePrefix(String str, String prefix){
		int index = prefix.length();
		String ret = str.substring(index).trim();
		return ret;
	}
}
