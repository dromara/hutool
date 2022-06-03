package cn.hutool.core.compiler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.compress.ZipUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.Resource;

import javax.tools.JavaFileObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * {@link JavaFileObject} 相关工具类封装
 *
 * @author lzpeng, looly
 * @since 5.5.2
 */
public class JavaFileObjectUtil {

	/**
	 * 获取指定资源下的所有待编译的java源码文件，并以{@link JavaFileObject}形式返回
	 * <ul>
	 *     <li>如果资源为目录，则遍历目录找到目录中的.java或者.jar等文件加载之</li>
	 *     <li>如果资源为.jar或.zip等，解压读取其中的.java文件加载之</li>
	 *     <li>其他情况直接读取资源流并加载之</li>
	 * </ul>
	 *
	 * @param resource 资源，可以为目录、文件或流
	 * @return 所有待编译的 {@link JavaFileObject}
	 */
	public static List<JavaFileObject> getJavaFileObjects(final Resource resource) {
		final List<JavaFileObject> result = new ArrayList<>();

		if (resource instanceof FileResource) {
			final File file = ((FileResource) resource).getFile();
			result.addAll(JavaFileObjectUtil.getJavaFileObjects(file));
		} else {
			result.add(new JavaSourceFileObject(resource.getName(), resource.getStream()));
		}

		return result;
	}

	/**
	 * 获取指定文件下的所有待编译的java文件，并以{@link JavaFileObject}形式返回
	 * <ul>
	 *     <li>如果文件为目录，则遍历目录找到目录中的.java或者.jar等文件加载之</li>
	 *     <li>如果文件为.jar或.zip等，解压读取其中的.java文件加载之</li>
	 * </ul>
	 *
	 * @param file 文件或目录，文件支持.java、.jar和.zip文件
	 * @return 所有待编译的 {@link JavaFileObject}
	 */
	public static List<JavaFileObject> getJavaFileObjects(final File file) {
		final List<JavaFileObject> result = new ArrayList<>();
		if (file.isDirectory()) {
			FileUtil.walkFiles(file, (subFile) -> result.addAll(getJavaFileObjects(file)));
		} else {
			final String fileName = file.getName();
			if (isJavaFile(fileName)) {
				result.add(new JavaSourceFileObject(file.toURI()));
			} else if (isJarOrZipFile(fileName)) {
				result.addAll(getJavaFileObjectByZipOrJarFile(file));
			}
		}

		return result;
	}

	/**
	 * 是否是jar 或 zip 文件<br>
	 * 通过扩展名判定
	 *
	 * @param fileName 文件名
	 * @return 是否是jar 或 zip 文件
	 */
	public static boolean isJarOrZipFile(final String fileName) {
		return FileNameUtil.isType(fileName, "jar", "zip");
	}

	/**
	 * 是否是java文件<br>
	 * 通过扩展名判定
	 *
	 * @param fileName 文件名
	 * @return 是否是.java文件
	 */
	public static boolean isJavaFile(final String fileName) {
		return FileNameUtil.isType(fileName, "java");
	}

	/**
	 * 通过zip包或jar包创建Java文件对象
	 *
	 * @param file 压缩文件
	 * @return Java文件对象
	 */
	private static List<JavaFileObject> getJavaFileObjectByZipOrJarFile(final File file) {
		final List<JavaFileObject> collection = new ArrayList<>();
		final ZipFile zipFile = ZipUtil.toZipFile(file, null);
		ZipUtil.read(zipFile, (zipEntry) -> {
			final String name = zipEntry.getName();
			if (isJavaFile(name)) {
				collection.add(new JavaSourceFileObject(name, ZipUtil.getStream(zipFile, zipEntry)));
			}
		});
		return collection;
	}
}
