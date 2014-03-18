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
			// readerΪ����������÷������������ writerΪ�����������������ȡ�ͻ�������
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			PrintStream writer = new PrintStream(socket.getOutputStream());

			// ��ȡһ����������ͨ������Ϣ
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

			// �ͻ���ͨ����������ȡ�������͹�������Ϣ�������䱣�����ַ���serverMessage��
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
