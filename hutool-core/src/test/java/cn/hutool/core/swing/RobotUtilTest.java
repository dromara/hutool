package cn.hutool.core.swing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cn.hutool.core.io.FileUtil;

public class RobotUtilTest {

	@Test
	@Disabled
	public void captureScreenTest() {
		RobotUtil.captureScreen(FileUtil.file("e:/screen.jpg"));
	}
}
