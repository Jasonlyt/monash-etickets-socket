package fb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fb.entity.Flight;
import fb.util.DBUtil;

public class FlightDaoImpl implements fb.dao.FlightDao {

	@Override
	public List<Flight> queryFlightListFromAll(String fromCity, String toCity) {
		String sql = "select * from cea_flight where departure_city=? and destination_city=? "
				+ "union select * from ac_flight where departure_city=? and destination_city=? "
				+ "union select * from qantas_flight where departure_city=? and destination_city=? ";

		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,fromCity);
			pstmt.setString(2, toCity);
			pstmt.setString(3,fromCity);
			pstmt.setString(4, toCity);
			pstmt.setString(5,fromCity);
			pstmt.setString(6, toCity);

			
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
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<?> listFlightsFromAll() {
		
		String sql = "select * from ac_flight"
				+ " union select * from cea_flight"
				+" union select * from qantas_flight";
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			List<Flight> flightList = new ArrayList<Flight>();
			while(rs.next()){
				String fid = rs.getString("fid");
				String airline_comp = rs.getString("airline_comp");
				String departure_city = rs.getString("departure_city");
				String destination_city = rs.getString("destination_city");
				Date departing_date = rs.getDate("departing_date");
				Date returning_date = rs.getDate("returning_date");
				int tickets = rs.getInt("tickets");
				
				Flight flight = new Flight();
				flight.setFid(fid);
				flight.setAirline_comp(airline_comp);
				flight.setDeparture_city(departure_city);
				flight.setDestination_city(destination_city);
				flight.setDeparting_date(departing_date);
				flight.setReturning_date(returning_date);
				flight.setTickets(tickets);
				flightList.add(flight);
			}
			return flightList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean queryTicketExistedFromCEA(String fid) {
		String sql = "select tickets from cea_flight where fid=? ";
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fid);
			ResultSet rs = pstmt.executeQuery();
			int tickets = rs.getInt("tickets");
			if(tickets>0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean updateTicketsToCEA(String fid) {
		String sql = "update cea_flight set tickets=tickets-1 where fid = ? ";
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,fid);
			int status = pstmt.executeUpdate();
			if(status == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
