package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * https://github.com/dromara/hutool/issues/1399<br>
 * 异常SQLException实现了Iterable导致被识别为列表，可能造成死循环，此处按照字符串处理
 */
public class Issue1399Test {
	@Test
	void sqlExceptionTest() {
		final JSONObject set = JSONUtil.ofObj().set("error", new SQLException("test"));
		Assertions.assertEquals("{\"error\":\"java.sql.SQLException: test\"}", set.toString());
	}
}
