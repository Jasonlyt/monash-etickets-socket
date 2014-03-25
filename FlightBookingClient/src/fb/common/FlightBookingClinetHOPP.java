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
	public String[] listReq() {
		writer.print(FlightBookingConstants.LIST+FlightBookingConstants.CR_LF);
		List<String> list = new ArrayList<String>();
		String line = null;
		while(true){
			try {
				line = reader.readLine();
			} catch (IOException e) {
				break;
			}
			if(line.equals(""))
				break;
			list.add(line);
		}
		String[] flightList = new String[list.size()];
		list.toArray(flightList);
		return flightList;
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
				list.add(line);
			}
		}
		String[] flightList = new String[list.size()];
		list.toArray(flightList);
		return flightList;
	}

	@Override
	public String[] timeReq(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean orderReq(String fid) {
		// TODO Auto-generated method stub
		return false;
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

}
