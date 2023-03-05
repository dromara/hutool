package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 移动情况测试，环境：
 * <pre>
 *     d:/test/dir1/test1.txt   文件
 *     d:/test/dir2/            空目录
 * </pre>
 */
public class IssueI666HBTest {

	@Test
	@Ignore
	public void moveDirToDirTest() {
		// 目录移动到目录，将整个目录移动
		// 会将dir1及其内容移动到dir2下，变成dir2/dir1
		FileUtil.move(FileUtil.file("d:/test/dir1"), FileUtil.file("d:/test/dir2"), false);
	}

	@Test
	@Ignore
	public void moveFileToDirTest() {
		// 文件移动到目录
		// 会将test1.txt移动到dir2下，变成dir2/test1.txt
		FileUtil.move(FileUtil.file("d:/test/dir1/test1.txt"), FileUtil.file("d:/test/dir2"), false);
	}

	@Test
	@Ignore
	public void moveDirToDirNotExistTest() {
		// 目录移动到目标，dir3不存在，将整个目录移动
		// 会将目录dir1变成目录dir3
		FileUtil.move(FileUtil.file("d:/test/dir1"), FileUtil.file("d:/test/dir3"), false);
	}

	@Test
	@Ignore
	public void moveFileToTargetNotExistTest() {
		// 目录移动到目录，将整个目录移动
		// 会将test1.txt重命名为test2
		FileUtil.move(FileUtil.file("d:/test/dir1/test1.txt"), FileUtil.file("d:/test/test2"), false);
	}
}
