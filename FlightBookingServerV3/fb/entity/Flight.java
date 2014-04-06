package fb.entity;

import java.util.Date;

public class Flight {
	private String fid;
	private String airline_comp;
	private String departure_city;
	private String destination_city;
	private Date departing_date;
	private Date returning_date;
	private int tickets;
	private int price;
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getTickets() {
		return tickets;
	}
	public void setTickets(int tickets) {
		this.tickets = tickets;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getAirline_comp() {
		return airline_comp;
	}
	public void setAirline_comp(String airline_comp) {
		this.airline_comp = airline_comp;
	}
	public String getDeparture_city() {
		return departure_city;
	}
	public void setDeparture_city(String departure_city) {
		this.departure_city = departure_city;
	}
	public String getDestination_city() {
		return destination_city;
	}
	public void setDestination_city(String destination_city) {
		this.destination_city = destination_city;
	}
	public Date getDeparting_date() {
		return departing_date;
	}
	public void setDeparting_date(Date departing_date) {
		this.departing_date = departing_date;
	}
	public Date getReturning_date() {
		return returning_date;
	}
	public void setReturning_date(Date returning_date) {
		this.returning_date = returning_date;
	}
	
	
}
