package cn.hutool.core.collection;

import org.junit.Assert;
import org.junit.Test;

public class MemorySafeLinkedBlockingQueueTest {

	@Test
	public void offerTest(){
		final MemorySafeLinkedBlockingQueue<String> queue = new MemorySafeLinkedBlockingQueue<>(Integer.MAX_VALUE);
		Assert.assertFalse(queue.offer("123"));
	}
}
