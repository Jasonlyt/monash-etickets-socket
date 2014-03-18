package fb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fb.entity.Flight;

public class FilghtDaoImpl implements fb.dao.FlightDao {

	@Override
	public List<Flight> queryFlightList(String fromCity, String toCity) {
		String sql = "select * from flighttbl where departure_city=? and destination_city=?";
		Connection conn = fb.util.DBUtil.getConn();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,fromCity);
			pstmt.setString(2, toCity);
			
			ResultSet rs = pstmt.executeQuery();
			List<Flight> list = new ArrayList<Flight>();
			while (rs.next()) {
				String fid = rs.getString("fid");
				String airline_comp = rs.getString("airline_comp");
				Date departing_date = rs.getDate("departing_date");
				Date returning_date = rs.getDate("returning_date");
				int tickets = rs.getInt("tickets");
				Flight flight = new Flight();
				flight.setFid(fid);
				flight.setAirline_comp(airline_comp);
				flight.setDeparture_city(fromCity);
				flight.setDestination_city(toCity);
				flight.setDeparting_date(departing_date);
				flight.setReturning_date(returning_date);
				flight.setTickets(tickets);
				list.add(flight);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
