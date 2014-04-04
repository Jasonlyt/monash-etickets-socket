package fb.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fb.dao.FlightDao;
import fb.dao.impl.FlightDaoImpl;
import fb.entity.Flight;

public class Demo2 {
	public static void main(String[] args) {
		FlightDao dao = new FlightDaoImpl();
		Flight flight = new Flight();
		Flight flight2 = new Flight();

		String[] city = { "Melbourne", "Sydney", "Shanghai", "Beijing" };
		String[] air = {"China Eastern Airlines","Airline China","Qantas"};

		String airline = air[1];
		String fid,fid2;
		Date departing_date;
		int tickets;
		int price;

		for (int i = 1; i < 31; i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			String departure_city = city[2];
			String destination_city = city[0];
			String toCity = city[1];
			String str = "2014-03-" + i;
			try {
				departing_date = sdf.parse(str);
				java.sql.Date sqlDate = new java.sql.Date(departing_date.getTime());

				
				if (i < 10) {
					fid = "AC03010" + i;
					fid2 = "AC03020" + i;
				} else {
					fid = "AC0301" + i;
					fid2 = "AC0302"+i;
				}

				tickets = 30;
				price = 100;
				flight.setFid(fid);
				flight.setAirline_comp(airline);
				flight.setDeparture_city(departure_city);
				flight.setDestination_city(destination_city);
				flight.setDeparting_date(sqlDate);
				flight.setPrice(price);
				flight.setTickets(tickets);

				flight2.setFid(fid2);
				flight2.setAirline_comp(airline);
				flight2.setDeparture_city(departure_city);
				flight2.setDestination_city(toCity);
				flight2.setDeparting_date(sqlDate);
				flight2.setPrice(price);
				flight2.setTickets(tickets);

				boolean flag = dao.insertFlightToAC(flight);
				boolean flag2 = dao.insertFlightToAC(flight2);
				if(flag&&flag2)
					System.out.println(i+"-->done");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}
}
