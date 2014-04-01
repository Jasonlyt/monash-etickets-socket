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
						+ FlightBookingConstants.QUERY + " | "
						+ FlightBookingConstants.REG + " | "
						+ FlightBookingConstants.ORDER + " | "
						+ FlightBookingConstants.CHECK + " | Enter "
						+ FlightBookingConstants.QUIT + " to exit.");
				System.out.println("Enter request: ");
				
				line = console.readLine();
				System.out.println("Request was " + line);
			} catch (IOException e) {
				clientHOPP.quit();
				e.printStackTrace();
				System.exit(1);
			}

			if (line.toUpperCase().startsWith(FlightBookingConstants.QUERY)) {
				// query <from,to>
				query(losePrefix(line, FlightBookingConstants.QUERY));
			} else if (line.toUpperCase().startsWith(FlightBookingConstants.REG)) {
				// reg <airline,username,phone,email,creditcard>
				register(losePrefix(line, FlightBookingConstants.REG));
			} else if (line.toUpperCase().startsWith(FlightBookingConstants.ORDER)) {
				// order <fid1,fid2,username>
				order(losePrefix(line, FlightBookingConstants.ORDER));
			} else if (line.toUpperCase().startsWith(FlightBookingConstants.CHECK)) {
				// check <airline,username>
				check(losePrefix(line,FlightBookingConstants.CHECK));
			} else if (line.equalsIgnoreCase(FlightBookingConstants.QUIT)) {
				quit();
			} else {
			}
		
		}
	}
	

	private void check(String input) {
		String[] list = clientHOPP.CheckReq(input);
		for (String str : list) {
			System.out.println(str);
		}
	}

	private void register(String input) {
		if(clientHOPP.regReq(input)){
			System.out.println("Register your infomation successfully!");
		}else
			System.out.println("Failed");;
	}

	private void order(String input) {
		if(clientHOPP.orderReq(input))
			System.out.println("ORDER successfully");
		else
			System.out.println("Failed");
	}

	private void query(String input) {
		String[] list = clientHOPP.queryReq(input);
		listOutput(list);
	}

	
	private void quit() {
		clientHOPP.quit();
		System.out.println("You have quit the system successfully!");
	}
	/**
	 * Public operation: Output the specified list
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
	 * Public operation: Given that the string starts with the prefix, get rid of the prefix
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
