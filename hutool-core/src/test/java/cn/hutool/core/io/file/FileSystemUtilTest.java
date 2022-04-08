package cn.hutool.core.io.file;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemUtilTest {

	@Test
	@Ignore
	public void listTest(){
		final FileSystem fileSystem = FileSystemUtil.createZip("d:/test/test.zip",
				CharsetUtil.CHARSET_GBK);
		final Path root = FileSystemUtil.getRoot(fileSystem);
		PathUtil.walkFiles(root, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				Console.log(path);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
