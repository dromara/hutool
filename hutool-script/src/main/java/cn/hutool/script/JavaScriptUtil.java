package cn.hutool.script;

import cn.hutool.core.io.FileUtil;
import javax.script.ScriptException;
import java.io.File;

/**
 * JavaScript脚本工具类
 * 可用于爬虫 请求参数的计算
 *
 * @author dahuoyzs
 *
 */
public class JavaScriptUtil {

    private static JavaScriptEngine javaScriptEngine = JavaScriptEngine.instance();

    /**
     * 加载JavaScript脚本，并计算出结果<br>
     *
     * @param script        JavaScript脚本
     * @param method        需要调用的方法
     * @param args          方法的参数列表
     * @return Object       计算结果
     * @throws ScriptRuntimeException 脚本异常
     * @since 5.3.3
     */
    public static Object exec(String script,String method,Object... args) throws ScriptRuntimeException {
        try {
            javaScriptEngine.eval(script);
            return javaScriptEngine.invokeFunction(method,args);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptRuntimeException(e);
        }
    }

    /**
     * 加载JavaScript脚本文件，并计算出结果<br>
     *
     * @param scriptFile    JavaScript脚本文件
     * @param method        需要调用的方法
     * @param args          方法的参数列表
     * @return Object       计算结果
     * @throws ScriptRuntimeException 脚本异常
     * @since 5.3.3
     */
    public static Object exec(File scriptFile, String method, Object... args) throws ScriptRuntimeException {
        try {
            javaScriptEngine.eval(FileUtil.readString(scriptFile,"UTF-8"));
            return javaScriptEngine.invokeFunction(method,args);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptRuntimeException(e);
        }
    }

}
