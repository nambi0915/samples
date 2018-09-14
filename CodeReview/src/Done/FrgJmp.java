package Done;

import java.math.BigDecimal;
import java.math.MathContext;

public class FrgJmp {
	public static void main(String args[]) {
		int X = 3, Y = 999111321, D = 7;
		// System.err.println(Y - X);
		MathContext mc = new MathContext(10);
		BigDecimal xBd = new BigDecimal(X);
		BigDecimal yBd = new BigDecimal(Y);
		BigDecimal zBd = new BigDecimal(D);
		BigDecimal yMinusX = yBd.subtract(xBd);
		BigDecimal yMinusXdivD = yMinusX.divide(zBd, mc);
		BigDecimal integerResult = new BigDecimal(yMinusXdivD.toBigInteger());
		// System.err.println(intbd);
		// System.err.println(bd);
		BigDecimal result = yMinusXdivD.compareTo(integerResult) > 0 ? integerResult.add(new BigDecimal(1)) : integerResult;
		int answer = Integer.parseInt(result.toString());
		System.err.println(answer);
	}
}