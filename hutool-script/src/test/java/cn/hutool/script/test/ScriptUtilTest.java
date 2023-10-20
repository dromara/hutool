package cn.hutool.script.test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.script.ScriptRuntimeException;
import cn.hutool.script.ScriptUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.concurrent.TimeUnit;

/**
 * 脚本单元测试类
 *
 * @author looly
 *
 */
public class ScriptUtilTest {

	@Test
	public void compileTest() {
		CompiledScript script = ScriptUtil.compile("print('Script test!');");
		try {
			script.eval();
		} catch (ScriptException e) {
			throw new ScriptRuntimeException(e);
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

	@Test
	public void pythonTest() throws ScriptException {
		final ScriptEngine pythonEngine = ScriptUtil.getPythonEngine();
		pythonEngine.eval("print('Hello Python')");
	}

	@Test
	public void luaTest() throws ScriptException {
		final ScriptEngine engine = ScriptUtil.getLuaEngine();
		engine.eval("print('Hello Lua')");
	}

	@Test
	public void groovyTest() throws ScriptException {
		final ScriptEngine engine = ScriptUtil.getGroovyEngine();
		engine.eval("println 'Hello Groovy'");
	}

	/**
	 *  多线程环境下模拟脚本引擎变量交替初始化执行结果是否正确
	 */
	public static void groovySafeEnginTest() throws ScriptException {

		new Thread(()->{
			try {
				System.out.println("groovySafeEnginTest-1-start");
				ScriptEngine groovyEngine = ScriptUtil.getScriptEngine("groovy");
				groovyEngine.put("s","test1");
				Object result = groovyEngine.eval("return s");
				System.out.println("线程1,s="+result);
				TimeUnit.SECONDS.sleep(3);
				groovyEngine.put("s","test1");
				System.out.println("groovySafeEnginTest-1-end");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(()->{
			try {
				System.out.println("groovySafeEnginTest-2-start");
				ScriptEngine groovyEngine = ScriptUtil.getScriptEngine("groovy");
				groovyEngine.put("s","test2");
				TimeUnit.SECONDS.sleep(4);
				Object result = groovyEngine.eval("return s");
				System.out.println("线程2,s="+result);
				System.out.println("groovySafeEnginTest-2-end");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		try {
			groovySafeEnginTest();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}
