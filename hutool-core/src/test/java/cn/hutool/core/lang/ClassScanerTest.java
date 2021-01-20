package cn.hutool.core.lang;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

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
}
