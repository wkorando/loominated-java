package step3;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class StructuredConcurrencyTimeoutWithConfig {

	public static void main(String... args) throws Throwable {
		System.out.println(
				"Thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
		var instance = new StructuredConcurrencyTimeoutWithConfig();
		String results = instance.runTasks();
		System.out.println(results);
	}

	private String runTasks() throws Throwable {
		try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow(),
				cf -> cf.withTimeout(Duration.ofMillis(1000L)))) {
			scope.fork(() -> CommonUtils.task("A", 10000));
			scope.fork(() -> CommonUtils.task("B", 500));
			scope.fork(() -> CommonUtils.task("C", 500));
			scope.fork(() -> CommonUtils.task("D", 500));

			return scope.join().map(Subtask::get).collect(Collectors.joining(", ", "{ ", " }"));

		} catch (Exception e) {
			throw e;
		}

	}

}
