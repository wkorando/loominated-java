package com.fly.us;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpHandlers;
import com.sun.net.httpserver.HttpServer;

public class MockServer {
	private static final InetSocketAddress LOOPBACK_ADDR = new InetSocketAddress(InetAddress.getLoopbackAddress(),
			8080);
	private static final String RESPONSE_BODY = """
			{
				"airline" : "%s",
				"cost" : "%s"
			}
			""";

	public static void main(String[] args) throws Exception {

		Headers headers = new Headers();
		HttpServer s = HttpServer.create(LOOPBACK_ADDR, 0);
		Properties props = new Properties();
		props.load(new FileInputStream("mock-server.properties"));

		Set<String> propertyNames = props.stringPropertyNames();

		for (String propertyName : propertyNames) {
			String value = props.getProperty(propertyName);
			String[] endpointValues = value.split(",");
			s.createContext("/" + endpointValues[0],
					new TimedHandler(Long.valueOf(endpointValues[3]),
							HttpHandlers.of(Integer.valueOf(endpointValues[1]), headers,
									RESPONSE_BODY.formatted(endpointValues[0], endpointValues[2]))));
		}

//		s.createContext("/success-lines",
//				new TimedHandler(500L, HttpHandlers.of(200, headers, RESPONSE_BODY.formatted("success-lines", "200"))));
//		s.createContext("/ok-airways",
//				new TimedHandler(500L, HttpHandlers.of(200, headers, RESPONSE_BODY.formatted("ok-lines", "100"))));
//		s.createContext("/two-hundred-air", new TimedHandler(500L,
//				HttpHandlers.of(200, headers, RESPONSE_BODY.formatted("two-hundred-lines", "300"))));
//		s.createContext("/not-found-lines", HttpHandlers.of(404, headers, ""));
//		s.createContext("/slow-airlines", new TimedHandler(2000L,
//				HttpHandlers.of(200, headers, RESPONSE_BODY.formatted("slow-response", "500"))));
//		s.createContext("/malformed-response", new TimedHandler(500L, HttpHandlers.of(200, headers, "malformed-body")));
//		s.createContext("/server-error-lines", new TimedHandler(500L, HttpHandlers.of(500, headers, "")));

		s.start();
	}
}

class TimedHandler implements HttpHandler {
	HttpHandler realHandler;
	long sleep;

	public TimedHandler(long sleep, HttpHandler realHandler) {
		super();
		this.sleep = sleep;
		this.realHandler = realHandler;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			Thread.sleep(sleep);
			realHandler.handle(exchange);
		} catch (Exception e) {

		}
	}
}