package step4;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

			Subtask<String> task = scope.fork(() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(50000000);
				result = "a";
				System.out.println(result);
				return result;
			});
			scope.fork(() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(5000000);
				result = "b";
				System.out.println(result);
				return result;
			});
			scope.fork(() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(1000000000);
				result = "c";
				System.out.println(result);
				return result;
			});
			return scope.join().map(f -> f.get()).collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}
