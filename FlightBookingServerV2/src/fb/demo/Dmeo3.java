package fb.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dmeo3 {
	public static void main(String[] args) {
		
		Date date = new Date();
		System.out.println(date);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int i = 1;
		String str = "2014-03-"+i;
		try {
			Date departing_date = sdf.parse(str);
			System.out.println(departing_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}	
