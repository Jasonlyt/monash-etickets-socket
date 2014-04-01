package fb.dao;

import java.util.List;

import fb.entity.Flight;


public interface FlightDao {
	
	public List<Flight> queryFlightListFromQan(String fromCity, String toCity);
	public List<Flight> queryFlightListFromCEA(String fromCity, String toCity);
	public List<Flight> queryFlightListfromAC(String fromCity, String toCity);
	
	public boolean queryTicketExistedFromCEA(String fid);
	public boolean queryTicketExistedFromAC(String fid);
	public boolean queryTicketExistedFromQan(String fid);
	
	public boolean updateTicketsToCEA(String fid);
	public boolean updateticketsToAC(String fid);
	public boolean updateTicketsToQan(String fid);
	
	
}
