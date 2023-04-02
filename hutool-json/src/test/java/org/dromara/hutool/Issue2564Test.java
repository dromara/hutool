package org.dromara.hutool;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2564Test {

	/**
	 * 实力类 没有get set方法，不能被认为是一个bean
	 */
	@Test()
	public void emptyToBeanTest(){
		final String x = "{}";
		final A a = JSONUtil.toBean(x, JSONConfig.of().setIgnoreError(true), A.class);
		Assertions.assertNull(a);
	}

	@Getter
	@Setter
	public static class A{
	}
}
