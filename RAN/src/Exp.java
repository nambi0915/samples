import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Exp {

	public static void main(String args[]) {

		forEachAndConsumerDemo();

	}

	private static void forEachAndConsumerDemo() {

		// accept() method implemented using lambda expression
		Consumer<Integer> consumer = t -> {
			System.err.println(t + 1);
			System.err.println(t + 2);
		};

		Consumer<Integer> consumer2 = t -> {
			System.err.println("consumer2");
		};
		// implementing andThen() method using consumer object
		Consumer<Integer> consumerWithAndThen = consumer.andThen(i -> System.err.println("printed"));
		List<Integer> list1 = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++)
			list1.add(i + 1);

		for (Integer i : list1) {
			consumer.accept(i);
		}

		// traversing temporarily
		list1.forEach(new Consumer<Integer>() {
			public void accept(Integer t) {
				// System.err.println(t + 1);
			}

		});

		// reusable function
		list1.forEach(consumerWithAndThen);
		// System.err.println(list1);
		list1.forEach(consumer.andThen(consumer2));

	}

}
