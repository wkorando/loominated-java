package step4;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.TimeUnit;

import common.CommonUtils;

public class StructuredConcurrencyShutdownOnSuccess {

	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");

		StructuredConcurrencyShutdownOnSuccess instance = new StructuredConcurrencyShutdownOnSuccess();
		String result = instance.callWebServices();
		System.out.println(result);

		CommonUtils.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		try (var scope = StructuredTaskScope.<String, String>open(Joiner.anySuccessfulResultOrThrow())) {

			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			return scope.join();
		} catch (Exception e) {
			throw e;
		}
	}

}
