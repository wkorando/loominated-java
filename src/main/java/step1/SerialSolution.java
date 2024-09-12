package step1;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.CommonUtils;

public class SerialSolution {

	public static void main(String... args) throws Exception {
		CommonUtils.waitForUser("Press enter to start.");

		var instance = new SerialSolution();
		String result = instance.runTasks();

		System.out.println(result);
		CommonUtils.waitForUser("Press enter to exit.");
	}

	private String runTasks() throws Exception {

		var tasks = Stream.of(
				CommonUtils.task("A", 500), 
				CommonUtils.task("B", 500), 
				CommonUtils.task("C", 500));

		return tasks.collect(Collectors.joining(", ", "{ ", " }"));

	}

}