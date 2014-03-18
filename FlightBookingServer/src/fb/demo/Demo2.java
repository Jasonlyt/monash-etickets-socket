package fb.demo;

import java.util.List;

import fb.dao.FlightDao;
import fb.dao.impl.FilghtDaoImpl;
import fb.entity.Flight;

public class Demo2 {
	public static void main(String[] args) {
		FlightDao dao = new FilghtDaoImpl();
		List<?> flightList = dao.queryFlightList("Shanghai", "Melbourne");
		System.out.println(flightList.size());
		System.out.println(flightList);
		String rs = build(flightList);
		System.out.println(rs);
	}
	
	private static String build(List<?> list){
		String msg = "";
		for (int i = 0; i < list.size(); i++) {
			Flight f = (Flight) list.get(i);
			msg += f.getFid()+":"+f.getAirline_comp()+"\n";
		}
		return msg;
	}
}
