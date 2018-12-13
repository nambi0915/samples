package Done;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DistinctElementArray {
	public static void main(String args[]) {
		int[] ab = { 1, 1, 2, 2, 3, 1 };
		List<Integer> you = Arrays.stream(ab).boxed().collect(Collectors.toList());
		System.err.println(new HashSet<Integer>(you).size());
	}
}
