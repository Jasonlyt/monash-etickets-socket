package fb.demo;

import java.util.List;

import org.junit.Test;

import fb.dao.FlightDao;
import fb.dao.impl.FlightDaoImpl;
import fb.entity.Flight;

public class Demo4 {
	@Test
	public void listRequest() {
		FlightDao dao = new FlightDaoImpl();
		List<?> list = dao.listFlights();
		System.out.println(list.size());
		build(list);

	}

	private void build(List<?> list) {
		String msg = "";
		for (int i = 0; i < list.size(); i++) {
			Flight f = (Flight) list.get(i);
			msg = f.getFid()+":"+f.getAirline_comp();
			System.out.println(msg);
			//writer.print(msg+FlightBookingConstants.CR_LF);
		}
	}
}
