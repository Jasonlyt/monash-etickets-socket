package fb.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientDemo1 {

	public static final int MYPORT = 8188;

	public static void main(String[] args) {
		Socket socket = null;
		try {
			// InetAddress address = null;
			InetAddress address = InetAddress.getByName("localhost");
			socket = new Socket(address, MYPORT);
			// reader为输入流，获得服务器的输出， writer为输出流，供服务器获取客户端数据
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			PrintStream writer = new PrintStream(socket.getOutputStream());

			// 获取一条服务器连通测试信息
			String serverMessage = reader.readLine();
			System.out.println(serverMessage);

			System.out
					.println("Please input your departing city:(e.g Shanghai)");

			Scanner cin = new Scanner(System.in);
			String fromCity = cin.nextLine();
			// if (fromCity.equalsIgnoreCase("quit"))
			// break;

			System.out
					.println("Please input your desternation city:(e.g Melbourne)");
			String toCity = cin.nextLine();

			// if (toCity.equalsIgnoreCase("quit"))
			//break;

			writer.println(fromCity);
			writer.println(toCity);

			// 客户端通过输入流读取服务器送过来的信息，并将其保存在字符串serverMessage中
			String queryResult = reader.readLine();

			// Transforming the result to multi-line, using the Regular
			// Expression
			Pattern pat = Pattern.compile(";");
			String[] rs = pat.split(queryResult);
			for (String str : rs) {
				System.out.println(str);
			}
			cin.close();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
