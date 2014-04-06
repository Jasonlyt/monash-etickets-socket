package fb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fb.common.AirlineServerHOPP;
import fb.common.FlightBookingConstants;
import fb.entity.Flight;
import fb.entity.Order;

public class AirlineServer {
	public static void main(String[] args) {
		ServerSocket s = null;
		String airline = null;
		
		if (args.length != 1) {
			System.err.println("Usage: airline name");
			return;
		}
		
		try {
			if(args[0].equalsIgnoreCase(FlightBookingConstants.CEA)){
				s = new ServerSocket(FlightBookingConstants.PORT_CEA);
				airline = "cea";
				System.out.println("CHINA EASTERN AIRLINES Server is running");
			}
			else if(args[0].equalsIgnoreCase(FlightBookingConstants.AC)){
				s = new ServerSocket(FlightBookingConstants.PORT_AC);
				airline = "ac";
				System.out.println("AIRLINE CHINA Server is running");
			}
			else if(args[0].equalsIgnoreCase(FlightBookingConstants.QAN)){
				s = new ServerSocket(FlightBookingConstants.PORT_QAN);
				airline = "qan";
				System.out.println("QANTAS Server is running");
			}
			else
				System.out.println("Only CEA, AC, and QAN are supported!");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			Socket incoming = null;
			try {
				incoming = s.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			new AirlineHandler(incoming,airline).start();
		}
	}
}

class AirlineHandler extends Thread {
	Socket incoming;
	BufferedReader reader;
	PrintStream writer;
	AirlineServerHOPP hopp = new AirlineServerHOPP();
	String airline;

	public AirlineHandler(Socket incoming,String airline) {
		this.incoming = incoming;
		this.airline = airline;
	}

	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(
					incoming.getInputStream()));
			writer = new PrintStream(incoming.getOutputStream());
			writer.print("Airline Server send response!"
					+ FlightBookingConstants.CR_LF);

			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				System.out.println("Received request: " + line);
				if (line.startsWith(FlightBookingConstants.QUERY))
					queryResponse(losePrefix(line, FlightBookingConstants.QUERY),airline);
				else if (line.startsWith(FlightBookingConstants.REG))
					regResponse(losePrefix(line, FlightBookingConstants.REG));
				else if (line.startsWith(FlightBookingConstants.ORDER))
					orderResponse(losePrefix(line, FlightBookingConstants.ORDER));
				else if (line.startsWith(FlightBookingConstants.CHECK))
					checkResponse(losePrefix(line, FlightBookingConstants.CHECK));
				else if (line.startsWith(FlightBookingConstants.QUIT)){
					quit();
					break;
				}
				
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

	private void quit() {
		//pass
	}

	private synchronized void checkResponse(String str) {
		List<Order> list = hopp.checkOrders(str);
		if (list == null){
			writer.print(FlightBookingConstants.ERROR
					+ FlightBookingConstants.CR_LF);
		} else {
			String msg;
			for (Order order : list) {
				msg = order.getFid();
				System.out.println(msg);
				writer.print(msg+FlightBookingConstants.CR_LF);
			}
			writer.print(FlightBookingConstants.CR_LF);
		}
	}

	private synchronized void regResponse(String str) {
		if(hopp.regResp(str)){
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
		}else{
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		}
		writer.print(FlightBookingConstants.CR_LF);
	}

	private synchronized void orderResponse(String str) {
		if(hopp.orderResp(str)){
			System.out.println(hopp.orderResp(str));
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
		}else{
			System.out.println("----->ERROR");
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		}
		System.out.println("--->crlf");
		writer.print(FlightBookingConstants.CR_LF);
	}

	private synchronized void queryResponse(String cities,String airline) {
		List<Flight> list = hopp.queryResp(cities,airline);
		if (list == null){
			writer.print(FlightBookingConstants.ERROR
					+ FlightBookingConstants.CR_LF);
		} else {
			for (Flight f : list) {
				String msg = f.getFid()+" [Departure]"+f.getDeparting_date()+" [Tickets]"+f.getTickets()+" [price]"+f.getPrice();
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