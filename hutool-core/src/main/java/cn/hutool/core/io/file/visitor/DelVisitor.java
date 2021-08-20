package cn.hutool.core.io.file.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 删除操作的FileVisitor实现，用于递归遍历删除文件夹
 *
 * @author looly
 * @since 5.5.1
 */
public class DelVisitor extends SimpleFileVisitor<Path> {

	public static DelVisitor INSTANCE = new DelVisitor();

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}

	/**
	 * 访问目录结束后删除目录，当执行此方法时，子文件或目录都已访问（删除）完毕<br>
	 * 理论上当执行到此方法时，目录下已经被清空了
	 *
	 * @param dir 目录
	 * @param e   异常
	 * @return {@link FileVisitResult}
	 * @throws IOException IO异常
	 */
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
		if (e == null) {
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		} else {
			throw e;
		}
	}
}
