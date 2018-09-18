package cn.hutool.core.swing;

import org.junit.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.swing.RobotUtil;

public class RobotUtilTest {
	
	@Test
	public void captureScreenTest() {
		RobotUtil.captureScreen(FileUtil.file("e:/screen.jpg"));
	}
}
