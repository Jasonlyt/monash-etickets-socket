package fb.common;

import java.util.ArrayList;
import java.util.List;

import fb.dao.FlightDao;
import fb.dao.OrderDao;
import fb.dao.UserDao;
import fb.dao.impl.FlightDaoImpl;
import fb.dao.impl.OrderDaoImpl;
import fb.dao.impl.UserDaoImpl;
import fb.entity.Flight;
import fb.entity.Order;
import fb.entity.User;

public class ChildServerHOPP implements ServerSevice {
		
	@Override
	public List<Flight> queryRespFromQanDB(String input) {
		String[] city = input.split(" ");
		List<Flight> qanList1, qanList2;
		List<Flight> roundTripList = null;
		if (city.length == 4) {
			FlightDao dao = new FlightDaoImpl();
			//list = dao.queryFlightListFromAll(city[0], city[1]);
			
			java.sql.Date leaveDate = java.sql.Date.valueOf(city[2]);
			java.sql.Date returnDate = java.sql.Date.valueOf(city[3]);
			
			qanList1 =  dao.queryFlightListFromQan(city[0], city[1],leaveDate);
			qanList2 =  dao.queryFlightListFromQan(city[1], city[0],returnDate);
			
			
			if(qanList1 !=null && qanList2 != null){
				for (Flight f : qanList2) {
					qanList1.add(f);
				}
				roundTripList = qanList1;
			}
		}
		return roundTripList;
	}

	@Override
	public List<Flight> queryRespFromCeaDB(String input) {
		String[] city = input.split(" ");
		List<Flight> ceaList1, ceaList2;
		List<Flight> roundTripList = null;
		if (city.length == 4) {
			FlightDao dao = new FlightDaoImpl();
			//list = dao.queryFlightListFromAll(city[0], city[1]);
			//ceaList1 =  dao.queryFlightListFromCEA(city[0], city[1]);
			//ceaList2 =  dao.queryFlightListFromCEA(city[1], city[0]);
			
			java.sql.Date leaveDate = java.sql.Date.valueOf(city[2]);
			java.sql.Date returnDate = java.sql.Date.valueOf(city[3]);
			
			ceaList1 =  dao.queryFlightListFromCEA(city[0], city[1],leaveDate);
			ceaList2 =  dao.queryFlightListFromCEA(city[1], city[0],returnDate);
			
			
			if(ceaList1 !=null && ceaList2 != null){
				for (Flight f : ceaList2) {
					ceaList1.add(f);
				}
				roundTripList = ceaList1;
			}
		}
		return roundTripList;
	}

	@Override
	public List<Flight> queryRespFromAcDB(String input) {
		String[] city = input.split(" ");
		List<Flight> acList1, acList2;
		List<Flight> roundTripList = null;
		if (city.length == 4) {
			FlightDao dao = new FlightDaoImpl();
			//list = dao.queryFlightListFromAll(city[0], city[1]);
			java.sql.Date leaveDate = java.sql.Date.valueOf(city[2]);
			java.sql.Date returnDate = java.sql.Date.valueOf(city[3]);
			
			acList1 =  dao.queryFlightListfromAC(city[0], city[1],leaveDate);
			acList2 =  dao.queryFlightListfromAC(city[1], city[0],returnDate);
			
			if(acList1 !=null && acList2 != null){
				for (Flight f : acList2) {
					acList1.add(f);
				}
				roundTripList = acList1;
			}
		}
		return roundTripList;
	}
	

