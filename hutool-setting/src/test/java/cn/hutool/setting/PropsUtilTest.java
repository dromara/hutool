package cn.hutool.setting;

import cn.hutool.setting.dialect.PropsUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

public class PropsUtilTest {

	@Test
	public void getTest() {
		final String driver = PropsUtil.get("test").getStr("driver");
		Assert.assertEquals("com.mysql.jdbc.Driver", driver);
	}

	@Test
	public void getFirstFoundTest() {
		final String driver = Objects.requireNonNull(PropsUtil.getFirstFound("test2", "test")).getStr("driver");
		Assert.assertEquals("com.mysql.jdbc.Driver", driver);
	}
}
