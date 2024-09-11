package step4;

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

public class StructuredConcurrencyCustom {

	public static void main(String... args) throws Throwable {

//		WebServiceHelper.continueExecuting("Press enter to continue.");
		System.out.println(
				"Thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
		var instance = new StructuredConcurrencyCustom();
		String results = instance.callWebServices();
		System.out.println(results);

//		WebServiceHelper.continueExecuting("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		try (var scope = StructuredTaskScope
				.<String, Stream<Subtask<String>>>open(Joiner.allUntil(new CancelAfterTwoFailures<String>()))) {
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			return scope.join().map(Subtask::get)
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
