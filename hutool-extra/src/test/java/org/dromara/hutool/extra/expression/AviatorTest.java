package org.dromara.hutool.extra.expression;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.extra.expression.engine.aviator.AviatorEngine;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * Aviator引擎单元测试，来自https://github.com/looly/hutool/pull/1203
 */
public class AviatorTest {

	@Test
	public void simpleTest(){
		final Foo foo = new Foo(100, 3.14f, DateUtil.parse("2020-11-12"));
		final ExpressionEngine engine = new AviatorEngine();
		String exp =
				"\"[foo i=\"+ foo.i + \", f=\" + foo.f + \", date.year=\" + (foo.date.year+1900) + \", date.month=\" + foo.date.month + \", bars[0].name=\" + #foo.bars[0].name + \"]\"";
		String result = (String) engine.eval(exp, Dict.of().set("foo", foo));
		Assertions.assertEquals("[foo i=100, f=3.14, date.year=2020, date.month=10, bars[0].name=bar]", result);

		// Assignment.
		exp = "#foo.bars[0].name='hello aviator' ; #foo.bars[0].name";
		result = (String) engine.eval(exp, Dict.of().set("foo", foo));
		Assertions.assertEquals("hello aviator", result);
		Assertions.assertEquals("hello aviator", foo.bars[0].getName());

		exp = "foo.bars[0] = nil ; foo.bars[0]";
		result = (String) engine.eval(exp, Dict.of().set("foo", foo));
		Console.log("Execute expression: " + exp);
		Assertions.assertNull(result);
		Assertions.assertNull(foo.bars[0]);
	}

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
		Date date;
		Bar[] bars = new Bar[1];

		public Foo(final int i, final float f, final Date date) {
			this.i = i;
			this.f = f;
			this.date = date;
			this.bars[0] = new Bar();
		}
	}
}
