package fb.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FlightBookingClinetHOPP implements FlightBookingService {
	
	protected Socket  mySocket;
	protected BufferedReader reader;
	protected PrintStream writer;
	
	public FlightBookingClinetHOPP(String server) throws IOException{
		InetAddress address = InetAddress.getByName(server);
	    mySocket = null;
	    InputStream inStream = null;
	    OutputStream outStream = null;

	    mySocket = new Socket(address, FlightBookingConstants.PORT);
	    inStream = mySocket.getInputStream();
	    outStream = mySocket.getOutputStream();

	    reader = new BufferedReader(new InputStreamReader(inStream));
	    writer = new PrintStream(outStream);
	}
	

	@Override
	public String[] queryReq(String input) {
		writer.print(FlightBookingConstants.QUERY+" "+input+FlightBookingConstants.CR_LF);
		List<String> list = new ArrayList<String>();
		String response = null;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if(response.equals(FlightBookingConstants.ERROR)){
			System.out.println("Error in QUERY request");
		}
		else{
			//System.out.println("The available flights are:");
			String line = null;
			while(true){
				try {
					line = reader.readLine();
				} catch (IOException e) {
					break;
				}
				if(line.equals("")){
					try {
						response = reader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(response.equals(FlightBookingConstants.ERROR)){
						System.out.println("Error in QUERY request");
					} else {
						while(true){
							try {
								line = reader.readLine();
							} catch (IOException e) {
								e.printStackTrace();
							} if(line.equals("")){
								try {
									response = reader.readLine();
								} catch (IOException e) {
									e.printStackTrace();
								}
								if(response.equals(FlightBookingConstants.ERROR)){
									System.out.println("ERROR in QUERY request");
								} else {
									while(true){
										try {
											line = reader.readLine();
										} catch (IOException e) {
											e.printStackTrace();
										} if (line.equals(""))
											break;
										list.add(line);
									}
								}
								break;
							}
							list.add(line);
						}
					}
					break;
				}
				list.add(line);
			}
		}
		String[] flightList = new String[list.size()];
		list.toArray(flightList);
		return flightList;
	}

	@Override
	public boolean regReq(String input) {
		writer.print(FlightBookingConstants.REG+" "+input+FlightBookingConstants.CR_LF);
		String response = null;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(response.equals(FlightBookingConstants.SUCCEEDED))
			return true;
		else if(response.equals(FlightBookingConstants.ERROR))
			return false;
		else{
			System.out.println("Unrecogized error!----->reg");
			return false;
		}
			
	}

	// order fid1 fid2 username
	public boolean orderReq(String str) {
		writer.print(FlightBookingConstants.ORDER+" "+str+FlightBookingConstants.CR_LF);
		String response = null;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if(response.equals(FlightBookingConstants.SUCCEEDED))
			return true;
		else{
			return false;
		}
	}

	@Override
	public void quit() {
		try {
			writer.print(FlightBookingConstants.QUIT+FlightBookingConstants.CR_LF);
			reader.close();
			writer.close();
			mySocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String[] CheckReq(String input) {
		writer.print(FlightBookingConstants.CHECK+" "+input+FlightBookingConstants.CR_LF);
		String response = null;
		try {
			response = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if(response.equals(FlightBookingConstants.ERROR)){
			System.out.println("Error in CHECK request");
		}else {
			String[] order = response.split(":");
			return order;
		}
		return null;
	}

}
