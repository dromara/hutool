package cn.hutool.core.util;

import cn.hutool.core.collection.EnumerationIter;
import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public class JNDIUtilTest {

	@Test
	@Ignore
	public void getDnsTest() throws NamingException {
		final Attributes attributes = JNDIUtil.getAttributes("dns:paypal.com", "TXT");
		for (Attribute attribute: new EnumerationIter<>(attributes.getAll())){
			Console.log(attribute.get());
		}
	}
}
