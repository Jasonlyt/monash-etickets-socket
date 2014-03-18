package fb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import fb.dao.FlightDao;
import fb.dao.impl.FilghtDaoImpl;
import fb.entity.Flight;

public class FlightServerDemo {
	public static void main(String[] args) {
		ServerSocket server = null;
		// int num = 0;

		try {
			server = new ServerSocket(8188);
			while (true) {
				Socket link = server.accept();
				InputStream is = link.getInputStream();

				// 包装输入流对象，使得服务器能够一行一行的处理输入数据
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				OutputStream writer = link.getOutputStream();
				// 包装输出流
				PrintStream ps = new PrintStream(writer);
				ps.println("This is a message from ---a test server---");

				// 通过输入流获得客户端输入的数据
				String fromCity = reader.readLine();
				String toCity = reader.readLine();
				String queryResult = queryFlightList(fromCity, toCity);

				System.out.println(queryResult);
				// 将写过写入输出流，供客户端的输入流获取
				// 结果有2行，但这里只返回一行
				ps.println(queryResult);

				is.close();
				writer.close();
				link.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	/**
	 * order a ticket
	 * 
	 * @param flightNo
	 *            Flight Number you want to order
	 */
	private static void orderFlight(String flightNo) {

	}

}
