package org.dromara.hutool.core.text.split;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueI9UK5VTest {
	@Test
	public void splitTest() {
		final String str = "";
		List<String> split = SplitUtil.split(str, ",");
		assertEquals(1, split.size());

		split = SplitUtil.splitTrim(str, ",");
		assertEquals(0, split.size());
	}
}
