package cn.hutool.extra.expression.engine.aviator;

import cn.hutool.extra.expression.ExpressionEngine;
import com.googlecode.aviator.AviatorEvaluator;
import org.apache.commons.jexl3.JexlBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * @author independenter
 * @since
 */
public class AviatorExpressionEngine implements ExpressionEngine {

    Map<String,Object> mvelMap = new HashMap<>();

    public AviatorExpressionEngine(){

    }

    public AviatorExpressionEngine(Map<String,Object> map){
        set(map);
    }

    @Override
    public Object get(String expr) {
        return AviatorEvaluator.execute(expr, mvelMap);
    }

    @Override
    public void set(final Map<String, Object> map) {
        map.forEach(mvelMap::put);
    }

    @Override
    public void set(String key, Object value) {
        mvelMap.put(key,value);
    }
}
