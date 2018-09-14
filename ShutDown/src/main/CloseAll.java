package main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CloseAll {
	@SuppressWarnings("resource")
	public static void main(String args[]) throws InterruptedException {
		try {
			Process process;
			process = Runtime.getRuntime().exec("powershell \"gps | where {$_.MainWindowTitle } | select Id");
			Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\\n");
			StringBuilder sb = new StringBuilder();
			while (scanner.hasNext())
				sb.append(scanner.next().trim() + "\n");
			scanner.close();
			// System.err.println(sb.toString());
			List<String> ids = Arrays.asList(sb.toString().split("\\n"));
			for (String id : ids) {
				if (id.matches("\\d+")) {
					Runtime.getRuntime().exec("taskkill /PID " + id + " /F");
				}
			}
			Thread.sleep(5000);
			// Runtime.getRuntime().exec("shutdown /s");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
