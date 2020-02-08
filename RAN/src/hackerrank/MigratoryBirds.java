package hackerrank;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class MigratoryBirds {
	static int migratoryBirds(List<Integer> arr) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (Integer ar : arr) {
			map.put(ar, map.getOrDefault(ar, 0) + 1);
		}
		int maxCount = 0;
		int birdNumber = 0;

		System.out.println(map);
		for (Entry<Integer, Integer> entry : map.entrySet()) {

			if (maxCount == 0 || entry.getValue() > maxCount
					|| (entry.getValue() == 0 && entry.getKey() < birdNumber)) {
				maxCount = entry.getValue();
				birdNumber = entry.getKey();
			}
		}

		return birdNumber;

	}

	public static void main(String args[]) {
		Integer[] sight = { 1, 2, 3, 4, 5, 4, 3, 2, 1, 3, 4 };

		System.out.println(migratoryBirds(Arrays.asList(sight)));
	}
}
