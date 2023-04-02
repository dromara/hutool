package org.dromara.hutool.lang;

import org.dromara.hutool.collection.ConcurrentHashSet;
import org.dromara.hutool.lang.id.UUID;
import org.dromara.hutool.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UUIDTest {

	/**
	 * 测试UUID是否存在重复问题
	 */
	@Test
	public void fastUUIDTest(){
		final Set<String> set = new ConcurrentHashSet<>(100);
		ThreadUtil.concurrencyTest(100, ()-> set.add(UUID.fastUUID().toString()));
		Assertions.assertEquals(100, set.size());
	}


}
