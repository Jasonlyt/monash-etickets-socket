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

	public boolean saveOrderToCEA(Order order) {
		String sql = "insert into cea_order values (?,?,?) ";
		return saveOrderToServer(order, sql);
	}


	@Override
	public boolean saveOrderToAC(Order order) {
		String sql = "insert into ac_order values (?,?,?) ";
		return saveOrderToServer(order, sql);
	}
	
	@Override
	public boolean saveOrderToQan(Order order) {
		String sql = "insert into qantas_order values (?,?,?) ";
		return saveOrderToServer(order, sql);
	}
	
	/**
	 * public operation: saveOrderToServer
	 * @param order
	 * @param sql
	 * @return
	 */
	private boolean saveOrderToServer(Order order, String sql) {
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setInt(1, order.getOid());
			pstmt.setString(2, order.getFid());
			pstmt.setString(3, order.getUsername());
			int state = pstmt.executeUpdate();
			if(state==1)
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
	public List<Order> checkOrderFromCEA(String username) {
		String sql = "select * from cea_order where username=?";
		String column_fid = "fid";
		return checkOrderFromServer(username, sql, column_fid);
	}

	@Override
	public List<Order> checkOrderFromAC(String username) {
		String sql = "select * from ac_order where ac_username=?";
		String column_fid = "ac_fid";
		return checkOrderFromServer(username, sql, column_fid);
	}


	@Override
	public List<Order> checkOrderFromQan(String username) {
		String sql = "select * from qantas_order where qan_username=?";
		String column_fid = "qan_fid";
		return checkOrderFromServer(username, sql, column_fid);
	}
	
	private List<Order> checkOrderFromServer(String username, String sql,
			String column_fid) {
		DBUtil util =new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			List<Order> list = new ArrayList<Order>();
			while(rs.next()){
				Order order = new Order();
				int oid = rs.getInt("oid");
				String fid = rs.getString(column_fid);
				order.setOid(oid);
				order.setFid(fid);
				order.setUsername(username);
				list.add(order);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public boolean orderExistedFromCEA(String fid,String username) {
		String sql = "select fid from cea_order where fid=? and username=? ";
		String columnName = "fid";
		return orderExistedFromServer(fid,username, sql, columnName);
	}


	@Override
	public boolean orderExistedFromAC(String fid,String username) {
		String sql = "select ac_fid from ac_order where ac_fid=? and ac_username=? ";
		String columnName = "ac_fid";
		return orderExistedFromServer(fid,username, sql, columnName);
	}


	@Override
	public boolean orderExistedFromQAN(String fid,String username) {
		String sql = "select qan_fid from qantas_order where qan_fid=? and qan_username=? ";
		String columnName = "qan_fid";
		return orderExistedFromServer(fid, username, sql, columnName);
	}
	
	private boolean orderExistedFromServer(String fid, String username, String sql,String columnName) {
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fid);
			pstmt.setString(2, username);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String name = rs.getString(columnName);
				if(name!=null)
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return false;
	}
}
