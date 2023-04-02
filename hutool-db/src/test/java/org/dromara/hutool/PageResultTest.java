package org.dromara.hutool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageResultTest {

	@Test
	public void isLastTest(){
		// 每页2条，共10条，总共5页，第一页是0，最后一页应该是4
		final PageResult<String> result = new PageResult<>(4, 2, 10);
		Assertions.assertTrue(result.isLast());
	}
}
