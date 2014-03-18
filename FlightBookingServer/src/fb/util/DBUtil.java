package fb.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class DBUtil {
    private static String driver;  
    private static String url;  
    private static String user;  
    private static String password;  
    
    /**
     * load the property file
     */
    static{
        Properties props = new Properties();  
        //get the class loader
        InputStream is = DBUtil.class.getClassLoader().getResourceAsStream("fb/util/dbConfig.properties");  
        try {  
            props.load(is);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        driver = props.getProperty("driver");  
        url = props.getProperty("url");  
        user = props.getProperty("user");  
        password = props.getProperty("password");
    }
    
    /**
     * register the driver
     */
    static{
    	try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * OPEN THE DATABASE CONNECTION
     * @return
     */
    public static Connection getConn(){
    	Connection conn = null;
    	try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return conn;
    }
    
    /**
     * CLOSE THE DATABASE CONNECTION
     * @param rs
     */
	public static void closeAll(ResultSet rs,Statement stmt,Connection conn){
		close(rs);
		close(stmt);
		close(conn);
	}
    
	
	
    public static void close(ResultSet rs){
    	if(rs!=null){
    		try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    
	public static void close(Statement stmt){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

}
