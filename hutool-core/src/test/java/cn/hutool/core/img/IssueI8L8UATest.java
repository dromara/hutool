package cn.hutool.core.img;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI8L8UATest {
	@Test
	@Disabled
	public void convertTest() {
		ImgUtil.convert(
			FileUtil.file("d:/test/1.png"),
			FileUtil.file("d:/test/1.jpg"));
	}
}
