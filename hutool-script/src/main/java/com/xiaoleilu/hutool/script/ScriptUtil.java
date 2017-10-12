package com.xiaoleilu.hutool.script;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 脚本工具类
 * @author Looly
 *
 */
public final class ScriptUtil {
	
	/**
	 * 获得 {@link ScriptEngine} 实例
	 * @param name 脚本名称
	 * @return {@link ScriptEngine} 实例
	 */
	public static ScriptEngine getScript(String name){
		return new ScriptEngineManager().getEngineByName(name);
	}
	
	/**
	 * 获得 Javascript引擎 {@link JavaScriptEngine}
	 * @return {@link JavaScriptEngine}
	 */
	public static JavaScriptEngine getJavaScriptEngine(){
		return new JavaScriptEngine();
	}
	
	/**
	 * 编译脚本
	 * @param engine 引擎
	 * @param script 脚本内容
	 * @return {@link CompiledScript}
	 * @throws ScriptException 脚本异常
	 */
	public static CompiledScript compile(ScriptEngine engine, String script) throws ScriptException{
		if(engine instanceof Compilable){
			Compilable compEngine = (Compilable)engine;
			return compEngine.compile(script);
		}
		return null;
	}
}
