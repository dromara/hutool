package cn.hutool.extra.expression;

import cn.hutool.core.lang.Dict;
import cn.hutool.extra.expression.engine.jexl.JexlEngine;
import cn.hutool.extra.expression.engine.jfireel.JfireELEngine;
import cn.hutool.extra.expression.engine.mvel.MvelEngine;
import cn.hutool.extra.expression.engine.rhino.RhinoEngine;
import cn.hutool.extra.expression.engine.spel.SpELEngine;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ExpressionUtilTest {

	@Test
	public void evalTest(){
		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = ExpressionUtil.eval("a-(b-c)", dict);
		Assert.assertEquals(-143.8, (double)eval, 2);
	}

	@Test
	public void jexlTest(){
		ExpressionEngine engine = new JexlEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict);
		Assert.assertEquals(-143.8, (double)eval, 2);
	}

	@Test
	public void jexlScriptTest(){
		ExpressionEngine engine = new JexlEngine();

		String exps2="if(a>0){return 100;}";
		Map<String,Object> map2=new HashMap<>();
		map2.put("a", 1);
		Object eval1 = engine.eval(exps2, map2);
		Assert.assertEquals(100, eval1);
	}

	@Test
	public void mvelTest(){
		ExpressionEngine engine = new MvelEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict);
		Assert.assertEquals(-143.8, (double)eval, 2);
	}

	@Test
	public void jfireELTest(){
		ExpressionEngine engine = new JfireELEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict);
		Assert.assertEquals(-143.8, (double)eval, 2);
	}

	@Test
	public void spELTest(){
		ExpressionEngine engine = new SpELEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("#a-(#b-#c)", dict);
		Assert.assertEquals(-143.8, (double)eval, 2);
	}

	@Test
	public void rhinoTest(){
		ExpressionEngine engine = new RhinoEngine();

		final Dict dict = Dict.create()
				.set("a", 100.3)
				.set("b", 45)
				.set("c", -199.100);
		final Object eval = engine.eval("a-(b-c)", dict);
		Assert.assertEquals(-143.8, (double)eval, 2);
	}

}
