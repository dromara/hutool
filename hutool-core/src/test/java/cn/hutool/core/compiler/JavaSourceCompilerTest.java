package cn.hutool.core.compiler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.ZipUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Java源码编译器测试
 *
 * @author lzpeng
 */
public class JavaSourceCompilerTest {

	/**
	 * 测试编译Java源码
	 */
	@Test
	public void testCompile() throws ClassNotFoundException {
		// 依赖A，编译B和C
		final File libFile = ZipUtil.zip(FileUtil.file("lib.jar"),
				new String[]{"a/A.class", "a/A$1.class", "a/A$InnerClass.class"},
				new InputStream[]{
						FileUtil.getInputStream("test-compile/a/A.class"),
						FileUtil.getInputStream("test-compile/a/A$1.class"),
						FileUtil.getInputStream("test-compile/a/A$InnerClass.class")
				});
		final ClassLoader classLoader = CompilerUtil.getCompiler(null)
				.addSource(FileUtil.file("test-compile/b/B.java"))
				.addSource("c.C", FileUtil.readUtf8String("test-compile/c/C.java"))
				.addLibrary(libFile)
//				.addLibrary(FileUtil.file("D:\\m2_repo\\cn\\hutool\\hutool-all\\5.5.7\\hutool-all-5.5.7.jar"))
				.compile();
		final Class<?> clazz = classLoader.loadClass("c.C");
		Object obj = ReflectUtil.newInstance(clazz);
		Assert.assertTrue(String.valueOf(obj).startsWith("c.C@"));
	}

}
