package step1;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

public class SerialSolution {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to start.");

		var instance = new SerialSolution();
		String result = instance.callWebServices();

		System.out.println(result);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Exception {
		try {
			var tasks = List.<Callable<String>>of(
			() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(500);
				result = "a";
				System.out.println(result);
				return result;
			},
			() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(500);
				result = "b";
				System.out.println(result);
				return result;
			},
			() -> {
				String result;
				TimeUnit.MILLISECONDS.sleep(500);
				result = "c";
				System.out.println(result);
				return result;
			});
			
			return tasks.stream().map(t -> {
				try {
					return t.call();
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}).collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}