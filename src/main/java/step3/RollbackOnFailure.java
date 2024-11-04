package step3;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class RollbackOnFailure {

	public static void main(String... args) throws Throwable {

		var instance = new RollbackOnFailure();
		String results = instance.runTasks();
		System.out.println(results);

	}

	private String runTasks() throws Throwable {
		Joiner<String, Stream<Subtask<String>>> joiner = Joiner.allSuccessfulOrThrow();
		try (var scope = StructuredTaskScope.open(joiner)) {

			Subtask<String> task1 = scope.fork(() -> CommonUtils.task("A", 1000, true));
			Subtask<String> task2 = scope.fork(() -> CommonUtils.task("B", 500));
			Subtask<String> task3 = scope.fork(() -> CommonUtils.task("C", 500));
			try {
				return scope.join().map(f -> f.get()).collect(Collectors.joining(", ", "{ ", " }"));
			} catch (Exception ee) {
				handleRollback(List.of(task1, task2, task3));
				throw ee;
			}
		} catch (Exception e) {

			throw e;
		}
	}

	private void handleRollback(List<Subtask<String>> tasksToRollback) {
		tasksToRollback.stream().filter(s -> s.state() == State.SUCCESS).close();//Implement behavior to rollback tasks that have been completed
	}

}
