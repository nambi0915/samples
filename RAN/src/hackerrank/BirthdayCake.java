package hackerrank;

import java.util.Arrays;
import java.util.List;

public class BirthdayCake {
	static int birthday(List<Integer> s, int d, int m) {
		int possibleWays = 0;

		for (int i = 0; i < s.size() && (i != s.size() - m || m == s.size()); i++) {
			if (s.subList(i, i + m).stream().mapToInt(Integer::intValue).sum() == d) {
				possibleWays++;
			}
		}
		return possibleWays;
	}

	public static void main(String args[]) {
		Integer[] array = { 4 };

		System.out.println(birthday(Arrays.asList(array), 4, 1));
	}
}
