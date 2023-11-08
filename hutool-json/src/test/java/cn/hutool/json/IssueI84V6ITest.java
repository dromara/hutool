package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

public class IssueI84V6ITest {
	@Test
	public void formatTest() {
		final String a1 = "{'x':'\\n','y':','}";
		final String formatJsonStr = JSONUtil.formatJsonStr(a1);
//		Console.log(formatJsonStr);
		Assert.assertEquals(
			"{\n" +
				"    'x': '\\n',\n" +
				"    'y': ','\n" +
				"}", formatJsonStr);
	}
}
