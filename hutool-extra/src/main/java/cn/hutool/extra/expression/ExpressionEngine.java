package cn.hutool.extra.expression;

import cn.hutool.core.util.ObjectUtil;

import java.util.Map;

/**
 * <p>
 *
 * @author independenter
 * @since
 */
public interface ExpressionEngine {

    /**
     * @param expr 表达式
     * @return 表达式转换的Object
     */
    Object get(final String expr);

    /**
     * @param map 参数
     */
    void set(final Map<String,Object> map);

    /**
     * @param key
     * @param value
     */
    void set(String key,Object value);

    /**
     * @param expr 表达式
     * @return 表达式转换的boolean,报错默认为false
     */
    default boolean getBoolean(String expr){
        return getBoolean(expr,false);
    }

    /**
     * @param expr 表达式
     * @param defaultResult 解析报错的默认返回
     * @return 表达式转换的boolean,结果为空和报错为defaultResult
     */
    default boolean getBoolean(String expr,boolean defaultResult){
        Object result = get(expr);
        try {
            return result == null ? defaultResult:(Boolean)result;
        }catch (Exception e){}
        return defaultResult;
    };

    /**
     * @param expr 表达式
     * @return 表达式转换的String,报错返回expr
     */
    default String getString(String expr){
        Object result = get(expr);
        try {
            return (String)result;
        }catch (Exception e){}
        return expr;
    };

    /**
     * @param expr 表达式
     * @return 表达式转换的Integer,报错返回null
     */
    default Integer getInterger(String expr){
        Object result = get(expr);
        try {
            return (Integer)result;
        }catch (Exception e){}
        return null;
    };
}
