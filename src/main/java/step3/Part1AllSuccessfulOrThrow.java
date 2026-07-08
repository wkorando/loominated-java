package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.stream.Collectors;

import common.CommonUtils;

public class Part1AllSuccessfulOrThrow {

	public static void main(String... args) throws Throwable {

//		CommonUtils.waitForUser("Press enter to continue.");

		var instance = new Part1AllSuccessfulOrThrow();
		String results = instance.runTasks();
		System.out.println(results);
		
	}

	private String runTasks() throws Throwable {

		try (var scope = StructuredTaskScope.open(Joiner.<String>allSuccessfulOrThrow())) {

			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1500));
			scope.fork(() -> CommonUtils.task("C", 1000));
			
			return scope.join().stream().collect(Collectors.joining(", ", "Combined results:\n {\n{ ", " }"));

		} catch (InterruptedException e) {
			throw e;
		}
	}

}
