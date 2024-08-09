package cn.hutool.core.io;

import cn.hutool.core.io.file.FileCopier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 文件拷贝单元测试
 *
 * @author Looly
 */
public class FileCopierTest {

	@Test
	@Disabled
	public void dirCopyTest() {
		FileCopier copier = FileCopier.create("D:\\Java", "e:/eclipse/eclipse2.zip");
		copier.copy();
	}

	@Test
	@Disabled
	public void dirCopyTest2() {
		//测试带.的文件夹复制
		FileCopier copier = FileCopier.create("D:\\workspace\\java\\.metadata", "D:\\workspace\\java\\.metadata\\temp");
		copier.copy();

		FileUtil.copy("D:\\workspace\\java\\looly\\hutool\\.git", "D:\\workspace\\java\\temp", true);
	}

	@Test
	public void dirCopySubTest() {
		assertThrows(IORuntimeException.class, () -> {
			//测试父目录复制到子目录报错
			FileCopier copier = FileCopier.create("D:\\workspace\\java\\.metadata", "D:\\workspace\\java\\.metadata\\temp");
			copier.copy();
		});
	}

	@Test
	@Disabled
	public void copyFileToDirTest() {
		FileCopier copier = FileCopier.create("d:/GReen_Soft/XshellXftpPortable.zip", "c:/hp/");
		copier.copy();
	}

	@Test
	@Disabled
	public void copyFileByRelativePath(){
		// https://github.com/dromara/hutool/pull/2188
		//  当复制的目标文件位置是相对路径的时候可以通过
		FileCopier copier = FileCopier.create(new File("pom.xml"),new File("aaa.txt"));
		copier.copy();
		final boolean delete = new File("aaa.txt").delete();
		assertTrue(delete);
	}
}
