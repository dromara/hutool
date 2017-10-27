package com.xiaoleilu.hutool.script;

import org.junit.Test;

/**
 * 脚本单元测试类
 * @author looly
 *
 */
public class ScriptUtilTest {
	
	@Test
	public void compileTest() {
		ScriptUtil.eval("println('Script test!');");
	}
	
}
