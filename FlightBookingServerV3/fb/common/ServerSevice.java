package fb.common;

import java.util.List;

import fb.entity.Flight;
import fb.entity.Order;

public interface ServerSevice {
	
	public List<Flight> queryResp(String input, String airline);
	
	public boolean regResp(String str);
		
	public boolean orderResp(String str);
	
	public List<Order> checkOrders(String str);
	
}
