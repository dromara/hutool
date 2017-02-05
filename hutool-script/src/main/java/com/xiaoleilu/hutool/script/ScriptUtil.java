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
public class ScriptUtil {
	public static ScriptEngine getScript(String name){
		return new ScriptEngineManager().getEngineByName(name);
	}
	
	public static JavaScriptEngine getJavaScriptEngine(){
		return new JavaScriptEngine();
	}
	
	public static CompiledScript compile(ScriptEngine engine, String script) throws ScriptException{
		if(engine instanceof Compilable){
			Compilable compEngine = (Compilable)engine;
			return compEngine.compile(script);
		}
		return null;
	}
}
