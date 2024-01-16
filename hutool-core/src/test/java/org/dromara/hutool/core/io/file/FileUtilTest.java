/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.SystemUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * {@link FileUtil} 单元测试类
 *
 * @author Looly
 */
public class FileUtilTest {

	@Test
	void fileTest1() {
		final File file = FileUtil.file("d:/aaa", "bbb");
		Assertions.assertNotNull(file);
	}

	@Test
	public void fileTest2() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			// 构建目录中出现非子目录抛出异常
			FileUtil.file("d:/aaa/bbb", "../ccc");
		});
	}

	@Test
	public void getAbsolutePathTest() {
		final String absolutePath = FileUtil.getAbsolutePath("LICENSE-junit.txt");
		Assertions.assertNotNull(absolutePath);
		final String absolutePath2 = FileUtil.getAbsolutePath(absolutePath);
		Assertions.assertNotNull(absolutePath2);
		Assertions.assertEquals(absolutePath, absolutePath2);

		String path = FileUtil.getAbsolutePath("中文.xml");
		Assertions.assertTrue(path.contains("中文.xml"));

		path = FileUtil.getAbsolutePath("d:");
		Assertions.assertEquals("d:", path);
	}

	@Test
	@Disabled
	public void touchTest() {
		FileUtil.touch("d:\\tea\\a.jpg");
	}

	@Test
	@Disabled
	public void renameTest() {
		FileUtil.rename(FileUtil.file("d:/test/3.jpg"), "2.jpg", false);
	}

	@Test
	@Disabled
	public void renameTest2() {
		FileUtil.move(FileUtil.file("d:/test/a"), FileUtil.file("d:/test/b"), false);
	}

	@Test
	@Disabled
	public void renameToSubTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			// 移动到子目录，报错
			FileUtil.move(FileUtil.file("d:/test/a"), FileUtil.file("d:/test/a/c"), false);
		});
	}

	@Test
	@Disabled
	public void renameSameTest() {
		// 目标和源相同，不处理
		FileUtil.move(FileUtil.file("d:/test/a"), FileUtil.file("d:/test/a"), false);
	}

	@Test
	public void copyTest() {
		final File srcFile = FileUtil.file("hutool.jpg");
		final File destFile = FileUtil.file("hutool.copy.jpg");

		FileUtil.copy(srcFile, destFile, true);

		Assertions.assertTrue(destFile.exists());
		Assertions.assertEquals(srcFile.length(), destFile.length());
	}

	@Test
	@Disabled
	public void copySameTest() {
		final File srcFile = FileUtil.file("d:/test/a");
		final File destFile = FileUtil.file("d:/test/");

		// 拷贝到当前目录，不做处理
		FileUtil.copy(srcFile, destFile, true);
	}

	@Test
	@Disabled
	public void copyDirTest() {
		final File srcFile = FileUtil.file("D:\\test");
		final File destFile = FileUtil.file("E:\\");

		FileUtil.copy(srcFile, destFile, true);
	}

	@Test
	@Disabled
	public void moveDirTest() {
		final File srcFile = FileUtil.file("E:\\test2");
		final File destFile = FileUtil.file("D:\\");

		FileUtil.move(srcFile, destFile, true);
	}

	@Test
	public void equalsTest() {
		// 源文件和目标文件都不存在
		final File srcFile = FileUtil.file("d:/hutool.jpg");
		final File destFile = FileUtil.file("d:/hutool.jpg");

		final boolean equals = FileUtil.equals(srcFile, destFile);
		Assertions.assertTrue(equals);

		// 源文件存在，目标文件不存在
		final File srcFile1 = FileUtil.file("hutool.jpg");
		final File destFile1 = FileUtil.file("d:/hutool.jpg");

		final boolean notEquals = FileUtil.equals(srcFile1, destFile1);
		Assertions.assertFalse(notEquals);
	}

	@Test
	@Disabled
	public void convertLineSeparatorTest() {
		FileUtil.convertLineSeparator(FileUtil.file("d:/aaa.txt"), CharsetUtil.UTF_8, LineSeparator.WINDOWS);
	}

	@Test
	public void normalizeHomePathTest() {
		final String home = SystemUtil.getUserHomePath().replace('\\', '/');
		Assertions.assertEquals(home + "/bar/", FileUtil.normalize("~/foo/../bar/"));
	}

	@Test
	public void normalizeHomePathTest2() {
		final String home = SystemUtil.getUserHomePath().replace('\\', '/');
		// 多个~应该只替换开头的
		Assertions.assertEquals(home + "/~bar/", FileUtil.normalize("~/foo/../~bar/"));
	}

	@Test
	public void normalizeClassPathTest() {
		Assertions.assertEquals("", FileUtil.normalize("classpath:"));
	}

	@Test
	public void normalizeClassPathTest2() {
		Assertions.assertEquals("../a/b.csv", FileUtil.normalize("../a/b.csv"));
		Assertions.assertEquals("../../../a/b.csv", FileUtil.normalize("../../../a/b.csv"));
	}

	@Test
	public void doubleNormalizeTest() {
		final String normalize = FileUtil.normalize("/aa/b:/c");
		final String normalize2 = FileUtil.normalize(normalize);
		Assertions.assertEquals("/aa/b:/c", normalize);
		Assertions.assertEquals(normalize, normalize2);
	}

	@Test
	public void subPathTest() {
		final Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

		Path subPath = FileUtil.subPath(path, 5, 4);
		Assertions.assertEquals("eee", subPath.toString());
		subPath = FileUtil.subPath(path, 0, 1);
		Assertions.assertEquals("aaa", subPath.toString());
		subPath = FileUtil.subPath(path, 1, 0);
		Assertions.assertEquals("aaa", subPath.toString());

		// 负数
		subPath = FileUtil.subPath(path, -1, 0);
		Assertions.assertEquals("aaa/bbb/ccc/ddd/eee", subPath.toString().replace('\\', '/'));
		subPath = FileUtil.subPath(path, -1, Integer.MAX_VALUE);
		Assertions.assertEquals("fff", subPath.toString());
		subPath = FileUtil.subPath(path, -1, path.getNameCount());
		Assertions.assertEquals("fff", subPath.toString());
		subPath = FileUtil.subPath(path, -2, -3);
		Assertions.assertEquals("ddd", subPath.toString());
	}

	@Test
	public void subPathTest2() {
		String subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb/ccc/");
		Assertions.assertEquals("ccc/", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc/");
		Assertions.assertEquals("ccc/", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc/test.txt");
		Assertions.assertEquals("ccc/test.txt", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb/ccc");
		Assertions.assertEquals("ccc", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb/ccc");
		Assertions.assertEquals("ccc", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb", "d:/aaa/bbb");
		Assertions.assertEquals("", subPath);

		subPath = FileUtil.subPath("d:/aaa/bbb/", "d:/aaa/bbb");
		Assertions.assertEquals("", subPath);
	}

	@Test
	public void getPathEle() {
		final Path path = Paths.get("/aaa/bbb/ccc/ddd/eee/fff");

		Path ele = FileUtil.getPathEle(path, -1);
		Assertions.assertEquals("fff", ele.toString());
		ele = FileUtil.getPathEle(path, 0);
		Assertions.assertEquals("aaa", ele.toString());
		ele = FileUtil.getPathEle(path, -5);
		Assertions.assertEquals("bbb", ele.toString());
		ele = FileUtil.getPathEle(path, -6);
		Assertions.assertEquals("aaa", ele.toString());
	}

	@Test
	@EnabledForJreRange(max = JRE.JAVA_8)
	public void listFileNamesTest() {
		// JDK9+中，由于模块化问题，获取的classoath路径非项目下，而是junit下的。
		List<String> names = FileUtil.listFileNames("classpath:");
		Assertions.assertTrue(names.contains("hutool.jpg"));

		names = FileUtil.listFileNames("");
		Assertions.assertTrue(names.contains("hutool.jpg"));

		names = FileUtil.listFileNames(".");
		Assertions.assertTrue(names.contains("hutool.jpg"));
	}

	@Test
	@Disabled
	public void listFileNamesInJarTest() {
		final List<String> names = FileUtil.listFileNames("d:/test/hutool-core-5.1.0.jar!/cn/hutool/core/util ");
		for (final String name : names) {
			Console.log(name);
		}
	}

	@Test
	@Disabled
	public void listFileNamesTest2() {
		final List<String> names = FileUtil.listFileNames("D:\\m2_repo\\commons-cli\\commons-cli\\1.0\\commons-cli-1.0.jar!org/mina/commons/cli/");
		for (final String string : names) {
			Console.log(string);
		}
	}

	@Test
	@Disabled
	public void loopFilesTest() {
		final List<File> files = FileUtil.loopFiles("d:/");
		for (final File file : files) {
			Console.log(file.getPath());
		}
	}

	@Test
	@Disabled
	public void loopFileTest() {
		final List<File> files = FileUtil.loopFiles("D:\\m2_repo\\cglib\\cglib\\3.3.0\\cglib-3.3.0.jar");
		Console.log(files);
	}

	@Test
	@Disabled
	public void loopFilesTest2() {
		FileUtil.loopFiles("").forEach(Console::log);
	}

	@Test
	@Disabled
	public void loopFilesWithDepthTest() {
		final List<File> files = FileUtil.loopFiles(FileUtil.file("d:/m2_repo"), 2, null);
		for (final File file : files) {
			Console.log(file.getPath());
		}
	}

	@Test
	public void getParentTest() {
		// 只在Windows下测试
		if (FileUtil.isWindows()) {
			File parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 0);
			Assertions.assertEquals(FileUtil.file("d:\\aaa\\bbb\\cc\\ddd"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 1);
			Assertions.assertEquals(FileUtil.file("d:\\aaa\\bbb\\cc"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 2);
			Assertions.assertEquals(FileUtil.file("d:\\aaa\\bbb"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 4);
			Assertions.assertEquals(FileUtil.file("d:\\"), parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 5);
			Assertions.assertNull(parent);

			parent = FileUtil.getParent(FileUtil.file("d:/aaa/bbb/cc/ddd"), 10);
			Assertions.assertNull(parent);
		}
	}

	@Test
	public void lastIndexOfSeparatorTest() {
		final String dir = "d:\\aaa\\bbb\\cc\\ddd";
		final int index = FileUtil.lastIndexOfSeparator(dir);
		Assertions.assertEquals(13, index);

		final String file = "ddd.jpg";
		final int index2 = FileUtil.lastIndexOfSeparator(file);
		Assertions.assertEquals(-1, index2);
	}

	@Test
	public void getNameTest() {
		String path = "d:\\aaa\\bbb\\cc\\ddd\\";
		String name = FileNameUtil.getName(path);
		Assertions.assertEquals("ddd", name);

		path = "d:\\aaa\\bbb\\cc\\ddd.jpg";
		name = FileNameUtil.getName(path);
		Assertions.assertEquals("ddd.jpg", name);
	}

	@Test
	public void mainNameTest() {
		String path = "d:\\aaa\\bbb\\cc\\ddd\\";
		String mainName = FileNameUtil.mainName(path);
		Assertions.assertEquals("ddd", mainName);

		path = "d:\\aaa\\bbb\\cc\\ddd";
		mainName = FileNameUtil.mainName(path);
		Assertions.assertEquals("ddd", mainName);

		path = "d:\\aaa\\bbb\\cc\\ddd.jpg";
		mainName = FileNameUtil.mainName(path);
		Assertions.assertEquals("ddd", mainName);
	}

	@Test
	public void extNameTest() {
		String path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd\\" : "~/Desktop/hutool/ddd/";
		String mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd" : "~/Desktop/hutool/ddd";
		mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\ddd.jpg" : "~/Desktop/hutool/ddd.jpg";
		mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("jpg", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.xlsx" : "~/Desktop/hutool/fff.xlsx";
		mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("xlsx", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.gz" : "~/Desktop/hutool/fff.tar.gz";
		mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("tar.gz", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.Z" : "~/Desktop/hutool/fff.tar.Z";
		mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("tar.Z", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.bz2" : "~/Desktop/hutool/fff.tar.bz2";
		mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("tar.bz2", mainName);

		path = FileUtil.isWindows() ? "d:\\aaa\\bbb\\cc\\fff.tar.xz" : "~/Desktop/hutool/fff.tar.xz";
		mainName = FileNameUtil.extName(path);
		Assertions.assertEquals("tar.xz", mainName);
	}

	@Test
	@EnabledForJreRange(max = JRE.JAVA_8)
	public void getWebRootTest() {
		// JDK9+环境中，由于模块问题，junit获取的classpath路径和实际不同
		final File webRoot = FileUtil.getWebRoot();
		Assertions.assertNotNull(webRoot);
		Assertions.assertEquals("hutool-core", webRoot.getName());
	}

	@Test
	public void getMimeTypeTest() {
		String mimeType = FileUtil.getMimeType("test2Write.jpg");
		Assertions.assertEquals("image/jpeg", mimeType);

		mimeType = FileUtil.getMimeType("test2Write.html");
		Assertions.assertEquals("text/html", mimeType);

		mimeType = FileUtil.getMimeType("main.css");
		Assertions.assertEquals("text/css", mimeType);

		mimeType = FileUtil.getMimeType("test.js");
		// 在 jdk 11+ 会获取到 text/javascript,而非 自定义的 application/x-javascript
		final List<String> list = ListUtil.of("text/javascript", "application/x-javascript");
		Assertions.assertTrue(list.contains(mimeType));

		// office03
		mimeType = FileUtil.getMimeType("test.doc");
		Assertions.assertEquals("application/msword", mimeType);
		mimeType = FileUtil.getMimeType("test.xls");
		Assertions.assertEquals("application/vnd.ms-excel", mimeType);
		mimeType = FileUtil.getMimeType("test.ppt");
		Assertions.assertEquals("application/vnd.ms-powerpoint", mimeType);

		// office07+
		mimeType = FileUtil.getMimeType("test.docx");
		Assertions.assertEquals("application/vnd.openxmlformats-officedocument.wordprocessingml.document", mimeType);
		mimeType = FileUtil.getMimeType("test.xlsx");
		Assertions.assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", mimeType);
		mimeType = FileUtil.getMimeType("test.pptx");
		Assertions.assertEquals("application/vnd.openxmlformats-officedocument.presentationml.presentation", mimeType);

		// pr#2617@Github
		mimeType = FileUtil.getMimeType("test.wgt");
		Assertions.assertEquals("application/widget", mimeType);

		// issue#3092
		mimeType = FileUtil.getMimeType("https://xxx.oss-cn-hangzhou.aliyuncs.com/xxx.webp");
		Assertions.assertEquals("image/webp", mimeType);
	}

	@Test
	public void isSubTest() {
		final File file = new File("d:/test");
		final File file2 = new File("d:/test2/aaa");
		Assertions.assertFalse(FileUtil.isSub(file, file2));
	}

	@Test
	public void isSubRelativeTest() {
		final File file = new File("..");
		final File file2 = new File(".");
		Assertions.assertTrue(FileUtil.isSub(file, file2));
	}

	@Test
	@Disabled
	public void appendLinesTest() {
		final List<String> list = ListUtil.of("a", "b", "c");
		FileUtil.appendLines(list, FileUtil.file("d:/test/appendLines.txt"), CharsetUtil.UTF_8);
	}

	@Test
	public void createTempFileTest() {
		final File nullDirTempFile = FileUtil.createTempFile();
		Assertions.assertTrue(nullDirTempFile.exists());

		final File suffixDirTempFile = FileUtil.createTempFile(".xlsx", true);
		Assertions.assertEquals("xlsx", FileNameUtil.getSuffix(suffixDirTempFile));

		final File prefixDirTempFile = FileUtil.createTempFile("prefix", ".xlsx", true);
		Console.log(prefixDirTempFile);
		Assertions.assertTrue(FileNameUtil.getPrefix(prefixDirTempFile).startsWith("prefix"));
	}

	@Test
	@Disabled
	public void getTotalLinesTest() {
		// 千万行秒级内返回
		final int totalLines = FileUtil.getTotalLines(FileUtil.file(""));
		Assertions.assertEquals(10000000, totalLines);
	}

	@Test
	public void isAbsolutePathTest() {
		String path = "d:/test\\aaa.txt";
		Assertions.assertTrue(FileUtil.isAbsolutePath(path));

		path = "test\\aaa.txt";
		Assertions.assertFalse(FileUtil.isAbsolutePath(path));
	}

	@Test
	@Disabled
	void readBytesTest() {
		final byte[] bytes = FileUtil.readBytes("test.properties");
		Assertions.assertEquals(125, bytes.length);
	}

	@Test
	void checkSlipTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			FileUtil.checkSlip(FileUtil.file("test/a"), FileUtil.file("test/../a"));
		});
	}
}