	@Override
	public boolean orderResp(String str) {
		String[] arr = str.split(" ");
		FlightDao flightDao ;
		OrderDao orderDao ;
		UserDao userDao;
		if(arr.length ==3 ) {
			String fid1 = arr[0];
			String fid2 = arr[1];
			String username = arr[2];
			
			flightDao = new FlightDaoImpl();
			orderDao = new OrderDaoImpl();
			userDao = new UserDaoImpl();
			
			if(fid1.toUpperCase().startsWith(FlightBookingConstants.CEA)
					&&fid2.toUpperCase().startsWith(FlightBookingConstants.CEA)){
				if(flightDao.queryTicketExistedFromCEA(fid1)
						&&flightDao.queryTicketExistedFromCEA(fid2)
						&&userDao.queryUserExistedFromCEA(username)){
					if(flightDao.updateTicketsToCEA(fid1)&&flightDao.updateTicketsToCEA(fid2)){
						Order order1 = new Order();
						Order order2 = new Order();
						order1.setFid(fid1);
						order2.setFid(fid2);
						order1.setUsername(username);
						order2.setUsername(username);
						boolean flag1 = orderDao.saveOrderToCEA(order1);
						boolean flag2 = orderDao.saveOrderToCEA(order2);
						if(flag1&&flag2)
							return true;
					}
					else
						return false;
				}
				else
					return false;
			}else if(fid1.toUpperCase().startsWith(FlightBookingConstants.AC)
					&&fid2.toUpperCase().startsWith(FlightBookingConstants.AC)){
				if(flightDao.queryTicketExistedFromAC(fid1)
						&&flightDao.queryTicketExistedFromAC(fid2)
						&&userDao.queryUserExistedFromAC(username)){
					
					if(flightDao.updateticketsToAC(fid1)&&flightDao.updateticketsToAC(fid2)){
						Order order1 = new Order();
						Order order2 = new Order();
						order1.setFid(fid1);
						order2.setFid(fid2);
						order1.setUsername(username);
						order2.setUsername(username);
						boolean flag1 = orderDao.saveOrderToAC(order1);
						boolean flag2 = orderDao.saveOrderToAC(order2);
						if(flag1&&flag2)
							return true;
					}
					else 
						return false;
				}
				else
					return false;
			}else if(fid1.toUpperCase().startsWith(FlightBookingConstants.QAN)
					&&fid2.toUpperCase().startsWith(FlightBookingConstants.QAN)){
				if(flightDao.queryTicketExistedFromQan(fid1)
						&&flightDao.queryTicketExistedFromQan(fid2)
						&&userDao.queryUserExistedFromQan(username)){
					
					if(flightDao.updateTicketsToQan(fid1)&&flightDao.updateTicketsToQan(fid2)){
						Order order1 = new Order();
						Order order2 = new Order();
						order1.setFid(fid1);
						order2.setFid(fid2);
						order1.setUsername(username);
						order2.setUsername(username);
						boolean flag1 = orderDao.saveOrderToQan(order1);
						boolean flag2 = orderDao.saveOrderToQan(order2);
						if(flag1&&flag2)
							return true;
					}
					else
						return false;
				}
				else
					return false;
			}
			else
				return false;
		} 
		
		/**
		 * booking one-stop ticket
		 */
		else if(arr.length==2){
			String fid = arr[0];
			String username = arr[1];
			
			flightDao = new FlightDaoImpl();
			orderDao = new OrderDaoImpl();
			userDao = new UserDaoImpl();
			
			if(fid.toUpperCase().startsWith(FlightBookingConstants.CEA)){
				
				if(flightDao.queryTicketExistedFromCEA(fid)
						&&userDao.queryUserExistedFromCEA(username)){
					if(flightDao.updateTicketsToCEA(fid)){
						Order order = new Order();
						order.setFid(fid);
						order.setUsername(username);
						boolean flag = orderDao.saveOrderToCEA(order);
						if(flag)
							return true;
					}
					else
						return false;
				}
				else
					return false;
			}else if(fid.toUpperCase().startsWith(FlightBookingConstants.AC)){
				if(flightDao.queryTicketExistedFromAC(fid)
						&&userDao.queryUserExistedFromAC(username)){
					
					if(flightDao.updateticketsToAC(fid)){
						Order order = new Order();
						order.setFid(fid);
						order.setUsername(username);
						boolean flag = orderDao.saveOrderToAC(order);
						if(flag)
							return true;
					}
					else 
						return false;
				}
				else
					return false;
			}else if(fid.toUpperCase().startsWith(FlightBookingConstants.QAN)){
				if(flightDao.queryTicketExistedFromQan(fid)
						&&userDao.queryUserExistedFromQan(username)){
					
					if(flightDao.updateTicketsToQan(fid)){
						Order order = new Order();
						order.setFid(fid);
						order.setUsername(username);
						boolean flag = orderDao.saveOrderToQan(order);
						if(flag)
							return true;
					}
					else
						return false;
				}
				else
					return false;
			}
			else
				return false;
		}
		return false;
	}

	@Override
	public boolean regResp(String str) {
		String[] arr = str.split(" ");
		if(arr.length ==5 ){
			String air = arr[0];
			String username = arr[1];
			String phone = arr[2];
			String mail = arr[3];
			String credit = arr[4];
			
			User user = new User();
			user.setUsername(username);
			user.setPhone(phone);
			user.setEmail(mail);
			user.setCreditcard(credit);
			UserDao dao = new UserDaoImpl();
			if(air.equalsIgnoreCase(FlightBookingConstants.QAN)){
				if(!dao.queryUserExistedFromQan(username)){
					if(dao.insertUserToQan(user)){
						System.out.println("----insert a new user successfully------");
						return true;
					}
				}
			}else if(air.equalsIgnoreCase(FlightBookingConstants.CEA)){
				if(!dao.queryUserExistedFromCEA(username)){
					if(dao.insertUserToCea(user)){
						System.out.println("----insert a new user successfully------");						
						return true;
					}
				}
			}else if(air.equalsIgnoreCase(FlightBookingConstants.AC)){
				if(!dao.queryUserExistedFromAC(username))
					if(dao.insertUserToAc(user)){
						System.out.println("----insert a new user successfully------");						
						return true;
					}
			}
		}
		return false;
	}

	@Override
	public List<Order> checkOrders(String str) {
		String[] arr = str.split(" ");
		if(arr.length==2){
			String airline = arr[0];
			String username = arr[1];
			List<Order> list = new ArrayList<Order>();
			OrderDao dao = new OrderDaoImpl();
			UserDao userDao = new UserDaoImpl();
			if(airline.equalsIgnoreCase(FlightBookingConstants.CEA)){
				if(userDao.queryUserExistedFromCEA(username))
					list = dao.checkOrderFromCEA(username);
				else
					System.out.println("Cannot found this user!");
			}else if(airline.equalsIgnoreCase(FlightBookingConstants.AC)){
				if(userDao.queryUserExistedFromAC(username))
					list = dao.checkOrderFromAC(username);
				else
					System.out.println("Cannot found this user!");
			}else if(airline.equalsIgnoreCase(FlightBookingConstants.QAN)){
				if(userDao.queryUserExistedFromQan(username))
					list = dao.checkOrderFromQan(username);
				else
					System.out.println("Cannot found this user!");
			}else
				list = null;
			
			return list;
		} else {
			return null;
		}
	}

}
