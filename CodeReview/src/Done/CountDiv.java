package Done;

public class CountDiv {
	public static void main(String[] args) {
		int A = 6, B = 6, K = 2, div = 0;
		for (int i = A; i <= B; i++) {
			if (i % K == 0)
				div++;
		}
		System.err.println(div);
	}
}
