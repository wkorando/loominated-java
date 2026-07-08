package step3;

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

import common.CommonUtils;

public class Part11MultiExecutionStructuredConcurrency {

	public static void main(String... args) throws Throwable {

//		CommonUtils.waitForUser("Press enter to continue.");

		final var instance = new Part11MultiExecutionStructuredConcurrency();
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

			executor.invokeAll(List.<Callable<String>>of(() -> {
				return instance.callWebServices(true);
			}, () -> {
				return instance.callWebServices(false);
			}, () -> {
				return instance.callWebServices(false);
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String callWebServices(boolean throwError) throws Exception {
		
		try (var scope = StructuredTaskScope.open(Joiner.<String>allSuccessfulOrThrow())) {
			scope.fork(() -> CommonUtils.task("A", 14500, throwError));
			scope.fork(() -> CommonUtils.task("B", 15500));
			scope.fork(() -> CommonUtils.task("C", 15000));
			return scope.join().stream().collect(Collectors.joining(", ", "Combined results:\n {\n{ ", " }"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
