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

				// ��װ����������ʹ�÷������ܹ�һ��һ�еĴ�����������
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				OutputStream writer = link.getOutputStream();
				// ��װ�����
				PrintStream ps = new PrintStream(writer);
				ps.println("This is a message from ---a test server---");

				// ͨ����������ÿͻ������������
				String fromCity = reader.readLine();
				String toCity = reader.readLine();
				String queryResult = queryFlightList(fromCity, toCity);

				System.out.println(queryResult);
				// ��д��д������������ͻ��˵���������ȡ
				// �����2�У�������ֻ����һ��
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
