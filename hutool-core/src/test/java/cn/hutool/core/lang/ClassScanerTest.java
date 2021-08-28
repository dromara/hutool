package cn.hutool.core.lang;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;

public class ClassScanerTest {

	@Test
	@Ignore
	public void scanTest() {
		ClassScanner scaner = new ClassScanner("cn.hutool.core.util", null);
		Set<Class<?>> set = scaner.scan();
		for (Class<?> clazz : set) {
			Console.log(clazz.getName());
		}
	}

	@Test
	@Ignore
	public void scanPackageBySuperTest(){
		// 扫描包，如果在classpath下找到，就不扫描JDK的jar了
		final Set<Class<?>> classes = ClassScanner.scanPackageBySuper(null, Iterable.class);
		Console.log(classes.size());
	}

	@Test
	@Ignore
	public void scanAllPackageBySuperTest(){
		// 扫描包，如果在classpath下找到，就不扫描JDK的jar了
		final Set<Class<?>> classes = ClassScanner.scanAllPackageBySuper(null, Iterable.class);
		Console.log(classes.size());
	}
}
