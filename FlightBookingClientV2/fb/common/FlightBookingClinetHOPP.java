package fb.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FlightBookingClinetHOPP implements FlightBookingService {

	protected Socket mySocket;
	protected BufferedReader reader;
	protected PrintStream writer;

	public FlightBookingClinetHOPP(String server) throws IOException {
		InetAddress address = InetAddress.getByName(server);
		mySocket = null;
		InputStream inStream = null;
		OutputStream outStream = null;

		mySocket = new Socket(address, FlightBookingConstants.PORT);
		inStream = mySocket.getInputStream();
		outStream = mySocket.getOutputStream();

		reader = new BufferedReader(new InputStreamReader(inStream));
		writer = new PrintStream(outStream);
	}

	@Override
	public String[] queryReq(String input) {
		writer.print(FlightBookingConstants.QUERY + " " + input
				+ FlightBookingConstants.CR_LF);
		return readLinesFromServer();
	}

	@Override
	public boolean regReq(String input) {
		writer.print(FlightBookingConstants.REG + " " + input
				+ FlightBookingConstants.CR_LF);
		String response = null;
		boolean flag = false;
		while(true){
			try {
				response = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (response.equals(FlightBookingConstants.SUCCEEDED))
				flag = true;
			else if (response.equals(FlightBookingConstants.ERROR))
				System.out.println("Output:ERROR");
			else if (response.equals(""))
				break;
			else {
				System.out.println("Unrecogized error!----->reg");
			}

		}
		return flag;
	}

	// order fid1 fid2 username
	// order fid username
	
	public boolean orderReq(String str) {
		writer.print(FlightBookingConstants.ORDER + " " + str
				+ FlightBookingConstants.CR_LF);
		String response = null;
		Boolean flag = false;
		while(true){
			try {
				response = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (response.equals(FlightBookingConstants.SUCCEEDED))
				flag = true;
			else if (response.equals(FlightBookingConstants.ERROR))
				flag = false;
			else if (response.equals("")){
				break;
			}
		}
		return flag;
	}

	@Override
	public void quit() {
		try {
			writer.print(FlightBookingConstants.QUIT
					+ FlightBookingConstants.CR_LF);
			reader.close();
			writer.close();
			mySocket.close();
			System.out.println("You have quit the system successfully!");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] checkReq(String input) {
		writer.print(FlightBookingConstants.CHECK + " " + input
				+ FlightBookingConstants.CR_LF);
		return readLinesFromServer();
	}
	
	/**
	 * Public operation: read lines from server
	 * Common codes for: query and check
	 * @return string array
	 */
	private String[] readLinesFromServer() {
		List<String> list = new ArrayList<String>();
		String line = null;
		while (true) {
			try {
				line = reader.readLine();
			} catch (IOException e) {
				break;
			}
			if (line.equals("")) {
				break;
			}
			if(line.toUpperCase().startsWith(FlightBookingConstants.AC)
					||line.toUpperCase().startsWith(FlightBookingConstants.CEA)
					||line.toUpperCase().startsWith(FlightBookingConstants.QAN)){
				list.add(line);
			}
			//list.add(line);
		}
		String[] resultList = new String[list.size()];
		list.toArray(resultList);
		return resultList;
	}

	@Override
	public boolean checkInputOfQuery(String input) {
		String[] arr = input.split(" ");
		String cityRE = "^[A-Za-z]+$";
		String dateRE = "2014-03-[0-9]{2}";
		String fromCity;
		String toCity;
		String leaveDate;
		String returndate;
		if(arr.length == 4){
			//return-trip
			fromCity = arr[0];
			toCity = arr[1];
			leaveDate = arr[2];
			returndate = arr[3];
			if(fromCity.matches(cityRE)&&toCity.matches(cityRE))
				if(leaveDate.matches(dateRE)&&returndate.matches(dateRE))
					return true;
				else
					System.out.println("You can only booking the tickets between 2014-03-01 to 2014-03-30");
			else{
				System.out.println("Your city names may have Illegal characters!");
			}
				
			
		}else if(arr.length == 3){
			//one-stop trip
			fromCity = arr[0];
			toCity = arr[1];
			leaveDate = arr[2];
			if(fromCity.matches(cityRE)&&toCity.matches(cityRE))
				if(leaveDate.matches(dateRE))
					return true;
				else
					System.out.println("You can only booking the tickets between 2014-03-01 to 2014-03-30");
			else{
				System.out.println("Your city names may have Illegal characters!");
			}
		}
		return false;
	}
	
	@Override
	public boolean checkInputOfCheck(String input) {
		//check <airline, username>
		boolean flag = false;
		String[] arr = input.split(" ");
		String airline = arr[0];
		String username = arr[1];
		String airRE = "(qan|cea|ac)";
		String nameRE = "^[A-Za-z]+$";
		if(arr.length == 2){
			if(airline.matches(airRE))
				if(username.matches(nameRE))
					flag = true;
				else
					System.out.println("Username has Illegal characters!");
			else{
				System.out.println("Only cea|qan|ac are supported!");
			}
		}
		
		return flag;
	}

	@Override
	public boolean checkInputOfReg(String input) {
		// reg <airline,username,phone,email,creditcard>
		String[] arr = input.split(" ");
		String airline = arr[0];
		String username = arr[1];
		String phone = arr[2];
		String email = arr[3];
		String creditcard = arr[4];

		String airRE = "(qan|cea|ac)";
		String nameRE = "^[A-Za-z]+$";
		String phoneRE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		String mailRE = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		String creditcardRE = "[0-9]{16}";

		if(arr.length == 5){
			if(airline.matches(airRE))
				if(username.matches(nameRE))
					if(phone.matches(phoneRE))
						if(email.matches(mailRE))
							if(creditcard.matches(creditcardRE))
								return true;
							else
								System.out.println("Illegal creditcard number, 16bits!");
						else
							System.out.println("Illegal email address!");
					else	
						System.out.println("Illegal phone number");
				else
					System.out.println("Illegal name!");
			else
				System.out.println("Only support: qan|cea|ac");
		}else{
			System.out.println("Format: REG <airline,username,phone,email,creditcard>");
		}
		return false;
	}

	@Override
	public boolean checkInputOfOrder(String input) {
		//order cea030110 cea030111 weiwei
		//order cea030110 weiwei
		String[] arr = input.split(" ");
		if(arr.length == 3){
			String fid1 = arr[1];
			String fid2 = arr[2];
			String username = arr[3];
			String fidRE = "(cea|ac|qan)0[1-4]0[1-4][0-3][0-9]";
			String nameRE = "^[A-Za-z]+$";
			if(fid1.matches(fidRE)&&fid2.matches(fidRE))
				if(username.matches(nameRE))
					return true;
				else
					System.out.println("Illegal name");
			else
				System.out.println("Illegal FID number");
		}
		else if(arr.length == 2){
			String fid = arr[1];
			String username = arr[3];
			String fidRE = "(cea|ac|qan)0[1-4]0[1-4][0-3][0-9]";
			String nameRE = "^[A-Za-z]+$";
			if(fid.matches(fidRE))
				if(username.matches(nameRE))
					return true;
				else
					System.out.println("Illegal name");
			else
				System.out.println("Illegal FID number");
		}
		return false;
	}

}
