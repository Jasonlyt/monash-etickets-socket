package fb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fb.dao.OrderDao;
import fb.entity.Order;
import fb.util.DBUtil;

public class OrderDaoImpl implements OrderDao {

	@Override
	public boolean saveOrder(Order order) {
		String sql = "insert into ordertbl values (?,?,?,?,?,?) ";
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, order.getOid());
			pstmt.setString(2, order.getFid());
			pstmt.setString(3, order.getFullname());
			pstmt.setString(4, order.getPhone());
			pstmt.setString(5, order.getEmail());
			pstmt.setString(6, order.getCreditcard());
			
			pstmt.executeUpdate();
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
	public void updateTickets(String flightNo) {
		String sql = "update flighttbl set tickets=tickets-1 where fid=?";
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,flightNo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			util.close(conn);
		}
	}

	@Override
	public List checkOrders(String fullname) {
		String sql = "select * from ordertbl where fullname=?";
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fullname);
			ResultSet rs = pstmt.executeQuery();
			List<Order> list = new ArrayList<Order>();
			
			while(rs.next()){
				int oid = rs.getInt("oid");
				String fid = rs.getString("fid");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				String creditcard = rs.getString("creditcard");
				
				Order order = new Order();
				order.setOid(oid);
				order.setFid(fid);
				order.setFullname(fullname);
				order.setPhone(phone);
				order.setEmail(email);
				order.setCreditcard(creditcard);
				list.add(order);
			}
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
