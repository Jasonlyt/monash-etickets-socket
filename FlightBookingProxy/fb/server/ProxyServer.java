package fb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import fb.common.FlightBookingConstants;
import fb.common.ProxyServerHOPP;

public class ProxyServer {

	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(FlightBookingConstants.PORT);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Proxy server is running.");
		while (true) {
			Socket incoming = null;
			try {
				incoming = server.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			new ProxySocketHandler(incoming).start();
		}
	}
}

class ProxySocketHandler extends Thread {

	Socket incoming;
	BufferedReader reader;
	PrintStream writer;
	OutputStream outstream;
	// server-side HOPP
	protected ProxyServerHOPP psHOPP = new ProxyServerHOPP();

	ProxySocketHandler(Socket incoming) {
		this.incoming = incoming;
	}

	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(
					incoming.getInputStream()));
			outstream = incoming.getOutputStream();
			writer = new PrintStream(outstream);
			while (true) {
				try {
					String line = reader.readLine();
					if (line == null)
						break;
					System.out.println("Reveived request: " + line);
					if (line.startsWith(FlightBookingConstants.QUERY))
						queryResponse(losePrefix(line,
								FlightBookingConstants.QUERY));
					else if (line.startsWith(FlightBookingConstants.REG))
						registerResponse(losePrefix(line, FlightBookingConstants.REG));
					else if (line.startsWith(FlightBookingConstants.ORDER))
						orderResponse(losePrefix(line, FlightBookingConstants.ORDER));
					else if (line.startsWith(FlightBookingConstants.CHECK))
						checkResponse(losePrefix(line, FlightBookingConstants.CHECK));
					else if (line.startsWith(FlightBookingConstants.QUIT))
						quit();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			incoming.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void quit() {
		psHOPP.quit();
	}

	private void checkResponse(String str) {
		String[] rs = psHOPP.checkOrders(str);
		for (String line : rs) {
			System.out.println(line+"<<<---");
			writer.print(line + FlightBookingConstants.CR_LF);
		}
		writer.print(FlightBookingConstants.CR_LF);
	}

	private void orderResponse(String str) {
		boolean flag = psHOPP.orderResp(str);
		if(flag)
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
		else
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		writer.print(FlightBookingConstants.CR_LF);
	}

	private void registerResponse(String str) {
		boolean flag = psHOPP.regResp(str);
		if(flag)
			writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
		else
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		writer.print(FlightBookingConstants.CR_LF);
	}

	private void queryResponse(String str) {
		String[] rs = psHOPP.queryResp(str);
		for (String line : rs) {
			System.out.println(line);
			writer.print(line + FlightBookingConstants.CR_LF);
		}
		writer.print(FlightBookingConstants.CR_LF);
	}

	private String losePrefix(String str, String prefix) {
		int index = prefix.length();
		String ret = str.substring(index).trim();
		return ret;
	}
}