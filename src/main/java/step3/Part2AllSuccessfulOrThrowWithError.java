package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class Part2AllSuccessfulOrThrowWithError {
	public static void main(String... args) throws Throwable {

//		CommonUtils.waitForUser("Press enter to continue.");

		Part2AllSuccessfulOrThrowWithError instance = new Part2AllSuccessfulOrThrowWithError();
		String results = instance.runTasks();
		System.out.println(results);
//		CommonUtils.waitForUser("Press enter to exit.");
	}

	private String runTasks() throws Throwable {
		try (var scope = StructuredTaskScope.open(Joiner.<String>allSuccessfulOrThrow())) {
			 
			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1000));
			scope.fork(() -> CommonUtils.task("C", 1500));
			scope.fork(() -> CommonUtils.task("D", 100,true));
			return scope.join().stream()
					.collect(Collectors.joining(", ", "Combined results:\n {\n{ ", " }"));
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
