package cn.hutool.core.io.file;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 移动情况测试，环境：
 * <pre>
 *     d:/test/dir1/test1.txt   文件
 *     d:/test/dir2/            空目录
 * </pre>
 */
public class IssueI666HBTest {

	@Test
	@Disabled
	public void moveDirToDirTest() {
		// 目录移动到目录，将整个目录移动
		// 会将dir1及其内容移动到dir2下，变成dir2/dir1
		FileUtil.move(FileUtil.file("d:/test/dir1"), FileUtil.file("d:/test/dir2"), false);
	}

	@Test
	@Disabled
	public void moveContentDirToDirTest() {
		// 目录内容移动到目录
		// 移动内容，不移除目录本身
		PathUtil.moveContent(
				FileUtil.file("d:/test/dir1").toPath(),
				FileUtil.file("d:/test/dir2").toPath(), false);
	}

	@Test
	@Disabled
	public void moveFileToDirTest() {
		// 文件移动到目录
		// 会将test1.txt移动到dir2下，变成dir2/test1.txt
		FileUtil.move(FileUtil.file("d:/test/dir1/test1.txt"), FileUtil.file("d:/test/dir2"), false);
	}

	@Test
	@Disabled
	public void moveContentFileToDirTest() {
		// 文件移动到目录
		// 会将test1.txt移动到dir2下，变成dir2/test1.txt
		PathUtil.moveContent(
				FileUtil.file("d:/test/dir1/test1.txt").toPath(),
				FileUtil.file("d:/test/dir2").toPath(), false);
	}

	@Test
	@Disabled
	public void moveDirToDirNotExistTest() {
		// 目录移动到目标，dir3不存在，将整个目录移动
		// 会将目录dir1变成目录dir3
		FileUtil.move(FileUtil.file("d:/test/dir1"), FileUtil.file("d:/test/dir3"), false);
	}

	@Test
	@Disabled
	public void moveContentDirToDirNotExistTest() {
		// 目录移动到目标，dir3不存在
		// 会将目录dir1内容移动到dir3，但是dir1目录不删除
		PathUtil.moveContent(
				FileUtil.file("d:/test/dir1").toPath(),
				FileUtil.file("d:/test/dir3").toPath(), false);
	}

	@Test
	@Disabled
	public void moveFileToTargetNotExistTest() {
		// 文件移动到不存在的路径
		// 会将test1.txt重命名为test2
		FileUtil.move(FileUtil.file("d:/test/dir1/test1.txt"), FileUtil.file("d:/test/test2"), false);
	}

	@Test
	@Disabled
	public void moveContentFileToTargetNotExistTest() {
		// 目录移动到目录，将整个目录移动
		// 会将test1.txt重命名为test2
		PathUtil.moveContent(
				FileUtil.file("d:/test/dir1/test1.txt").toPath(),
				FileUtil.file("d:/test/test2").toPath(), false);
	}
}
