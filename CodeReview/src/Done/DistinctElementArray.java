package Done;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DistinctElementArray {
	public static void main(String args[]) {
		int[] ab = { 1, 1, 2, 2, 3, 1 };

		System.err.println(Arrays.stream(ab).distinct().boxed().collect(Collectors.toList()).size());

		IntPredicate i = (x) -> x == 3;
		// Creating a List using stream range..
		List<Integer> number = IntStream.rangeClosed(1, 1000).boxed().collect(Collectors.toList());
		// Reverse sort using stream
		List<Integer> reverse = number.stream().sorted((a, b) -> b.compareTo(a)).collect(Collectors.toList());
		// Sum of LIst using stream
		Integer sum = number.stream().reduce(0, (a, b) -> a + b);
		System.err.println(sum);
		Collections.sort(number);
		List<Integer> f50 = number.stream().filter((d) -> d <= 50).collect(Collectors.toList());
		System.err.println(f50);

		Predicate<String> predicate = (a) -> Pattern.compile("[a-zA-Z]").matcher(a).find();
		List<String> str = Arrays.asList(new String[] { "a", "b", "c", "d", "e", "f" });
		System.err.println(str.stream().allMatch(predicate));

		// Stream builder
		Stream.Builder<String> builder = Stream.builder();
		str.stream().map(a -> a.concat("1")).forEachOrdered(builder::accept);
		List<String> str1 = builder.add("g1").build().collect(Collectors.toList());
		System.err.println(str1);

		List<Double> doubleList = Arrays.stream(ab).asDoubleStream().boxed().collect(Collectors.toList());
		System.err.println(doubleList);

		Supplier<String> supplier = () -> "go";
		List<String> gen = Stream.generate(supplier).limit(5).map(s -> s.concat("1")).collect(Collectors.toList());
		System.err.println(gen);

		List<String> generate = IntStream.range(0, 5).mapToObj(it -> "go".concat(Integer.toString(it)))
				.collect(Collectors.toList());
		System.err.println(generate);

		System.err.println(Arrays.stream(ab).anyMatch(i));
	}
}
