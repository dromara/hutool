package cn.hutool.json;

import lombok.Data;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Issue3649Test {
	@Test
	public void toEmptyBeanTest() {
		final Object bean = JSONUtil.toBean("{}", JSONConfig.create().setIgnoreError(true), EmptyBean.class);
		assertEquals(new EmptyBean(), bean);
	}

	@Data
	public static class EmptyBean {}
}
