package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class AllSuccessfulOrThrow {

	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new AllSuccessfulOrThrow();
		String results = instance.runTasks();
		System.out.println(results);
	}

	private String runTasks() throws Throwable {
		Joiner<String, Stream<Subtask<String>>> allSuccessful = Joiner.allSuccessfulOrThrow();

		try (var scope = StructuredTaskScope.open(allSuccessful)) {

			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1500));
			scope.fork(() -> CommonUtils.task("C", 1000));
			
			return scope.join().map(f -> f.get()).collect(Collectors.joining(", ", "{ ", " }"));

		} catch (InterruptedException e) {
			throw e;
		}
	}

}
