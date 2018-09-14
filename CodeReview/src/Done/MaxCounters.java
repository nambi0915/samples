package Done;

import java.util.Arrays;

public class MaxCounters {
	public static void main(String[] args) {
		int[] A = { 3, 4, 4, 6, 1, 4, 4 };
		int N = 5;
		int maxCounter = 0;
		int addToCounters = 0;
		int[] counters = new int[N];
		for (int i = 0; i < A.length; i++) {
			if (A[i] > N) {
				addToCounters += maxCounter;
				maxCounter = 0;
				counters = new int[N];
			} else {
				int added = ++counters[A[i] - 1];
				maxCounter = Math.max(added, maxCounter);
			}
		}
		System.err.println(addToCounters);
		for (int i = 0; i < counters.length; i++)
			counters[i] += addToCounters;
		System.err.println(Arrays.toString(counters));
	}
}