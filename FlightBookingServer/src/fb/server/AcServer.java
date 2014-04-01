package fb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fb.common.ChildServerHOPP;
import fb.common.FlightBookingConstants;
import fb.entity.Flight;
import fb.entity.Order;

public class AcServer {
	public static void main(String[] args) {
		ServerSocket s = null;
		try {
			s = new ServerSocket(FlightBookingConstants.PORT_AC);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("AIRLINE CHINA Server is running");

		while (true) {
			Socket incoming = null;
			try {
				incoming = s.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			new AcHandler(incoming).start();
		}
	}
}

class AcHandler extends Thread{
	Socket incoming;
	BufferedReader reader;
	PrintStream writer;
	ChildServerHOPP acHOPP = new ChildServerHOPP();
	
	AcHandler(Socket incoming){
		this.incoming = incoming;
	}
	
	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(
					incoming.getInputStream()));
			writer = new PrintStream(incoming.getOutputStream());
			writer.print("AC Server send response!"
					+ FlightBookingConstants.CR_LF);

			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				System.out.println("Received request: " + line);
				if (line.startsWith(FlightBookingConstants.QUERY))
					queryResponse(losePrefix(line, FlightBookingConstants.QUERY));
				else if (line.startsWith(FlightBookingConstants.REG))
					regResponse(losePrefix(line, FlightBookingConstants.REG));
				else if (line.startsWith(FlightBookingConstants.ORDER))
					orderResponse(losePrefix(line, FlightBookingConstants.ORDER));
				else if (line.startsWith(FlightBookingConstants.CHECK))
					checkResponse(losePrefix(line, FlightBookingConstants.CHECK));
				else {
					writer.print(FlightBookingConstants.ERROR
							+ FlightBookingConstants.CR_LF);
				}

			}
			incoming.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void checkResponse(String str) {
		List<Order> list = acHOPP.checkOrders(str);
		String msg = "";
		if(list == null){
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		} else {
			for (Order o : list) {
				msg += o.getFid()+":";
			}
			writer.print(msg+FlightBookingConstants.CR_LF);
			writer.print(msg+FlightBookingConstants.CR_LF);
		}
	}

	private void regResponse(String str) {
		if(acHOPP.regResp(str)){
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
		}else{
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		}
	}

	private void orderResponse(String str) {
		if(acHOPP.orderResp(str)){
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);

		}else{
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);

		}
	}

	private void queryResponse(String cities) {
		List<Flight> list = acHOPP.queryRespFromCeaDB(cities);
		if (list == null){
			writer.print(FlightBookingConstants.ERROR
					+ FlightBookingConstants.CR_LF);
		} else {
			for (Flight f : list) {
				String msg = f.getFid()+" [Departure]"+f.getDeparting_date()+" [Tickets]"+f.getTickets();
				System.out.println(msg);
				writer.print(msg + FlightBookingConstants.CR_LF);
			}
			writer.print(FlightBookingConstants.CR_LF);
		}
	}


	private String losePrefix(String str, String prefix) {
		int index = prefix.length();
		String ret = str.substring(index).trim();
		return ret;
	}
	
}

