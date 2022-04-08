package cn.hutool.core.io;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.file.FileCopier;

import java.io.File;

/**
 * 文件拷贝单元测试
 *
 * @author Looly
 */
public class FileCopierTest {

	@Test
	@Ignore
	public void dirCopyTest() {
		FileCopier copier = FileCopier.create("D:\\Java", "e:/eclipse/eclipse2.zip");
		copier.copy();
	}

	@Test
	@Ignore
	public void dirCopyTest2() {
		//测试带.的文件夹复制
		FileCopier copier = FileCopier.create("D:\\workspace\\java\\.metadata", "D:\\workspace\\java\\.metadata\\temp");
		copier.copy();

		FileUtil.copy("D:\\workspace\\java\\looly\\hutool\\.git", "D:\\workspace\\java\\temp", true);
	}

	@Test(expected = IORuntimeException.class)
	public void dirCopySubTest() {
		//测试父目录复制到子目录报错
		FileCopier copier = FileCopier.create("D:\\workspace\\java\\.metadata", "D:\\workspace\\java\\.metadata\\temp");
		copier.copy();
	}

	@Test
	@Ignore
	public void copyFileToDirTest() {
		FileCopier copier = FileCopier.create("d:/GReen_Soft/XshellXftpPortable.zip", "c:/hp/");
		copier.copy();
	}

	@Test
	@Ignore
	public void copyFileByRelativePath(){
		// https://github.com/dromara/hutool/pull/2188
		//  当复制的目标文件位置是相对路径的时候可以通过
		FileCopier copier = FileCopier.create(new File("pom.xml"),new File("aaa.txt"));
		copier.copy();
		final boolean delete = new File("aaa.txt").delete();
		Assert.assertTrue(delete);
	}
}
