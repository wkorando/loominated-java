package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class StructuredConcurrencyTimeoutWithPredicate {

	public static void main(String... args) throws Throwable {

		System.out.println(
				"Thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
		var instance = new StructuredConcurrencyTimeoutWithPredicate();
		String results = instance.runTasks();
		System.out.println(results);
	}

	private String runTasks() throws Throwable {
		try (var scope = StructuredTaskScope
				.<String, Stream<Subtask<String>>>open(Joiner.allUntil(new Timeout<String>()))) {
			scope.fork(() -> CommonUtils.task("A", 10000));
			scope.fork(() -> CommonUtils.task("B", 500));
			scope.fork(() -> CommonUtils.task("C", 500));
			scope.fork(() -> CommonUtils.task("D", 500));

			return scope.join().filter(s -> s.state() == State.SUCCESS).map(Subtask::get)
					.collect(Collectors.joining(", ", "{ ", " }"));

		} catch (Exception e) {
			throw e;
		}

	}

	class Timeout<T> implements Predicate<Subtask<? extends T>> {
		@Override
		public boolean test(Subtask<? extends T> subtask) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		}
	}
}
