package step3;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class AwaitAll {

	public static void main(String... args) throws Throwable {
		var instance = new AwaitAll();
		instance.runTasks();
	}

	private void runTasks() throws Throwable {
		Joiner<String, Void> joiner = Joiner.awaitAll();

		try (var scope = StructuredTaskScope.open(joiner)) {

			scope.fork(() -> CommonUtils.task("A", 500));
			scope.fork(() -> CommonUtils.task("B", 1500));
			scope.fork(() -> CommonUtils.task("C", 1000));
			
			scope.join();

		} catch (Exception e) {
			throw e;
		}
	}

}
