package cn.hutool.core.img;

import cn.hutool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI8L8UATest {
	@Test
	@Ignore
	public void convertTest() {
		ImgUtil.convert(
			FileUtil.file("d:/test/1.png"),
			FileUtil.file("d:/test/1.jpg"));
	}
}
