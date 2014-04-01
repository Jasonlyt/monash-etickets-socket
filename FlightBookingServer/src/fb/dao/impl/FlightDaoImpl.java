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
	public List<Flight> queryFlightListFromCEA(String fromCity, String toCity) {
		String sql = "select * from cea_flight where departure_city=? and destination_city=? ";
		String fid_column_name = "fid";
		return queryFlightListFromSrver(fromCity, toCity, sql, fid_column_name);
	}

	@Override
	public List<Flight> queryFlightListfromAC(String fromCity, String toCity) {
		String sql = "select * from ac_flight where departure_city=? and destination_city=? ";
		String fid_column_name = "ac_fid";
		return queryFlightListFromSrver(fromCity, toCity, sql, fid_column_name);
	}
	
	@Override
	public List<Flight> queryFlightListFromQan(String fromCity, String toCity) {
		String sql = "select * from qantas_flight where departure_city=? and destination_city=? ";
		String fid_column_name = "qan_fid";
		return queryFlightListFromSrver(fromCity, toCity, sql, fid_column_name);
	}
	
	/**
	 * public operation: query flight list from server
	 * @param fromCity
	 * @param toCity
	 * @param sql
	 * @return
	 */
	private List<Flight> queryFlightListFromSrver(String fromCity,
			String toCity, String sql, String fid_column_name) {
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,fromCity);
			pstmt.setString(2, toCity);

			
			ResultSet rs = pstmt.executeQuery();
			List<Flight> list = new ArrayList<Flight>();
			while (rs.next()) {
				String fid = rs.getString(fid_column_name);
				String airline_comp = rs.getString("airline_comp");
				Date departing_date = rs.getDate("departing_date");
				int tickets = rs.getInt("tickets");
				Flight flight = new Flight();
				flight.setFid(fid);
				flight.setAirline_comp(airline_comp);
				flight.setDeparture_city(fromCity);
				flight.setDestination_city(toCity);
				flight.setDeparting_date(departing_date);
				flight.setTickets(tickets);
				list.add(flight);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean queryTicketExistedFromCEA(String fid) {
		String sql = "select fid,tickets from cea_flight where fid=? ";
		return queryTicketExistedFromServer(fid, sql);
	}

	@Override
	public boolean queryTicketExistedFromAC(String fid) {
		String sql = "select ac_fid,tickets from ac_flight where ac_fid=? ";
		return queryTicketExistedFromServer(fid, sql);
	}

	@Override
	public boolean queryTicketExistedFromQan(String fid) {
		String sql = "select qan_fid,tickets from qantas_flight where qan_fid=? ";
		return queryTicketExistedFromServer(fid, sql);
	}

	@Override
	public boolean updateTicketsToCEA(String fid) {
		String sql = "update cea_flight set tickets=tickets-1 where fid = ? ";
		return updateTicketsToServer(fid, sql);
	}

	@Override
	public boolean updateticketsToAC(String fid) {
		String sql = "update ac_flight set tickets=tickets-1 where ac_fid = ? ";
		return updateTicketsToServer(fid, sql);
	}
	

	@Override
	public boolean updateTicketsToQan(String fid) {
		String sql = "update qantas_flight set tickets=tickets-1 where qan_fid = ? ";
		return updateTicketsToServer(fid, sql);
	}
	
	/**
	 * public operation: query ticket existed from server
	 * 
	 * @param fid
	 * @param sql
	 * @return
	 */
	private boolean queryTicketExistedFromServer(String fid, String sql) {
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				int tic = rs.getInt("tickets");
				if(tic > 0)
					return true;
			}
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
	
	/**
	 * public operation: update tickets to server
	 * @param fid
	 * @param sql
	 * @return
	 */
	private boolean updateTicketsToServer(String fid, String sql) {
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
