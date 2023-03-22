package cn.hutool.setting.yaml;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class YamlUtilTest {

	@Test
	public void loadByPathTest() {
		final Dict result = YamlUtil.loadByPath("test.yaml");

		Assert.assertEquals("John", result.getStr("firstName"));

		final List<Integer> numbers = result.getByPath("contactDetails.number");
		Assert.assertEquals(123456789, (int) numbers.get(0));
		Assert.assertEquals(456786868, (int) numbers.get(1));
	}

	@Test
	@Ignore
	public void dumpTest() {
		final Dict dict = Dict.create()
				.set("name", "hutool")
				.set("count", 1000);

		YamlUtil.dump(
				dict
				, FileUtil.getWriter("d:/test/dump.yaml", CharsetUtil.CHARSET_UTF_8, false));
	}
}
