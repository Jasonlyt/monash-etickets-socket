package fb.dao;

import java.util.List;


public interface FlightDao {
	
	
	public List<?> listFlightsFromAll();
	/**
	 * query the flight remaining have tickets
	 * 
	 * @param fromCity	The city you want to depart
	 * @param toCity	The city you flight for
	 * @return Flight
	 */	
	public List<?> queryFlightListFromAll(String fromCity, String toCity);
	
	public boolean queryTicketExistedFromCEA(String fid);
	
	public boolean updateTicketsToCEA(String fid);
}
