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

public class AirlineServerHOPP implements ServerSevice {

	@Override
	public List<Flight> queryResp(String input, String airline) {
		String[] arr = input.split(" ");
		List<Flight> list1 = new ArrayList<Flight>();
		List<Flight> list2 = new ArrayList<Flight>();
		List<Flight> roundList = new ArrayList<Flight>();
		// round-trip
		if (arr.length == 4) {
			FlightDao dao = new FlightDaoImpl();
			java.sql.Date leaveDate = java.sql.Date.valueOf(arr[2]);
			java.sql.Date returnDate = java.sql.Date.valueOf(arr[3]);

			if (airline.equalsIgnoreCase(FlightBookingConstants.CEA)) {
				list1 = dao.queryFlightListFromCEA(arr[0], arr[1], leaveDate);
				list2 = dao.queryFlightListFromCEA(arr[1], arr[0], returnDate);
			} else if (airline.equalsIgnoreCase(FlightBookingConstants.AC)) {
				list1 = dao.queryFlightListfromAC(arr[0], arr[1], leaveDate);
				list2 = dao.queryFlightListfromAC(arr[1], arr[0], returnDate);
			} else if (airline.equalsIgnoreCase(FlightBookingConstants.QAN)) {
				list1 = dao.queryFlightListFromQan(arr[0], arr[1], leaveDate);
				list2 = dao.queryFlightListFromQan(arr[1], arr[0], returnDate);
			} else
				return null;
		} else if (arr.length == 3) {
			// one-stop trip
			FlightDao dao = new FlightDaoImpl();
			java.sql.Date leaveDate = java.sql.Date.valueOf(arr[2]);
			if (airline.equalsIgnoreCase(FlightBookingConstants.CEA)) {
				list1 = dao.queryFlightListFromCEA(arr[0], arr[1], leaveDate);
			} else if (airline.equalsIgnoreCase(FlightBookingConstants.AC)) {
				list1 = dao.queryFlightListfromAC(arr[0], arr[1], leaveDate);
			} else if (airline.equalsIgnoreCase(FlightBookingConstants.QAN)) {
				list1 = dao.queryFlightListFromQan(arr[0], arr[1], leaveDate);
			} else
				return null;
		}

		if (list1 != null && list2 != null) {
			for (Flight f : list2) {
				list1.add(f);
			}
			roundList = list1;
		}
		return roundList;
	}

