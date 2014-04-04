package fb.demo;

import java.util.List;

import fb.dao.FlightDao;
import fb.dao.impl.FlightDaoImpl;
import fb.entity.Flight;

public class Demo5 {
	public static void main(String[] args) {
		String[] city = {"shanghai","melbourne"};
		List<Flight> list,list2;
		if (city.length == 2) {
			FlightDao dao = new FlightDaoImpl();
			list = dao.queryFlightListFromQan(city[0], city[1]);
			
			for (Flight f : list) {
				System.out.println(f.getFid());
			}
			System.out.println("===============");
			list2 = dao.queryFlightListFromQan(city[1], city[0]);
			for (Flight f : list2) {
				System.out.println(f.getFid());
			}
			System.out.println("================");
			for (Flight f : list2) {
				list.add(f);
			}
			for (Flight f : list) {
				System.out.println(f.getFid());
			}
		} else {
			return;
		}
	}
}
