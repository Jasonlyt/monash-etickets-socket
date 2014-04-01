package fb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import fb.common.FlightBookingConstants;

public class ProxySocketHandler extends Thread {
	
	/**
	 * incoming used to accept from client
	 * distribute used to send request to each airline
	 */
	Socket incoming;
	BufferedReader reader,ceaReader,acReader,qanReader;
	PrintStream writer,ceaWriter,acWriter,qanWriter;
	OutputStream outstream;

	private Socket ceaSocket,acSocket,qanSocket;
	
	public ProxySocketHandler(Socket incoming) {
		this.incoming = incoming;
	}	
	
	@Override
	public void run() {
		try {
			
			InetAddress address = InetAddress.getByName("localhost");
			ceaSocket = new Socket(address,FlightBookingConstants.PORT_CEA);
			acSocket = new Socket(address,FlightBookingConstants.PORT_AC);
			qanSocket = new Socket(address,FlightBookingConstants.PORT_QAN);
			
			reader = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			outstream = incoming.getOutputStream();
			writer = new PrintStream(outstream);

			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}
				System.out.println("Received request: " + line);
				
				if (line.startsWith(FlightBookingConstants.QUERY)) {
					queryResponse(losePrefix(line,FlightBookingConstants.QUERY));
				} else if (line.startsWith(FlightBookingConstants.REG)){
					regResponse(losePrefix(line, FlightBookingConstants.REG));
				} else if (line.startsWith(FlightBookingConstants.ORDER)){
					orderResonse(losePrefix(line,FlightBookingConstants.ORDER));
				} else if (line.startsWith(FlightBookingConstants.CHECK)){
					checkResponse(losePrefix(line,FlightBookingConstants.CHECK));
				} else if (line.startsWith(FlightBookingConstants.QUIT)){
					break;
				} else {
					writer.print(FlightBookingConstants.ERROR + FlightBookingConstants.CR_LF);
				}
			}
			incoming.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkResponse(String str) {
		streamInit();
		
		if(str.toUpperCase().startsWith(FlightBookingConstants.QAN)){
			qanWriter.print(FlightBookingConstants.CHECK+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				line = qanReader.readLine();
				line = qanReader.readLine();
				if(line.equals(FlightBookingConstants.ERROR)){
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
				}else{
					/**
					 * only one line will be sent to the client
					 * it means that the client should to resolute the string
					 * using one line is because of the user information should not be much larger
					 */
					writer.print(line+FlightBookingConstants.CR_LF);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (str.toUpperCase().startsWith(FlightBookingConstants.AC)){
			acWriter.print(FlightBookingConstants.CHECK+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				line = acReader.readLine();
				line = acReader.readLine();
				if(line.equals(FlightBookingConstants.ERROR)){
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
				}else{
					/**
					 * only one line will be sent to the client
					 * it means that the client should to resolute the string
					 * using one line is because of the user information should not be much larger
					 */
					writer.print(line+FlightBookingConstants.CR_LF);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (str.toLowerCase().startsWith(FlightBookingConstants.CEA)){
			ceaWriter.print(FlightBookingConstants.CHECK+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				line = ceaReader.readLine();
				line = ceaReader.readLine();
				if(line.equals(FlightBookingConstants.ERROR)){
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
				}else{
					/**
					 * only one line will be sent to the client
					 * it means that the client should to resolute the string
					 * using one line is because of the user information should not be much larger
					 */
					writer.print(line+FlightBookingConstants.CR_LF);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			
			//writer.print(FlightBookingConstants.CR_LF);
		}
	}
	

	private void regResponse(String str) {
		streamInit();
		if(str.toUpperCase().startsWith(FlightBookingConstants.QAN)){
			qanWriter.print(FlightBookingConstants.REG+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				//read from CEA server
				line = qanReader.readLine();
				//line = qanReader.readLine();
				if(line.equals(FlightBookingConstants.ERROR))
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
				else
					writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(str.toUpperCase().startsWith(FlightBookingConstants.CEA)){
			ceaWriter.print(FlightBookingConstants.REG+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				//read from CEA server
				line = ceaReader.readLine();
				//line = ceaReader.readLine();
				if(line.equals(FlightBookingConstants.ERROR))
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
				else
					writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(str.toUpperCase().startsWith(FlightBookingConstants.AC)){
			acWriter.print(FlightBookingConstants.REG+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				line = acReader.readLine();
				//line = acReader.readLine();
				if(line.equals(FlightBookingConstants.ERROR))
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
				else
					writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Unrecognized airline! Only AC, CEA, QAN are supported!");
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		}
	}

	private void orderResonse(String str) {
		streamInit();
		/**
		 * All CEA orders will be handled by CEAServer
		 * FID like CEA010101
		 */
		if(str.toUpperCase().startsWith(FlightBookingConstants.CEA)){
			//write to CEA server
			ceaWriter.print(FlightBookingConstants.ORDER+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				//read from CEA server
				line = ceaReader.readLine();
				//////test//////
				line = ceaReader.readLine();
				System.out.println(line);
				if(line.equals(FlightBookingConstants.SUCCEEDED))
					writer.print(FlightBookingConstants.SUCCEEDED+FlightBookingConstants.CR_LF);
				else
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(str.toUpperCase().startsWith(FlightBookingConstants.AC)){
			//write to AC server
			acWriter.print(FlightBookingConstants.ORDER+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				line = acReader.readLine();
				line = acReader.readLine();
				if(line.equals(FlightBookingConstants.SUCCEEDED))
					writer.print(line+FlightBookingConstants.CR_LF);
				else
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(str.toUpperCase().startsWith(FlightBookingConstants.QAN)){
			//write to QANTAS server
			qanWriter.print(FlightBookingConstants.ORDER+" "+str+FlightBookingConstants.CR_LF);
			String line;
			try {
				line = qanReader.readLine();
				line = qanReader.readLine();
				if(line.equals(FlightBookingConstants.SUCCEEDED))
					writer.print(line+FlightBookingConstants.CR_LF);
				else
					writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			writer.print(FlightBookingConstants.ERROR+FlightBookingConstants.CR_LF);
		}
	}

	private void queryResponse(String cities) {
		streamInit();

		qanWriter.print(FlightBookingConstants.QUERY+" "+cities+FlightBookingConstants.CR_LF);
		ceaWriter.print(FlightBookingConstants.QUERY+" "+cities+FlightBookingConstants.CR_LF);
		acWriter.print(FlightBookingConstants.QUERY+" "+cities+FlightBookingConstants.CR_LF);
		
		String qanLine = null;
		String ceaLine = null;
		String acLine = null;
			
        while(true){
            try {
                qanLine = qanReader.readLine();
                writer.print(qanLine+FlightBookingConstants.CR_LF);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } if(qanLine.equals("")) {
            	while(true){
	                try {
	                    ceaLine = ceaReader.readLine();
	                    writer.print(ceaLine+FlightBookingConstants.CR_LF);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    break;
	                } if (ceaLine.equals("")){
	                	while(true){
	                		try {
	                			acLine = acReader.readLine();
	                			writer.print(acLine+FlightBookingConstants.CR_LF);
	                		} catch (IOException e) {
	                			e.printStackTrace();
	                		} if("".equals(acLine)){
	                			break;
	                		}
	                		System.out.println(acLine);
	                	}
	                    break;
	                }
	                System.out.println(ceaLine);
            	}
            	break;
            }
            System.out.println(qanLine);
        }
		
	}
	
	/**
	 * public operation: stream init
	 */
	private void streamInit() {
		try {
			ceaReader = new BufferedReader(new InputStreamReader(ceaSocket.getInputStream()));
			ceaWriter = new PrintStream(ceaSocket.getOutputStream());
			qanReader = new BufferedReader(new InputStreamReader(qanSocket.getInputStream()));
			qanWriter = new PrintStream(qanSocket.getOutputStream());
			acReader = new BufferedReader(new InputStreamReader(acSocket.getInputStream()));
			acWriter = new PrintStream(acSocket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	private String losePrefix(String str, String prefix) {
		int index = prefix.length();
		String ret = str.substring(index).trim();
		return ret;
		
	}
}