	@Override
	public boolean orderResp(String str) {

		boolean res = false;

		String[] arr = str.split(" ");
		FlightDao flightDao;
		OrderDao orderDao;
		UserDao userDao;
		if (arr.length == 3) {
			String fid1 = arr[0];
			String fid2 = arr[1];
			String username = arr[2];

			flightDao = new FlightDaoImpl();
			orderDao = new OrderDaoImpl();
			userDao = new UserDaoImpl();

			if (fid1.toUpperCase().startsWith(FlightBookingConstants.CEA)
					&& fid2.toUpperCase().startsWith(FlightBookingConstants.CEA)) {
				if (flightDao.queryTicketExistedFromCEA(fid1)
						&& flightDao.queryTicketExistedFromCEA(fid2)
						&& userDao.queryUserExistedFromCea(username)) {
					
					if(orderDao.orderExistedFromCEA(fid1, username)||orderDao.orderExistedFromCEA(fid2, username))
						System.out.println("Order existed!");
					else{
						if (flightDao.updateTicketsToCEA(fid1)
								&& flightDao.updateTicketsToCEA(fid2)) {
							Order order1 = new Order();
							Order order2 = new Order();
							order1.setFid(fid1);
							order2.setFid(fid2);
							order1.setUsername(username);
							order2.setUsername(username);
							boolean flag1 = orderDao.saveOrderToCEA(order1);
							boolean flag2 = orderDao.saveOrderToCEA(order2);
							if (flag1 && flag2)
								res = true;
						}
					}
				}
			} 				
			else if (fid1.toUpperCase().startsWith(
						FlightBookingConstants.AC)
						&& fid2.toUpperCase().startsWith(
								FlightBookingConstants.AC)) {
				if (flightDao.queryTicketExistedFromAC(fid1)
							&& flightDao.queryTicketExistedFromAC(fid2)
							&& userDao.queryUserExistedFromAc(username)) {

						if(orderDao.orderExistedFromAC(fid1, username)||orderDao.orderExistedFromAC(fid2, username))
							System.out.println("Order existed!");
						else{
							if (flightDao.updateticketsToAC(fid1)
									&& flightDao.updateticketsToAC(fid2)) {
								Order order1 = new Order();
								Order order2 = new Order();
								order1.setFid(fid1);
								order2.setFid(fid2);
								order1.setUsername(username);
								order2.setUsername(username);
								boolean flag1 = orderDao.saveOrderToAC(order1);
								boolean flag2 = orderDao.saveOrderToAC(order2);
								if (flag1 && flag2)
									res = true;
							}
						}
				}
			}
			else if (fid1.toUpperCase().startsWith(FlightBookingConstants.QAN)
							&& fid2.toUpperCase().startsWith(FlightBookingConstants.QAN)) {
						if (flightDao.queryTicketExistedFromQan(fid1)
								&& flightDao.queryTicketExistedFromQan(fid2)
								&& userDao.queryUserExistedFromQan(username)) {

							if(orderDao.orderExistedFromQAN(fid1, username)||orderDao.orderExistedFromQAN(fid2, username))
								System.out.println("Order existed!");
							else{
								if (flightDao.updateTicketsToQan(fid1)
										&& flightDao.updateTicketsToQan(fid2)) {
									Order order1 = new Order();
									Order order2 = new Order();
									order1.setFid(fid1);
									order2.setFid(fid2);
									order1.setUsername(username);
									order2.setUsername(username);
									boolean flag1 = orderDao.saveOrderToQan(order1);
									boolean flag2 = orderDao.saveOrderToQan(order2);
									if (flag1 && flag2)
										res = true;
								}
							}
					}
			}
		}
		/**
		 * booking one-stop ticket
		 */
		else if (arr.length == 2) {
			String fid = arr[0];
			String username = arr[1];

			flightDao = new FlightDaoImpl();
			orderDao = new OrderDaoImpl();
			userDao = new UserDaoImpl();

			if (fid.toUpperCase().startsWith(FlightBookingConstants.CEA)) {
				if (flightDao.queryTicketExistedFromCEA(fid)
						&& userDao.queryUserExistedFromCea(username)) {
					if(orderDao.orderExistedFromCEA(fid, username))
						System.out.println("Oder existed!");
					else{
						if (flightDao.updateTicketsToCEA(fid)) {
							Order order = new Order();
							order.setFid(fid);
							order.setUsername(username);
							boolean flag = orderDao.saveOrderToCEA(order);
							if (flag)
								res = true;
						} 
					}
				} 
			} else if (fid.toUpperCase().startsWith(FlightBookingConstants.AC)) {
				if (flightDao.queryTicketExistedFromAC(fid)
						&& userDao.queryUserExistedFromAc(username)) {
					if(orderDao.orderExistedFromAC(fid, username))
						System.out.println("Order existed!");
					else{
						if (flightDao.updateticketsToAC(fid)) {
							Order order = new Order();
							order.setFid(fid);
							order.setUsername(username);
							boolean flag = orderDao.saveOrderToAC(order);
							if (flag)
								res = true;
						} 
					}
				} 
			} else if (fid.toUpperCase().startsWith(FlightBookingConstants.QAN)) {
				if (flightDao.queryTicketExistedFromQan(fid)
						&& userDao.queryUserExistedFromQan(username)) {
					if(orderDao.orderExistedFromQAN(fid, username))
						System.out.println("Order existed!");
					else{
						if (flightDao.updateTicketsToQan(fid)) {
							Order order = new Order();
							order.setFid(fid);
							order.setUsername(username);
							boolean flag = orderDao.saveOrderToQan(order);
							if (flag)
								res = true;
						} 
					}
				}
			} 
		}
		return res;
	}

	@Override
	public boolean regResp(String str) {
		boolean flag = false;
		String[] arr = str.split(" ");
		if (arr.length == 5) {
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
			if (air.equalsIgnoreCase(FlightBookingConstants.QAN)) {
				if (!dao.queryUserExistedFromQan(username)) {
					if (dao.insertUserToQan(user)) {
						System.out
								.println("----insert a new user successfully------");
						flag = true;
					}
				}
			} else if (air.equalsIgnoreCase(FlightBookingConstants.CEA)) {
				if (!dao.queryUserExistedFromCea(username)) {
					if (dao.insertUserToCea(user)) {
						System.out
								.println("----insert a new user successfully------");
						flag = true;
					}
				}
			} else if (air.equalsIgnoreCase(FlightBookingConstants.AC)) {
				if (!dao.queryUserExistedFromAc(username))
					if (dao.insertUserToAc(user)) {
						System.out
								.println("----insert a new user successfully------");
						flag = true;
					}
			}
		}
		return flag;
	}

	@Override
	public List<Order> checkOrders(String str) {
		String[] arr = str.split(" ");
		if (arr.length == 2) {
			String airline = arr[0];
			String username = arr[1];
			List<Order> list = new ArrayList<Order>();
			OrderDao dao = new OrderDaoImpl();
			UserDao userDao = new UserDaoImpl();
			if (airline.equalsIgnoreCase(FlightBookingConstants.CEA)) {
				if (userDao.queryUserExistedFromCea(username))
					list = dao.checkOrderFromCEA(username);
				else
					System.out.println("Cannot found this user!");
			} else if (airline.equalsIgnoreCase(FlightBookingConstants.AC)) {
				if (userDao.queryUserExistedFromAc(username))
					list = dao.checkOrderFromAC(username);
				else
					System.out.println("Cannot found this user!");
			} else if (airline.equalsIgnoreCase(FlightBookingConstants.QAN)) {
				if (userDao.queryUserExistedFromQan(username))
					list = dao.checkOrderFromQan(username);
				else
					System.out.println("Cannot found this user!");
			} else
				list = null;
			return list;
		} else {
			return null;
		}
	}

}
