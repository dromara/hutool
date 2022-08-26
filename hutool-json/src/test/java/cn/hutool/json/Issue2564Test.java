package cn.hutool.json;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

public class Issue2564Test {

	/**
	 * 实力类 没有get set方法，不能被认为是一个bean
	 */
	@Test()
	public void emptyToBeanTest(){
		final String x = "{}";
		final A a = JSONUtil.toBean(x, JSONConfig.of().setIgnoreError(true), A.class);
		Assert.assertNull(a);
	}

	@Getter
	@Setter
	public static class A{
	}
}
