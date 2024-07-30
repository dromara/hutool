package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Issue3660Test {
	@Test
	public void splitTest() {
		List<String> split = StrUtil.split("", ',');
		Assert.assertEquals(1, split.size());

		split = StrUtil.splitTrim("", ',');
		Assert.assertEquals(0, split.size());
	}
}
