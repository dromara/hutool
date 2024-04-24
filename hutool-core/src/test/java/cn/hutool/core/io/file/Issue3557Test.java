package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.StandardCopyOption;

public class Issue3557Test {

	@Test
	@Ignore
	public void copyFileTest() {
		// 如果只是文件不存在，则不会报错
		// 如果文件所在目录不存在，则会报错
		FileUtil.copyFile(FileUtil.getInputStream("d:/test/aaa.xlsx"), FileUtil.file("d:/test2/aaa_copy.xlsx"), StandardCopyOption.REPLACE_EXISTING);
	}
}
