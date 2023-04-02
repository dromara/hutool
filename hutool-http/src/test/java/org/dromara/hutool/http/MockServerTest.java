package org.dromara.hutool.http;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;

public class MockServerTest {
	public static void main(final String[] args) throws IOException {
		//noinspection resource
		final MockWebServer server = new MockWebServer();
		final MockResponse mockResponse = new MockResponse().setBody("hello, world!");
		server.enqueue(mockResponse);
		server.start(8080);
	}
}
