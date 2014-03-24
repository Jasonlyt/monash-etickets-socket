package fb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fb.dao.OrderDao;
import fb.entity.Order;
import fb.util.DBUtil;

public class OrderDaoImpl implements OrderDao {

	public boolean saveOrderToCEA(Order order) {
		String sql = "insert into ordertbl values (?,?,?) ";
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

}
