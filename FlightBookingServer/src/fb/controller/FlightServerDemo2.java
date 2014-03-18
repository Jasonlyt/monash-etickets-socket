package fb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fb.dao.FlightDao;
import fb.dao.OrderDao;
import fb.dao.impl.FilghtDaoImpl;
import fb.dao.impl.OrderDaoImpl;
import fb.entity.Flight;
import fb.entity.Order;

/**
 * 
 * @author IBM
 * 
 */
public class FlightServerDemo2 {

	public static final int PORT = 8188;

	public static void main(String[] args) {
		ServerSocket server = null;
		System.out.println("ServerSocket begin----");
		int num = 0;

		try {
			server = new ServerSocket(PORT);

			while (true) {
				num++;
				Socket socket = server.accept();
				new Thread(new ServerThread(socket), "Client" + num).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
				System.out.println("Serversocket closed!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * 
 * @author IBM
 * 
 */
class ServerThread implements Runnable {
	Socket socket = null;

	public ServerThread(Socket socket) {
		System.out.println("Create a new ServerThread...");
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		PrintWriter writer = null;

		try {
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());
			while (true) {
				/**
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				**/
				// 读取客户端发送过来的命令，通过该命令决定服务器的相关操作
				String cmd = reader.readLine();
				if (cmd != null) {
					switch (cmd) {
						case "queryFlight":
							System.out.println("--->queryFlight");
							queryFlight(writer, reader);
							break;
						case "orderFlight":
							System.out.println("--->orderFlight");
							orderFlight(writer, reader);
							break;
						case "checkOrders":
							System.out.println("--->checkOrders");
							checkOrders(writer, reader);
							break;
						default:
							break;
					}
				}
				System.out.println("Waiting from command from client...");
				doWriter(writer);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkOrders(PrintWriter writer, BufferedReader reader) {
		try {
			String fullname = reader.readLine();
			OrderDao dao = new OrderDaoImpl(); 
			List<?> orderList = dao.checkOrders(fullname);
			String msg = null;
			for (int i = 0; i < orderList.size(); i++) {
				Order o = (Order) orderList.get(i);
				msg += o.getFid() + " phone:"+ o.getPhone() + " email:"+o.getEmail()+" creditcard:"+o.getCreditcard()+";";
			}
			writer.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void orderFlight(PrintWriter writer, BufferedReader reader) {
		Order order = new Order();

		try {
			order.setFid(reader.readLine());
			order.setFullname(reader.readLine());
			order.setPhone(reader.readLine());
			order.setEmail(reader.readLine());
			order.setCreditcard(reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		OrderDao dao = new OrderDaoImpl();
		if (dao != null) {
			if (dao.saveOrder(order)) {
				dao.updateTickets(order.getFid());
				System.out.println("Update success");
			}
		}
	}

	private void queryFlight(PrintWriter writer, BufferedReader reader) {
		try {
			String fromCity = reader.readLine();
			String toCity = reader.readLine();
			String queryResult = queryFlightList(fromCity, toCity);
			writer.println(queryResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doWriter(PrintWriter writer) {
		writer.print("Welcome you client.");
		writer.flush();
	}

	/**
	 * check the remaining flight tickets
	 * 
	 * @param fromCity
	 * @param toCity
	 */
	private static String queryFlightList(String fromCity, String toCity) {
		FlightDao dao = new FilghtDaoImpl();
		List<?> flightList = dao.queryFlightList(fromCity, toCity);
		String result = build(flightList);
		return result;
	}

	/**
	 * Transform the List to String
	 * 
	 * @param flightList
	 * @return
	 */
	private static String build(List<?> flightList) {
		String msg = "";
		for (int i = 0; i < flightList.size(); i++) {
			Flight f = (Flight) flightList.get(i);
			msg += f.getFid() + ":" + f.getAirline_comp() + " remaining have "
					+ f.getTickets() + " tickets;";
		}
		return msg;
	}

}