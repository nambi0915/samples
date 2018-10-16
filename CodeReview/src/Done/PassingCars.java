package Done;

public class PassingCars {
	public static void main(String[] args) {
		int[] A = { 0, 1, 0, 1, 1, 0, 1 };
		int counter = 0;
		int pairs = 0;
		for (int i = 0; i < A.length; i++) {
			if (A[i] == 0) {
				counter++;
			} else {
				pairs += counter;
			}
		}
		System.err.println(Math.abs(pairs));

	}
}
