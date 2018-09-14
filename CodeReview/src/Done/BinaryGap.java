package Done;

public class BinaryGap {
	public static void main(String args[]) {
		int num = 1041;
		String a = Integer.toBinaryString(num);
		System.err.println(a);
		int len = 0;
		int max = 0;
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) == '1') {
				if (len >= max)
					max = len;
				len = 0;
			} else
				len++;
		}
		System.err.println(max);
	}
}
