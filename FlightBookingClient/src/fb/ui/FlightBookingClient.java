package fb.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fb.common.FlightBookingClinetHOPP;
import fb.common.FlightBookingConstants;

public class FlightBookingClient {

	protected BufferedReader console;
	protected FlightBookingClinetHOPP clientHOPP;

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: Client address");
			return;
		}
		FlightBookingClient client = new FlightBookingClient(args[0]);
		client.loop();
	}

	/**
	 * constructor with one parameter
	 * @param server
	 */
	public FlightBookingClient(String server) {
		clientHOPP = null;
		try {
			clientHOPP = new FlightBookingClinetHOPP(server);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		console = new BufferedReader(new InputStreamReader(System.in));
	}
	
	
	private void loop() {
		while(true){
			String line = null;
			try {
				System.out.println("==============================");
				System.out.println("Flight Booking request options: "
						+ FlightBookingConstants.LIST + " | "
						+ FlightBookingConstants.QUERY + " | "
						+ FlightBookingConstants.TIME + " | "
						+ FlightBookingConstants.ORDER + " | Enter "
						+ FlightBookingConstants.QUIT + " to exit.");
				System.out.println("Enter request: ");
				
				line = console.readLine();
				System.out.println("Request was " + line);
			} catch (IOException e) {
				clientHOPP.quit();
				e.printStackTrace();
				System.exit(1);
			}

			if (line.equalsIgnoreCase(FlightBookingConstants.LIST)) {
				// list the available flights
				list();
			} else if (line.toUpperCase().startsWith(FlightBookingConstants.QUERY)) {
				// query<from,to>
				query(losePrefix(line, FlightBookingConstants.QUERY));
			} else if (line.toUpperCase().startsWith(FlightBookingConstants.TIME)) {
				// time<start,back>
			} else if (line.toUpperCase().startsWith(FlightBookingConstants.ORDER)) {
				// order<fid,username>
			} else if (line.equalsIgnoreCase(FlightBookingConstants.QUIT)) {
				//exit();
			} else {
			}
		
		}
	}
	
	private void query(String input) {
		String[] list = clientHOPP.queryReq(input);
		listOutput(list);
	}

	private void list() {
		String[] list = clientHOPP.listReq();
		listOutput(list);
	}
	
	/**
	 * Output the specified list
	 * @param list
	 */
	private void listOutput(String[] list) {
		if(list.length == 0){
			System.out.println("No flight available");
		} else {
			System.out.println("The available flights are: ");
			for (String str : list) {
				System.out.println(str);
			}
		}
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
