package cn.hutool.core.io.file.visitor;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件拷贝的FileVisitor实现，用于递归遍历拷贝目录
 *
 * @author looly
 * @since 5.5.1
 */
public class CopyVisitor extends SimpleFileVisitor<Path> {

	final Path source;
	final Path target;

	public CopyVisitor(Path source, Path target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		final Path targetDir = target.resolve(source.relativize(dir));
		try {
			Files.copy(dir, targetDir);
		} catch (FileAlreadyExistsException e) {
			if (!Files.isDirectory(targetDir))
				throw e;
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		Files.copy(file, target.resolve(source.relativize(file)));
		return FileVisitResult.CONTINUE;
	}
}
