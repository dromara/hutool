package cn.hutool.core.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAOGDRTest {
	@Test
	void isChineseNameTest() {
		Assertions.assertFalse(Validator.isChineseName("张三。"));
	}
}
