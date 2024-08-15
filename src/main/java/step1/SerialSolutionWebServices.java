package step1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fly.us.WebServiceHelper;

public class SerialSolutionWebServices {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to start.");

		SerialSolutionWebServices instance = new SerialSolutionWebServices();
		String result = instance.callWebServices();

		System.out.println(result);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Exception {
		try {
			List<String> responses = new ArrayList<>();
			
			responses.add(WebServiceHelper.callWebService("success-jet"));
			responses.add(WebServiceHelper.callWebService("http-ok-airways"));
			responses.add(WebServiceHelper.callWebService("two-hundred-airlines"));
			responses.add(WebServiceHelper.callWebService("not-found-air"));

			return responses.stream().collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}
