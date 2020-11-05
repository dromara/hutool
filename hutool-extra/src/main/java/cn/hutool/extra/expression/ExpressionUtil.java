package cn.hutool.extra.expression;

import cn.hutool.extra.expression.engine.aviator.AviatorExpressionEngine;
import cn.hutool.extra.expression.engine.jexl.JexlExpressionEngine;
import cn.hutool.extra.expression.engine.mvel.MvelExpressionEngine;

import java.util.Map;

/**
 * 表达式工具类
 *
 * @author independenter
 */
public class ExpressionUtil {

    /**
     * 根据上下文获得表达式结果
     * @param expr 表达式
     * @param content 上下文
     * @param language 语言类型
     * @return
     */
    public static Object get(String expr,Map<String,Object> content, ExpressionLanguage language){
        ExpressionEngine engine = null;
        switch (language){
            case jexl:
                engine = new JexlExpressionEngine(content);
                break;
            case mvel:
                engine = new MvelExpressionEngine(content);
                break;
            case aviator:
                engine = new AviatorExpressionEngine(content);
                break;
            default:
                throw new ExpressionException("不支持的表达式语言:" + language);
        }
        return engine.get(expr);
    }

    /**
     * @param expr 表达式
     * @param content 上下文
     * @param language 语言类型
     * @return 表达式转换的boolean,报错默认为false
     */
    public static boolean getBoolean(String expr,Map<String,Object> content, ExpressionLanguage language){
        return getBoolean(expr,content,language,false);
    }

    /**
     * @param expr 表达式
     * @param content 上下文
     * @param language 语言类型
     * @param defaultResult 解析报错的默认返回
     * @return 表达式转换的boolean,结果为空和报错为public staticResult
     */
    public static boolean getBoolean(String expr,Map<String,Object> content, ExpressionLanguage language,boolean defaultResult){
        Object result = get(expr,content,language);
        try {
            return result == null ? defaultResult:(Boolean)result;
        }catch (Exception e){}
        return defaultResult;
    };

    /**
     * @param expr 表达式
     * @param content 上下文
     * @param language 语言类型
     * @return 表达式转换的String,报错返回expr
     */
    public static String getString(String expr,Map<String,Object> content, ExpressionLanguage language){
        Object result = get(expr,content,language);
        try {
            return (String)result;
        }catch (Exception e){}
        return expr;
    };

    /**
     * @param expr 表达式
     * @param content 上下文
     * @param language 语言类型
     * @return 表达式转换的Integer,报错返回null
     */
    public static Integer getInterger(String expr,Map<String,Object> content, ExpressionLanguage language){
        Object result = get(expr,content,language);
        try {
            return (Integer)result;
        }catch (Exception e){}
        return null;
    };
}
