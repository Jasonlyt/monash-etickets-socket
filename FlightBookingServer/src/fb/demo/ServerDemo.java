package fb.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerDemo {
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8188);
			
			/*
			 * �������ȴ��ͻ��˵��������յ������򷵻�һ��Socket����
			 * ���ڷ�װ�ͻ�������
			 */
			Socket link = server.accept();
			/*
			 * �������������ֱ����ڵõ�Socket�������������������������
			 * �������д���ݣ���һ�˿���ͨ����������ȡ
			 */
			InputStream is = link.getInputStream();
			// ��װ����������ʹ�÷������ܹ�һ��һ�еĴ�����������
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			OutputStream os = link.getOutputStream();
			
			/*
			 * �������������������д�롰hello��well��
			 * �ͻ��˿���ʹ�������������ȡ��Щ���� 
			 */
			os.write("hello, well".getBytes());
			
			byte buf[] = new byte[1024];
			/*
			 * ��ȡ�ͻ��˷��͹���������������
			 * �����䱣�浽buf������
			 */
			int len = is.read(buf);
			
			System.out.println(new String(buf,0,len));
			is.close();
			os.close();
			link.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
