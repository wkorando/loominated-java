package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.TimeUnit;

import common.CommonUtils;

public class StructuredConcurrencyShutdownOnSuccess {

	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");

		StructuredConcurrencyShutdownOnSuccess instance = new StructuredConcurrencyShutdownOnSuccess();
		String result = instance.runTasks();
		System.out.println(result);

		CommonUtils.waitForUser("Press enter to exit.");
	}

	private String runTasks() throws Throwable {
		try (var scope = StructuredTaskScope.<String, String>open(Joiner.anySuccessfulResultOrThrow())) {

			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1500));
			scope.fork(() -> CommonUtils.task("C", 1000));
			return scope.join();
		} catch (Exception e) {
			throw e;
		}
	}

}
