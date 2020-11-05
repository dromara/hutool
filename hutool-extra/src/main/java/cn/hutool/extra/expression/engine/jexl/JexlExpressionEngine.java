package cn.hutool.extra.expression.engine.jexl;

import cn.hutool.extra.expression.ExpressionEngine;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.introspection.JexlSandbox;

import java.util.Map;

/**
 * 基于https://github.com/apache/commons-jexl
 * <p>
 *
 * @author independenter
 * @since
 */
public class JexlExpressionEngine implements ExpressionEngine {

    private JexlEngine jexl;
    private JexlContext jexlContext = new MapContext();

    public JexlExpressionEngine(){
        jexl = new JexlBuilder().create();
    }

    public JexlExpressionEngine(Map<String,Object> map){
        this();
        set(map);
    }

    public JexlExpressionEngine(Map<String,Object> map,int cache){
        jexl = new JexlBuilder().cache(cache).create();
        set(map);
    }

    @Override
    public Object get(final String expr) {
        JexlExpression e = jexl.createExpression(expr);
        return e.evaluate(jexlContext);
    }

    @Override
    public void set(final Map<String, Object> map) {
        map.forEach(jexlContext::set);
    }

    @Override
    public void set(String key, Object value) {
        jexlContext.set(key,value);
    }
}
