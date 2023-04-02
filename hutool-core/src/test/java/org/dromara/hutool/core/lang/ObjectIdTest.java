package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.lang.id.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

/**
 * ObjectId单元测试
 *
 * @author looly
 *
 */
public class ObjectIdTest {

	@Test
	public void distinctTest() {
		//生成10000个id测试是否重复
		final HashSet<String> set = new HashSet<>();
		for(int i = 0; i < 10000; i++) {
			set.add(ObjectId.next());
		}

		Assertions.assertEquals(10000, set.size());
	}

	@Test
	@Disabled
	public void nextTest() {
		Console.log(ObjectId.next());
	}
}
