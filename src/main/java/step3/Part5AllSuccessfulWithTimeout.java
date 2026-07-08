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

public class Part5AllSuccessfulWithTimeout {

	public static void main(String... args) throws Throwable {
		var instance = new Part5AllSuccessfulWithTimeout();
		String results = instance.runTasks();
		System.out.println(results);
	}

	private String runTasks() throws Throwable {
		try (var scope = StructuredTaskScope.open(Joiner.<String>allSuccessfulOrThrow(), cf -> cf.withTimeout(Duration.ofMillis(1000L)))) {
			scope.fork(() -> CommonUtils.task("A", 10000));
			scope.fork(() -> CommonUtils.task("B", 500));
			scope.fork(() -> CommonUtils.task("C", 500));
			scope.fork(() -> CommonUtils.task("D", 500));

			return scope.join().stream().collect(Collectors.joining(", ", "Combined results:\n {\n{ ", " }"));

		} catch (Exception e) {
			throw e;
		}

	}

}
