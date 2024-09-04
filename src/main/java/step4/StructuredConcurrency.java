package step4;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class StructuredConcurrency {

	public static void main(String... args) throws Throwable {

//		WebServiceHelper.waitForUser("Press enter to continue.");

		var instance = new StructuredConcurrency();
		String results = instance.callWebServices();
		System.out.println(results);

//		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		Joiner<String, Stream<Subtask<String>>> joiner = Joiner.allSuccessfulOrThrow();
		try (var scope = StructuredTaskScope.open(joiner)) {

			Subtask<String> task = scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			return scope.join().map(f -> f.get()).collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}
