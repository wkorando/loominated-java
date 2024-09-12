package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

	/**
	 * Representation of a task within a unit of work that can be executed
	 * concurrent.
	 * 
	 * If you want to a task to throw an exception
	 * {@link CommonUtils#task(String, long, boolean)}
	 * 
	 * @param value         - A value to be returned wrapped in a simple JSON
	 *                      message.
	 * @param executionTime - How long, in milliseconds, this task should take to
	 *                      execute.
	 * @return simple JSON message formatted like: <br/>
	 *         {<br/>
	 *         &nbsp;&nbsp;&nbsp;"value" : ${value}<br/>
	 *         }<br/>
	 * 
	 */
	public static String task(String value, long executionTime) {
		return task(value, executionTime, false);
	}

	/**
	 * Override of {@link CommonUtils#task(String, long)} that allows for the
	 * declarative throwing of an exception.
	 * 
	 * @see CommonUtils#task(String, long)
	 * 
	 * @param value          - A value to be returned wrapped in a simple JSON
	 *                       message.
	 * @param executionTime  - How long, in milliseconds, this task should take to
	 *                       execute.
	 * @param throwException - Should this task throw an exception? Exception will
	 *                       be thrown after finishing executionTime.
	 * @return
	 */
	public static String task(String value, long executionTime, boolean throwException) {
		executionTime(executionTime);
		if (throwException) {
			System.out.println("Throwing exception!");
			throw new RuntimeException(String.format("""
					Task failed!
					value: %s
					executionTime: %d
					throwException: %b
					""", value, executionTime, throwException));
		}
		String result = String.format("""
				{
					"value" : "%s"
				}
				""", value);
		System.out.println("Result of task: " + result);
		return result;
	}

	/**
	 * Sleeps the execution thread for the passed in value.
	 * 
	 * @param executionTime - how long the thread should sleep in milliseconds.
	 */
	private static void executionTime(long executionTime) {
		try {
			TimeUnit.MILLISECONDS.sleep(executionTime);
		} catch (InterruptedException e) {
			// Just eating this exception, it shouldn't happen and isn't relevant to the
			// demo.
			e.printStackTrace();
		}
	}

	public static void waitForUser(String message) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(message);
		reader.readLine();
	}
}
