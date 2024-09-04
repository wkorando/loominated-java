package step4;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fly.us.WebServiceHelper;

import common.CommonUtils;

public class StructuredConcurrencyShutdownOnError {
	public static void main(String... args) throws Throwable {

		WebServiceHelper.waitForUser("Press enter to continue.");

		StructuredConcurrencyShutdownOnError instance = new StructuredConcurrencyShutdownOnError();
		String results = instance.callWebServices();
		System.out.println(results);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow())) {
			
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("A", 500));

			return scope.join().map(Subtask::get)
					.collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}
