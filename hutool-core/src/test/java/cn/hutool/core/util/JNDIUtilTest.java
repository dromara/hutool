package cn.hutool.core.util;

import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class JNDIUtilTest {

	@Test
	@Ignore
	public void getDnsTest(){
		final Attributes attributes = JNDIUtil.getAttributes("dns:hutool.cn", "TXT");
		for (Attribute attribute: new EnumerationIter<>(attributes.getAll())){
			Console.log(attribute);
		}
	}
}
