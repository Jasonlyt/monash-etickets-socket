package fb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fb.dao.UserDao;
import fb.entity.User;
import fb.util.DBUtil;

public class UserDaoImpl implements UserDao {

	@Override
	public boolean queryUserExistedFromCEA(String username) {
		String sql = "select username from cea_user where username=? ";
		String columnName = "username";
		return queryUserExistedFromServer(username, sql, columnName);
	}

	@Override
	public boolean queryUserExistedFromAC(String username) {
		String sql = "select ac_username from ac_user where ac_username=? ";
		String columnName = "ac_username";
		return queryUserExistedFromServer(username, sql, columnName);
	}

	@Override
	public boolean queryUserExistedFromQan(String username) {
		String sql = "select qan_username from qantas_user where qan_username=? ";
		String columnName = "qan_username";
		return queryUserExistedFromServer(username, sql, columnName);
	}
	
	/**
	 * public operation: query
	 * @param username
	 * @param sql
	 * @return ture, if the user existed in the database
	 */
	private boolean queryUserExistedFromServer(String username, String sql,String columnName) {
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
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

	@Override
	public boolean insertUserToQan(User user) {
		String sql = "insert into qantas_user values (?,?,?,?)";
		return insertNewUserToServer(user, sql);
	}
	
	private boolean insertNewUserToServer(User user, String sql) {
		DBUtil util = new DBUtil();
		Connection conn = util.getConn();
		
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
			pstmt.setString(1, user.getUsername() );
			pstmt.setString(2, user.getPhone());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getCreditcard());
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
	public boolean insertUserToAc(User user) {
		String sql = "insert into ac_user values (?,?,?,?)";
		return insertNewUserToServer(user, sql);
	}

	@Override
	public boolean insertUserToCea(User user) {
		String sql = "insert into cea_user values (?,?,?,?)";
		return insertNewUserToServer(user, sql);
	}

}
