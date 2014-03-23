package fb.dao;

import java.util.List;

import fb.entity.Order;

public interface OrderDao {
	/**
	 * Upload the order information of flight
	 * @param order
	 * @return true, upload successfully
	 */
	public boolean saveOrder(Order order);
	/**
	 * Update the remaining tickets of the flight
	 * @param flightNo
	 */
	public void updateTickets(String flightNo);
	
	/**
	 * Check the orders that the specific user ordered.
	 * @param fullname
	 * @return
	 */
	public List<?> checkOrders(String fullname);
}
