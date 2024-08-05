package com.fly.us;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructuredConcurrencyShutdownOnSuccess {

	public static void main(String... args) throws Throwable {

		WebServiceHelper.waitForUser("Press enter to continue.");

		StructuredConcurrencyShutdownOnSuccess instance = new StructuredConcurrencyShutdownOnSuccess();
		String result = instance.callWebServices();
		System.out.println(result);

		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		try (var scope = StructuredTaskScope.<String, String>open(Joiner.anySuccessfulResultOrThrow())) {
			scope.fork(() -> WebServiceHelper.callWebService("success-jet"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("http-ok-airways"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("two-hundred-airlines"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("delayed-travel"));// 5000ms
			return scope.join();// Map this to the return type of subtask
		} catch (Exception e) {
			throw e;
		}
	}

}
