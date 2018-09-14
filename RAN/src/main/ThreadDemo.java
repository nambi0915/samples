package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class ThreadDemo implements Runnable {
	public String checker;
	public String name;
	public String start;
	public String end;
	public String startPlusOneHour;

	ThreadDemo(String s, String hr) {
		name = s;
		int a = hr.length();
		if (a == 2) {
			start = "2018-06-25T" + hr + ":00:00Z";
			end = "2018-06-25T" + hr + ":00:10Z";
		} else {
			start = "2018-06-25T0" + hr + ":00:00Z";
			end = "2018-06-25T0" + hr + ":00:10Z";
		}
		startPlusOneHour = addOneHour(this.start);
		System.out
				.println("Start:  " + start + "  End  " + end + "  name:  " + name + "  Ends at  " + startPlusOneHour);

	}

	public static void write(File file, ThreadDemo obj) {
		try {
			FileWriter fw = new FileWriter(file, true);
			// System.out.println(this.name);
			fw.write(obj.name + "\t " + "Start time:" + obj.start + "\t" + "End time:" + obj.end + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateTime(ThreadDemo obj) {
		Date date = null;
		SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String ts = obj.end;

		try {
			date = s2.parse(ts);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		Timestamp startTime = new java.sql.Timestamp(date.getTime());
		Timestamp endTime = new java.sql.Timestamp(date.getTime());
		LocalDateTime loc = startTime.toLocalDateTime();
		LocalDateTime endLocalTime = loc.plusSeconds(10);
		endTime = Timestamp.valueOf(endLocalTime);

		obj.start = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(startTime);
		obj.end = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(endTime);
		// return obj;
	}

	public static String addOneHour(String start) {
		Date date = null;
		SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String ts = start;

		try {
			date = s2.parse(ts);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		Timestamp startTime = new java.sql.Timestamp(date.getTime());
		LocalDateTime loc = startTime.toLocalDateTime();
		loc = loc.plusHours(1);
		startTime = Timestamp.valueOf(loc);

		start = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(startTime);
		return start;
	}

	public void run() {
		// String nameT = Thread.currentThread().getName();
		// System.out.println("name:" + name);
		String Tname = Thread.currentThread().getName();

		File file = new File("C:\\Users\\lenovo\\Documents\\Nambi\\Threads\\" + Tname + ".csv");
		while (!(start.equals(startPlusOneHour))) {
			write(file, this);
			updateTime(this);
		}
		System.out.println("Data written for thread " + Tname);
	}

	public static void main(String args[]) {
		// String[] str = { "Thread one", "Thread two", "Thread three", "Thread four",
		// "Thread five" };
		for (int i = 0; i < 24; i++) {
			String name = Integer.toString(i);
			Thread t1 = new Thread(new ThreadDemo("Thread " + name, name), name);
			t1.start();
		}

	}
}