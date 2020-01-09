package hackerrank;

import java.util.ArrayList;
import java.util.List;

public class DiagonalDifference {
	public static int diagonalDifference(List<List<Integer>> arr) {
		int leftDiagonal = 0;
		int rightDiagonal = 0;
		int matrixLength = arr.size();

		for (int i = 0; i < matrixLength; i++) {
			for (int j = 0; j < matrixLength; j++) {
				if (i == j) {
					leftDiagonal = leftDiagonal + arr.get(i).get(j);
				}
				if (i == matrixLength - j - 1) {
					rightDiagonal = rightDiagonal + arr.get(i).get(j);
				}
			}
		}

		return Math.abs(rightDiagonal - leftDiagonal);

	}

	public static void main(String args[]) {
		List<List<Integer>> arr = new ArrayList<List<Integer>>();
		List<Integer> row1 = new ArrayList<Integer>();
		List<Integer> row2 = new ArrayList<Integer>();
		List<Integer> row3 = new ArrayList<Integer>();
		row1.add(1);
		row1.add(2);
		row1.add(3);

		row2.add(4);
		row2.add(5);
		row2.add(6);

		row3.add(9);
		row3.add(8);
		row3.add(9);

		arr.add(row1);
		arr.add(row2);
		arr.add(row3);

		int difference = diagonalDifference(arr);
		System.out.println(difference);
	}
}
