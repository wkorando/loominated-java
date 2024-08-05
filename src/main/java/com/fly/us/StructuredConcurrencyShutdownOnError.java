package com.fly.us;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructuredConcurrencyShutdownOnError {

	public static void main(String... args) throws Throwable {

		WebServiceHelper.waitForUser("Press enter to continue.");

		StructuredConcurrencyShutdownOnError instance = new StructuredConcurrencyShutdownOnError();
		String results = instance.callWebServices();
		System.out.println(results);
		WebServiceHelper.waitForUser("Press enter to exit.");
	}

	private String callWebServices() throws Throwable {
		try (var scope = StructuredTaskScope.<String, Stream<Subtask<String>>>open(Joiner.allSuccessfulOrThrow())) {
			scope.fork(() -> WebServiceHelper.callWebService("success-jet"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("http-ok-airways"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("two-hundred-airlines"));// 500ms
			scope.fork(() -> WebServiceHelper.callWebService("delayed-travel"));// 5000ms
			scope.fork(() -> WebServiceHelper.callWebService("not-found-air"));// 100ms
			return scope.join().map(f -> f.get()).collect(Collectors.joining(", ", "{ ", " }"));
		} catch (Exception e) {
			throw e;
		}
	}

}
