package fb.common;

public interface FlightBookingService {
	
	public String[] listReq();
	
	public String[] queryReq(String input);
	
	public String[] timeReq(String input);
	
	public boolean orderReq(String fid);
	
	public void quit();
}
