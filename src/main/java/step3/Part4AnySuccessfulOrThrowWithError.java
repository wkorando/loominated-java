package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;

import common.CommonUtils;

public class Part4AnySuccessfulOrThrowWithError {

	public static void main(String... args) throws Throwable {

		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new Part4AnySuccessfulOrThrowWithError();
		String results = instance.runTasks();
		System.out.println(results);
	}

	private String runTasks() throws Throwable {

		try (var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulOrThrow())) {

			scope.fork(() -> CommonUtils.task("D", 100, true));
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1500));
			scope.fork(() -> CommonUtils.task("C", 1000));
			
			return scope.join();

		} catch (Throwable e) {
			e.printStackTrace(System.err);
			throw e;
		}
	}

}
