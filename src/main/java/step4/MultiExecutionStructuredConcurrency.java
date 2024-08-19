package step4;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fly.us.WebServiceHelper;

public class MultiExecutionStructuredConcurrency {

	public static void main(String... args) throws Throwable {

//		WebServiceHelper.waitForUser("Press enter to continue.");

		final var instance = new MultiExecutionStructuredConcurrency();
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			var tasks = executor.invokeAll(List.<Callable<String>>of(() -> {
				return instance.callWebServices();
			}, () -> {
				return instance.callWebServices();
			}, () -> {
				return instance.callWebServices();
			}));

			System.out.println(tasks.stream().map(Future::resultNow).collect(Collectors.joining(", ", "{ ", " }")));
		}

//		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Exception {
		try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow())) {
			scope.fork(() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(1000000000);
				result = "a";
				System.out.println(result);
				return result;
			});
			scope.fork(() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(1000000000);
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