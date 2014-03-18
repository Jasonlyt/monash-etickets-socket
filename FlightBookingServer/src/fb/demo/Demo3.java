package fb.demo;

import fb.dao.OrderDao;
import fb.dao.impl.OrderDaoImpl;
import fb.entity.Order;


public class Demo3 {
	public static void main(String[] args) {
		Order order = new Order();
		
		order.setFid("CHN030110");
		order.setFullname("WELL");
		order.setPhone("15895878513");
		order.setEmail("SWWOL@QQ.COM");
		order.setCreditcard("12345");
		
		OrderDao dao = new OrderDaoImpl();
		if(dao!=null){
			if(dao.saveOrder(order)){
				dao.updateTickets(order.getFid());
				System.out.println("Update success");
			}
		}
	}
}
