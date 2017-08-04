package com.xiaoleilu.hutool.core.util;

import org.junit.Test;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.util.ImageUtil;

public class ImageUtilTest {
	
	@Test
	public void cutTest() {
		ImageUtil.cut(FileUtil.file("d:/police.png"), FileUtil.file("d:/hero.jpg"), 12, 24, 100, 150);
	}
}
