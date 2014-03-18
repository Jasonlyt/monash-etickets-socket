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
			 * 服务器等待客户端的请求，若收到请求则返回一个Socket对象
			 * 用于封装客户端请求
			 */
			Socket link = server.accept();
			/*
			 * 下面两个方法分别用于得到Socket对象的输入流对象和输出流对象
			 * 向输出流写内容，另一端可以通过输入流读取
			 */
			InputStream is = link.getInputStream();
			// 包装输入流对象，使得服务器能够一行一行的处理输入数据
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			OutputStream os = link.getOutputStream();
			
			/*
			 * 服务器端向输出流对象写入“hello，well”
			 * 客户端可以使用输入流对象读取这些内容 
			 */
			os.write("hello, well".getBytes());
			
			byte buf[] = new byte[1024];
			/*
			 * 读取客户端发送过来的输入流对象
			 * 并将其保存到buf数组中
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
