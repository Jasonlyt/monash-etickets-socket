package fb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fb.dao.FlightDao;
import fb.dao.impl.FlightDaoImpl;
import fb.entity.Flight;

public class CeaServer {
	public static void main(String[] args) {
		ServerSocket s = null;
		try {
			s = new ServerSocket(FlightBookingConstants.PORT2);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("CHINA EASTERN AIRLINES Server is running");

		while (true) {
			Socket incoming = null;
			try {
				incoming = s.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			new CeaHandler(incoming).start();
		}
	}
}

class CeaHandler extends Thread {
	Socket incoming;
	BufferedReader reader;
	PrintStream writer;

	public CeaHandler(Socket incoming) {
		this.incoming = incoming;
	}

	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(
					incoming.getInputStream()));
			writer = new PrintStream(incoming.getOutputStream());
			writer.print("CEA Server send response!"
					+ FlightBookingConstants.CR_LF);

			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				System.out.println("Received request: " + line);
				if (line.equalsIgnoreCase(FlightBookingConstants.LIST))
					listRequest();
				else if (line.startsWith(FlightBookingConstants.QUERY))
					queryResponse(losePrefix(line, FlightBookingConstants.QUERY));
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

	private void queryResponse(String cities) {
		String[] city = cities.split(" ");
		if (city.length == 2) {
			FlightDao dao = new FlightDaoImpl();
			List<?> list = dao.queryFlightList(city[0], city[1]);
			if (list == null) {
				writer.print(FlightBookingConstants.ERROR
						+ FlightBookingConstants.CR_LF);
			}
			for (Object o : list) {
				String msg = ((Flight) o).getFid() + ":"
						+ ((Flight) o).getAirline_comp();
				System.out.println(msg);
				writer.print(msg + FlightBookingConstants.CR_LF);
			}
			writer.print(FlightBookingConstants.CR_LF);
		} else {
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		}
	}

	private String losePrefix(String str, String prefix) {
		int index = prefix.length();
		String ret = str.substring(index).trim();
		return ret;
	}

	public void listRequest() {
		FlightDao dao = new FlightDaoImpl();
		List<?> list = dao.listFlights();
		if (list == null) {
			writer.print(FlightBookingConstants.ERROR
					+ FlightBookingConstants.CR_LF);
		}
		for (Object f : list) {
			String msg = ((Flight) f).getFid() + ":"
					+ ((Flight) f).getAirline_comp();
			System.out.println(msg);
			writer.print(msg + FlightBookingConstants.CR_LF);
		}
		writer.print(FlightBookingConstants.CR_LF);
	}
}