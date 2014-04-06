package fb.common;

public interface FlightBookingService {
		
	public String[] queryReq(String input);
	
	public String[] checkReq(String input);
	
	public boolean regReq(String input);
	
	public boolean orderReq(String fid);
	
	public void quit();
	
	/**
	 * input checking
	 * 
	 */
	public boolean checkInputOfQuery(String input);
	public boolean checkInputOfCheck(String input);
	public boolean checkInputOfReg(String input);
	public boolean checkInputOfOrder(String input);
}
