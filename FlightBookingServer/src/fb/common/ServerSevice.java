package fb.common;

import java.util.List;

import fb.entity.Flight;
import fb.entity.Order;

public interface ServerSevice {
	
	public List<Flight> queryRespFromQanDB(String input);
	public List<Flight> queryRespFromCeaDB(String input);
	public List<Flight> queryRespFromAcDB(String input);
	
	public boolean regResp(String str);
		
	public boolean orderResp(String str);
	
	public List<Order> checkOrders(String str);
}
