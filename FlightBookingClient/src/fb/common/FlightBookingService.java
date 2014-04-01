package fb.common;

public interface FlightBookingService {
		
	public String[] queryReq(String input);
	
	public String[] CheckReq(String input);
	
	public boolean regReq(String input);
	
	public boolean orderReq(String fid);
	
	public void quit();
}
