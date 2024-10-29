package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class AwaitAllSuccessfulOrThrow {

	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new AwaitAllSuccessfulOrThrow();
		instance.runTasks();

	}

	private void runTasks() throws Throwable {
		Joiner<String, Void> joiner = Joiner.awaitAllSuccessfulOrThrow();

		try (var scope = StructuredTaskScope.open(joiner)) {

			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1500));
			scope.fork(() -> CommonUtils.task("C", 1000));

			scope.join();

		} catch (Exception e) {
			throw e;
		}
	}

}
