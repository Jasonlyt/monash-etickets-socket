package fb.dao;

import java.util.List;


public interface FlightDao {
	
	
	public List<?> listFlights();
	/**
	 * query the flight remaining have tickets
	 * 
	 * @param fromCity	The city you want to depart
	 * @param toCity	The city you flight for
	 * @return Flight
	 */	
	public List<?> queryFlightList(String fromCity, String toCity);
	
	
	//public void orderFlight(String flightNo);
}
