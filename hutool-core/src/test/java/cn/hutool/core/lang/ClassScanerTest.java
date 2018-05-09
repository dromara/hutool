package cn.hutool.core.lang;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class ClassScanerTest {
	
	@Test
	public void scanTest() {
		Set<Class<?>> result = ClassScaner.scanPackage("cn.hutool.core");
//		for (Class<?> clazz : result) {
//			Console.log(clazz.getName());
//		}
		Assert.assertFalse(result.isEmpty());
	}
}
