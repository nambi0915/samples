package demo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaUtilStream {
	public static void main(String args[]) {
		List<Integer> myList = new ArrayList<>();
		for (int i = 0; i < 100; i++)
			myList.add(i);
		Function<Integer, Integer> supplier = t -> {
			return t *= 2;
		};
		Stream<Integer> seq = myList.stream().filter(p -> p < 30);

		// Converting stream to List
		List<Integer> st = seq.collect(Collectors.toList());
		Stream<Integer> seq1 = myList.stream().filter(p -> p < 10);

		// Converting stream to Map
		Map<Integer, Integer> strMap = seq1.collect(Collectors.toMap(i -> i, i -> i * 2));

		// Stream to Array
		Stream<Integer> intStream = Stream.of(1, 2, 3, 4);
		Integer[] intArray = intStream.toArray(Integer[]::new);
		System.out.println(Arrays.toString(intArray));
		System.err.println(strMap.toString());
		Stream<Integer> par = myList.parallelStream().filter(p -> p < 30);

		// seq.forEach(p -> System.out.println(supplier.apply(p)));
	}

}
