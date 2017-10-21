package com.xiaoleilu.hutool.io;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.io.file.FileCopier;

/**
 * 文件拷贝单元测试
 * @author Looly
 *
 */
public class FileCopierTest {
	
	@Test
	@Ignore
	public void dirCopyTest() {
		FileCopier copier = FileCopier.create("D:\\Java", "e:/eclipse/eclipse2.zip");
		copier.copy();
	}
}
