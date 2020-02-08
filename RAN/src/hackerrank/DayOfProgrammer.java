package hackerrank;

public class DayOfProgrammer {
	static String dayOfProgrammer(int year) {

		if (year <= 1917) {
			return year % 4 == 0 ? "12.09." + year : "13.09." + year;
		} else if (year >= 1919) {
			return (year % 400 == 0) || ((year % 4 == 0) && !(year % 100 == 0)) ? "12.09." + year : "13.09." + year;
		} else {
			return "26.08.1918";
		}
	}

	public static void main(String args[]) {

		System.out.println(dayOfProgrammer(1921));
		System.out.println(dayOfProgrammer(2016));
		System.out.println(dayOfProgrammer(1918));

	}
}
