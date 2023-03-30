package cn.hutool.core.io.file;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemUtilTest {

	@Test
	@Disabled
	public void listTest(){
		final FileSystem fileSystem = FileSystemUtil.createZip("d:/test/test.zip",
				CharsetUtil.GBK);
		final Path root = FileSystemUtil.getRoot(fileSystem);
		PathUtil.walkFiles(root, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) {
				Console.log(path);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
