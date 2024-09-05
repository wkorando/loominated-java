package step4;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class RollbackStructuredConcurrency {

	public static void main(String... args) throws Throwable {

//		WebServiceHelper.waitForUser("Press enter to continue.");

		var instance = new RollbackStructuredConcurrency();
		String results = instance.callWebServices();
		System.out.println(results);

//		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		Joiner<String, Stream<Subtask<String>>> joiner = Joiner.allSuccessfulOrThrow();
		try (var scope = StructuredTaskScope.open(joiner)) {

			Subtask<String> task1 = scope.fork(() -> CommonUtils.task("A", 500));
			Subtask<String> task2 = scope.fork(() -> CommonUtils.task("A", 500));
			Subtask<String> task3 = scope.fork(() -> CommonUtils.task("A", 500));
			try {
				return scope.join().map(f -> f.get()).collect(Collectors.joining(", ", "{ ", " }"));
			} catch (Exception ee) {
				throw ee;
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
