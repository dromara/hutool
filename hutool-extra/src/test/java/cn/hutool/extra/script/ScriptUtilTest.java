package cn.hutool.extra.script;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.script.CompiledScript;
import javax.script.ScriptException;

/**
 * 脚本单元测试类
 *
 * @author looly
 *
 */
public class ScriptUtilTest {

	@Test
	public void compileTest() {
		final CompiledScript script = ScriptUtil.compile("print('Script test!');");
		try {
			script.eval();
		} catch (final ScriptException e) {
			throw new UtilException(e);
		}
	}

	@Test
	public void evalTest() {
		ScriptUtil.eval("print('Script test!');");
	}

	@Test
	public void invokeTest() {
		final Object result = ScriptUtil.invoke(ResourceUtil.readUtf8Str("filter1.js"), "filter1", 2, 1);
		Assert.assertTrue((Boolean) result);
	}
}
