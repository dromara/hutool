package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3649Test {
	@Test
	void toEmptyBeanTest() {
		final Object bean = JSONUtil.toBean("{}", JSONConfig.of().setIgnoreError(false), EmptyBean.class);
		Assertions.assertEquals(new EmptyBean(), bean);
	}

	@Data
	public static class EmptyBean {}
}
