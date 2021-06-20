package cn.hutool.extra.pinyin;

import cn.hutool.extra.pinyin.engine.bopomofo4j.Bopomofo4jEngine;
import org.junit.Assert;
import org.junit.Test;

public class Bopomofo4jTest {

	final Bopomofo4jEngine engine = new Bopomofo4jEngine();

	@Test
	public void getFirstLetterByBopomofo4jTest(){
		final String result = engine.getFirstLetter("林海", "");
		Assert.assertEquals("lh", result);
	}

	@Test
	public void getPinyinByBopomofo4jTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		Assert.assertEquals("ni haoh", pinyin);
	}

}
