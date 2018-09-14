package Done;

import java.util.HashSet;
import java.util.Set;

public class RiverJump {
	public static void main(String args[]) {
		Integer[] A = { 1, 3, 2, 5, 1, 3 };
		int X = 5;

		Set<Integer> s = new HashSet<Integer>();
		for (int i = 0; i < A.length; i++) {
			s.add(A[i]);
			if (s.size() == X) {
				System.err.println(i);
				break;
			}
		}
		System.err.println(-1);
	}
}
