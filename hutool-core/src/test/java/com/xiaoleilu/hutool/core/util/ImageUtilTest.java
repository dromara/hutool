package com.xiaoleilu.hutool.core.util;

import org.junit.Test;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.util.ImageUtil;

public class ImageUtilTest {
	
	@Test
	public void cutTest() {
		ImageUtil.cut(FileUtil.file("e:/face.jpg"), FileUtil.file("e:/face_result.jpg"), 200, 200, 100, 100);
	}
}
