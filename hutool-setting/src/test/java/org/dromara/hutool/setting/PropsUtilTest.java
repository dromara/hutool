package org.dromara.hutool.setting;

import org.dromara.hutool.setting.dialect.PropsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class PropsUtilTest {

	@Test
	public void getTest() {
		final String driver = PropsUtil.get("test").getStr("driver");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getFirstFoundTest() {
		final String driver = Objects.requireNonNull(PropsUtil.getFirstFound("test2", "test")).getStr("driver");
		Assertions.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
