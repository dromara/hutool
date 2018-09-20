package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.clone.CloneSupport;

public class ObjectUtilTest {
	
	@Test
	public void cloneTest() {
		Obj obj = new Obj();
		Obj obj2 = ObjectUtil.clone(obj);
		Assert.assertEquals("OK", obj2.doSomeThing());
	}
	
	static class Obj extends CloneSupport<Obj>{
		public String doSomeThing() {
			return "OK";
		}
	}
}
