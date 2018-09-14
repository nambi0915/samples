package main;

public class Choc {
	public static void main(String args[]) {
		int chocolateBar = 0;
		int chocolateCoupon = 0;
		int money = 50;
		/*
		 * cb = cb + money; cp = money; while (cp >= 6) { cb += cp / 6;
		 * System.err.println(cb); cp = cp % 6 + cp / 6; System.err.println(cp); }
		 */
		for (int i = 1; i <= money; i++) {
			chocolateBar++;
			chocolateCoupon++;
			if (chocolateCoupon == 6) {
				chocolateBar++;
				chocolateCoupon++;
				chocolateCoupon -= 6;
			}
		}
		System.err.println("Chocolates: " + chocolateBar + "\nCoupons:  " + chocolateCoupon);
	}
}