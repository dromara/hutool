package org.dromara.hutool.core.lang.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleEventPublisherTest {

	@Test
	void registerTest() {
		final SimpleEventPublisher publisher = SimpleEventPublisher.of();
		publisher.register(event -> Assertions.assertEquals("test", ((TestEvent)event).getName()));

		publisher.publish(new TestEvent("test"));
	}

	@Test
	void sourceEventTest() {
		final SimpleEventPublisher publisher = SimpleEventPublisher.of();
		publisher.register(event -> Assertions.assertEquals("test", ((SourceEvent)event).getSource()));

		publisher.publish(new SourceEvent("test"));
	}

	private static class TestEvent implements Event {
		private final String name;

		public TestEvent(final String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
