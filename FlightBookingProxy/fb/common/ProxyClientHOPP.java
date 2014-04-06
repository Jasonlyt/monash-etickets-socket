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

public class ProxyClientHOPP implements ProxyClientService {

	protected Socket socket;
	protected BufferedReader reader;
	protected PrintStream writer;
	
	public ProxyClientHOPP(String server,int PORT) throws IOException {
		InetAddress address = InetAddress.getByName(server);
	    socket = null;
	    InputStream inStream = null;
	    OutputStream outStream = null;

	    socket = new Socket(address, PORT);
	    inStream = socket.getInputStream();
	    outStream = socket.getOutputStream();

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
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		if(response!=null){
			String line = null;
			while(true){
				try {
					line = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}if(line.equals(""))
					break;
				list.add(line);
			}
		}
		String[] flightList = new String[list.size()];
		list.toArray(flightList);
		return flightList;
	}

	@Override
	public String[] checkReq(String input) {
		writer.print(FlightBookingConstants.CHECK+" "+input+FlightBookingConstants.CR_LF);
		List<String> list = new ArrayList<String>();
		String line = null;
		
		while(true){
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}if(line.equals(""))
				break;
			if(line.toUpperCase().startsWith(FlightBookingConstants.AC)
					||line.toUpperCase().startsWith(FlightBookingConstants.CEA)
					||line.toUpperCase().startsWith(FlightBookingConstants.QAN)){
				System.out.println(">>>>>"+line);
				list.add(line);
			}
		}
		String[] orderList = new String[list.size()];
		list.toArray(orderList);
		return orderList;
	}

	@Override
	public boolean regReq(String input) {
		writer.print(FlightBookingConstants.REG+" "+input+FlightBookingConstants.CR_LF);
		boolean flag = false;
		String line;
		try {
			line = reader.readLine();
			if(line.equals(FlightBookingConstants.ERROR))
				flag = false;
			else if(line.equals(FlightBookingConstants.SUCCEEDED))
				flag = true;
			else
				System.out.println("Unrecognized result! >>>>"+line+"<<<<");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean orderReq(String str) {
		writer.print(FlightBookingConstants.ORDER+" "+str+FlightBookingConstants.CR_LF);
		String response=null;
		boolean flag = false;	
		
		while(true){
			try {
				response = reader.readLine();
				System.out.println(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(response.equals(FlightBookingConstants.SUCCEEDED)){
				return true;
			}
			else if(response.equals(FlightBookingConstants.ERROR))
				System.out.println("ERROR");
			else if(response.equals(""))
				break;
			else
				System.out.println(response);
		}
		/**
		try {
			response = reader.readLine();
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(response!=null){
			String line = null;
			try {
				line = reader.readLine();
				System.out.println("---->"+line);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(line.equals(FlightBookingConstants.SUCCEEDED)){
				flag = true;
			}else if(line.equals(FlightBookingConstants.ERROR)){
				System.out.println("Read line error!");
			}
		}
		**/
		return flag;
	}

	@Override
	public void quit() {
		writer.print(FlightBookingConstants.QUIT+FlightBookingConstants.CR_LF);
	}

}
