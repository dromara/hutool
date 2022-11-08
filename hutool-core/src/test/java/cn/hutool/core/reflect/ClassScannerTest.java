package cn.hutool.core.reflect;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;

public class ClassScannerTest {

	@Test
	@Ignore
	public void scanTest() {
		final ClassScanner scaner = new ClassScanner("cn.hutool.core.util", null);
		final Set<Class<?>> set = scaner.scan();
		for (final Class<?> clazz : set) {
			Console.log(clazz.getName());
		}
	}

	@Test
	@Ignore
	public void scanPackageBySuperTest() {
		// 扫描包，如果在classpath下找到，就不扫描JDK的jar了
		final Set<Class<?>> classes = ClassScanner.scanPackageBySuper(null, Iterable.class);
		Console.log(classes.size());
	}

	@Test
	@Ignore
	public void scanAllPackageBySuperTest() {
		// 扫描包，如果在classpath下找到，就不扫描JDK的jar了
		final Set<Class<?>> classes = ClassScanner.scanAllPackageBySuper(null, Iterable.class);
		Console.log(classes.size());
	}

	@Test
	@Ignore
	public void scanAllPackageIgnoreLoadErrorTest() {
		final ClassScanner classScanner = new ClassScanner(null, null);
		classScanner.setIgnoreLoadError(true);
		final Set<Class<?>> classes = classScanner.scan(false);
		Console.log(classes.size());
		Console.log(classScanner.getClassesOfLoadError());
	}
}
