package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class AllUntilTwoFailures {

	public static void main(String... args) throws Throwable {

//		WebServiceHelper.continueExecuting("Press enter to continue.");
		System.out.println(
				"Thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
		var instance = new AllUntilTwoFailures();
		String results = instance.runTasks();
		System.out.println(results);

//		WebServiceHelper.continueExecuting("Press enter to exit.");
	}

	private String runTasks() throws Throwable {
		try (var scope = StructuredTaskScope
				.<String, Stream<Subtask<String>>>open(Joiner.allUntil(new CancelAfterTwoFailures<String>()))) {
			scope.fork(() -> CommonUtils.task("A", 500, true));
			scope.fork(() -> CommonUtils.task("B", 1500, true));
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

class CancelAfterTwoFailures<T> implements Predicate<Subtask<? extends T>> {
	private final AtomicInteger failedCount = new AtomicInteger();

	@Override
	public boolean test(Subtask<? extends T> subtask) {
		return subtask.state() == Subtask.State.FAILED 
				&& failedCount.incrementAndGet() >= 2;
	}
}
