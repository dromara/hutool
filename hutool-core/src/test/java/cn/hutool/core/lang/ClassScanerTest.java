package cn.hutool.core.lang;

import java.util.Set;

import org.junit.Test;

public class ClassScanerTest {
	
	@Test
//	@Ignore
	public void scanTest() {
		ClassScaner scaner = new ClassScaner("cn.hutool.core.util.StrUtil", null);
		Set<Class<?>> set = scaner.scan();
		for (Class<?> clazz : set) {
			Console.log(clazz.getName());
		}
	}
}
