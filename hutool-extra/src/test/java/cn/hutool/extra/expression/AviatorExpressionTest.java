package cn.hutool.extra.expression;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.expression.engine.aviator.AviatorExpressionEngine;
import com.googlecode.aviator.AviatorEvaluator;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import lombok.Data;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * <p>
 *
 * @author independenter
 * @since
 */
public class AviatorExpressionTest {

    @Data
    public static class Bar {
        public Bar() {
            this.name = "bar";
        }
        private String name;
    }

    @Data
    public static class Foo {
        int i;
        float f;
        Date date = new Date();
        Bar[] bars = new Bar[1];
        public Foo(final int i, final float f, final Date date) {
            super();
            this.i = i;
            this.f = f;
            this.date = date;
            this.bars[0] = new Bar();
        }
    }

    @Test
    public void simpleTest(){
        Foo foo = new Foo(100, 3.14f, new Date());
        ExpressionEngine engine = new AviatorExpressionEngine(){{set("foo",foo);}};
        String exp =
                "\"[foo i=\"+ foo.i + \", f=\" + foo.f + \", date.year=\" + (foo.date.year+1900) + \", date.month=\" + foo.date.month + \", bars[0].name=\" + #foo.bars[0].name + \"]\"";
        String result = engine.getString(exp);
        Console.log("Execute expression: " + exp);
        Console.log("Result: " + result);

        // Assignment.
        exp = "#foo.bars[0].name='hello aviator' ; #foo.bars[0].name";
        result = engine.getString(exp);
        Console.log("Execute expression: " + exp);
        Console.log("Result: " + result);
        Console.log(foo.bars[0].getName());

        exp = "foo.bars[0] = nil ; foo.bars[0]";
        result = engine.getString(exp);
        Console.log("Execute expression: " + exp);
        Console.log("Result: " + result);
        Console.log(foo.bars[0]);
    }
}
