package step3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;

import common.CommonUtils;

public class Part12AwaitAllSuccessfulOrThrow {

	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new Part12AwaitAllSuccessfulOrThrow();
		instance.runTasks();

	}

	private void runTasks() throws Throwable {
		Joiner<String, Void, ExecutionException> joiner = Joiner.awaitAllSuccessfulOrThrow();

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
