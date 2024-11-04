package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class AllUntilReadValue {

	public static void main(String... args) throws Throwable {
		System.out.println(
				"Thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
		var instance = new AllUntilReadValue();
		String results = instance.runTasks();
		System.out.println(results);
	}

	private String runTasks() throws Throwable {
		try (var scope = StructuredTaskScope
				.<String, Stream<Subtask<String>>>open(Joiner.allUntil(new ReadValues<String>()))) {
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1500));
			scope.fork(() -> CommonUtils.task("C", 2500));
			scope.fork(() -> CommonUtils.task("D", 3500));
			scope.fork(() -> CommonUtils.task("E", 4500));
			return scope.join().filter(s -> s.state() == State.SUCCESS). map(Subtask::get)
					.collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}

class ReadValues<T> implements Predicate<Subtask<? extends T>> {
	private final AtomicInteger failedCount = new AtomicInteger();

	@Override
	public boolean test(Subtask<? extends T> subtask) {
		if(subtask.state() == Subtask.State.SUCCESS) {
			return subtask.get().equals("""
					{
						"value" : "C"
					}
					""");
		}
		return subtask.state() == Subtask.State.FAILED 
				&& failedCount.incrementAndGet() >= 2;
	}
}
