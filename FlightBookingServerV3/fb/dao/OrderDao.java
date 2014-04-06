package fb.dao;

import java.util.List;

import fb.entity.Order;

public interface OrderDao {
	
	public boolean saveOrderToCEA(Order order);
	public boolean saveOrderToAC(Order order);
	public boolean saveOrderToQan(Order order);
	
	public List<Order> checkOrderFromCEA(String username);
	public List<Order> checkOrderFromAC(String username);
	public List<Order> checkOrderFromQan(String username);
	
	public boolean orderExistedFromCEA(String fid,String username);
	public boolean orderExistedFromAC(String fid,String username);
	public boolean orderExistedFromQAN(String fid,String username);
	

}
