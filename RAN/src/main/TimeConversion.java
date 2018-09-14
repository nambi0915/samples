package main;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class TimeConversion {
	public static void main(String[] args) throws ParseException {
		Date date = null;
		SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String ts = "2018-06-30T23:59:55Z";

		try {
			date = s2.parse(ts);
			// System.out.println("parsing s2");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		Timestamp start = new java.sql.Timestamp(date.getTime());
		Timestamp end = new java.sql.Timestamp(date.getTime());
		LocalDateTime loc = start.toLocalDateTime();
		LocalDateTime endLocalTime = loc.plusSeconds(10);

		end = Timestamp.valueOf(endLocalTime);
		/*
		 * String start1 = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(start); String end1 = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(end);
		 */String hr = "1";
		int a = hr.compareTo(Integer.toString(9));
		System.out.println(a);
		// System.out.println(start1 + " " + end1);
	}
}
