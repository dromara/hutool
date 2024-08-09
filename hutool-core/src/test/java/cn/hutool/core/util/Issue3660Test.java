package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue3660Test {
	@Test
	public void splitTest() {
		List<String> split = StrUtil.split("", ',');
		assertEquals(1, split.size());

		split = StrUtil.splitTrim("", ',');
		assertEquals(0, split.size());
	}
}
