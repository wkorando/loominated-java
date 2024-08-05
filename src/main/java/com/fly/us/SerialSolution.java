package com.fly.us;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SerialSolution {

	public static void main(String... args) throws Exception {
		WebServiceHelper.waitForUser("Press enter to start.");

		SerialSolution instance = new SerialSolution();
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

			return responses.stream().collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}
