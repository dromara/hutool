package cn.hutool.core.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueI9UK5VTest {
	@Test
	public void splitTest() {
		String str = "";
		List<String> split = StrUtil.split(str, ',');
		assertEquals(1, split.size());

		split = StrUtil.splitTrim(str, ',');
		assertEquals(0, split.size());
	}
}
