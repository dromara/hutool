package cn.hutool.extra.expression;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.expression.engine.jexl.JexlExpressionEngine;
import cn.hutool.extra.expression.engine.mvel.MvelExpressionEngine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.mvel2.PropertyAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * @author dobb
 * @since
 */
public class ExpressionTest {

    private ExpressionEngine engine;

    @Before
    public void setUp(){
        //engine = new JexlExpressionEngine();
        engine = new MvelExpressionEngine();
    }

    @Rule
    public TestName timeout = new TestName(){
        @Override
        protected void starting(Description d) {
            System.out.println(String.format("%s starting ...", d.getMethodName()));
            super.starting(d);
        }
    };

    @Test
    public void utilTest() {
        Map<String,Object> jc = new HashMap<>();
        List<Object> l = new ArrayList<Object>();
        l.add("Hello from location 0");
        Integer two = 2;
        l.add(two);
        l.add(true);
        jc.put("array", l);

        boolean result = ExpressionUtil.getBoolean("array[2]",jc,ExpressionLanguage.aviator);
        Console.log(result);
    }

    @Test
    public void arrayTest() {
        Map<String,Object> jc = new HashMap<>();
        List<Object> l = new ArrayList<Object>();
        l.add("Hello from location 0");
        Integer two = 2;
        l.add(two);
        l.add(true);
        jc.put("array", l);

        engine.set(jc);
        boolean result = engine.getBoolean("array[2]");
        Console.log(result);
        Integer result1 = engine.getInterger("array[1]");
        Console.log(result1);
        Integer result3 = engine.getInterger("array[0].length()");
        Console.log(result3);
        boolean result4 = engine.getBoolean("array[1] == " + two);
        Console.log(result4);

    }

    @Test
    public void methodPropertyTest() {
        Map<String,Object> jc = new HashMap<>();
        Foo foo = new Foo();
        Integer number = 10;
        jc.put("foo", foo);
        jc.put("number", number);

        engine.set(jc);
        String result = engine.getString("foo.getFoo");
        Console.log(result);
        result = engine.getString("foo.convert(1)");
        Console.log(result);
        result = engine.getString("foo.convert(1+7)");
        Console.log(result);
        result = engine.getString("foo.convert(1+number)");
        Console.log(result);

        if(engine instanceof JexlExpressionEngine){
            //jexl会执行 foo.get('bar')
            result = engine.getString("foo.bar");
            Console.log(result);
        }if(engine instanceof MvelExpressionEngine){
            //mvel会取值 foo.bar
            Assert.assertThrows("mvel不能取到属性", PropertyAccessException.class,() -> {engine.getString("foo.bar");});
        }else{
            result = engine.getString("foo.bar");
            Console.log(result);
        }

    }

    /**
     * Helper example class.
     */
    public static class Foo {
        /**
         * Gets foo.
         * @return a string.
         */
        public String getFoo() {
            return "This is from getFoo()";
        }

        /**
         * Gets an arbitrary property.
         * @param arg property name.
         * @return arg prefixed with 'This is the property '.
         */
        public String get(String arg) {
            return "This is the property " + arg;
        }

        /**
         * Gets a string from the argument.
         * @param i a long.
         * @return The argument prefixed with 'The value is : '
         */
        public String convert(long i) {
            return "The value is : " + i;
        }
    }
}
