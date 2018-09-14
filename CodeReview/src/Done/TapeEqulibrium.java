package Done;

import java.util.Arrays;

public class TapeEqulibrium {
	public static void main(String args[]) {
		int[] A = { 1, 2, 3, -4, 5 };
		int leftsum = Arrays.stream(A).sum();
		int rightsum = 0;
		int maxDiff = Integer.MAX_VALUE;
		for (int i = A.length - 1; i > 0; i--) {
			leftsum -= A[i];
			rightsum += A[i];
			// System.err.println(leftsum);
			// System.err.println(rightsum);
			int diff = Math.abs(leftsum - rightsum);
			// System.err.println(diff);
			if (diff == 0)
				System.err.println(diff);
			if (diff < maxDiff)
				maxDiff = diff;
		}
		System.err.println(maxDiff);
	}
}
