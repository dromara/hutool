package cn.hutool.setting.yaml;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.map.Dict;
import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class YamlUtilTest {

	@Test
	public void loadByPathTest() {
		final Dict result = YamlUtil.loadByPath("test.yaml");

		Assertions.assertEquals("John", result.getStr("firstName"));

		final List<Integer> numbers = result.getByPath("contactDetails.number");
		Assertions.assertEquals(123456789, (int) numbers.get(0));
		Assertions.assertEquals(456786868, (int) numbers.get(1));
	}

	@Test
	@Disabled
	public void dumpTest() {
		final Dict dict = Dict.of()
				.set("name", "hutool")
				.set("count", 1000);

		YamlUtil.dump(
				dict
				, FileUtil.getWriter("d:/test/dump.yaml", CharsetUtil.UTF_8, false));
	}
}
