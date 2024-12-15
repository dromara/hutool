package cn.hutool.core.date;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

public class IssueIB9NPUTest {
	@Test
	void parseTest() {
		DateUtil.parse("202409032400", new SimpleDateFormat("yyyyMMddHHmm"));
	}
}
