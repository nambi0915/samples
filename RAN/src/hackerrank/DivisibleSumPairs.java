package hackerrank;

public class DivisibleSumPairs {
	public static void main(String args[]) {
		int[] ar = new int[] { 1, 3, 2, 6, 1, 2 };
		int count = 0, n = 6, k = 3;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i < j && (ar[i] + ar[j]) % k == 0) {
					count++;
				}
			}
		}
		System.out.println(count);
	}
}
