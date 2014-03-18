package fb.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientDemo2 {

	public static final int PORT = 8188;

	public static void main(String[] args) {
		System.out.println("Client is launching...");
		BufferedReader reader = null;
		PrintStream writer = null;
		Socket socket = null;
		Scanner cin = null;
		
		try {
			InetAddress address = InetAddress.getByName("localhost");
			socket = new Socket(address, PORT);
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer = new PrintStream(socket.getOutputStream());

			cin = new Scanner(System.in);
			while (true) {
				String menu = "Input 1 or 2 or 3 to choose the operation, input 'exit' to quit the system.\n"
						+ "[1] Query a flight which could be ordered.(Depart,Dest).\n"
						+ "[2] Order a flight based on the flight number.\n"
						+ "[3] Check the flight which had been ordered.";

				System.out.println(menu);
				String op = cin.nextLine();
				if(op!=null){
					switch (op) {
					case "1":
						queryFlight(writer, reader, cin);
						break;
					case "2":
						orderFlight(writer, reader, cin);
						break;
					case "3":
						checkOrders(writer, reader, cin);
						break;
					case "exit":
						System.exit(0);
					default:
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				cin.close();
				writer.close();
				reader.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	private static void checkOrders(PrintStream writer, BufferedReader reader,
			Scanner cin) {
		writer.println("checkOrders");
		System.out.println("Please input your fullname:");
		String fullname = cin.nextLine();
		writer.println(fullname);
		System.out.println("The flight(s) you ordered is(are):");
		
		try {
			String result = reader.readLine();
			Pattern pat = Pattern.compile(";");
			String[] rs = pat.split(result);
			for (String str : rs) {
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void orderFlight(PrintStream writer, BufferedReader reader,
			Scanner cin) {
		writer.println("orderFlight");
		System.out.println("Which FLIGHT do you want to order?");
		String fid = cin.nextLine();
		System.out.println("Please input your real name:");
		String fullname = cin.nextLine();
		System.out.println("Please input your phone number:");
		String phone = cin.nextLine();
		System.out.println("Please input your E-mail:");
		String email = cin.nextLine();
		System.out.println("Please input your CreditNumber:");
		String creditcard = cin.nextLine();
		System.out.println("Your order detail is: \n" + fid + "\n" + fullname
				+ "\n" + phone + "\n" + email + "\n" + creditcard);
		writer.println(fid);
		writer.println(fullname);
		writer.println(phone);
		writer.println(email);
		writer.println(creditcard);
	}

	private static void queryFlight(PrintStream writer, BufferedReader reader,
			Scanner cin) {
		writer.println("queryFlight");

		System.out.println("Please input your departing city:(e.g Shanghai)");
		String fromCity = cin.nextLine();
		System.out.println("Please input your destnation city:(e.g Melbourne)");
		String toCity = cin.nextLine();
		writer.println(fromCity);
		writer.println(toCity);
		try {
			String result = reader.readLine();

			Pattern pat = Pattern.compile(";");
			String[] rs = pat.split(result);
			for (String str : rs) {
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
