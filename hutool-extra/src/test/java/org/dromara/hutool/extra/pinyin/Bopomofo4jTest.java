package org.dromara.hutool.extra.pinyin;

import org.dromara.hutool.extra.pinyin.engine.bopomofo4j.Bopomofo4jEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Bopomofo4jTest {

	final Bopomofo4jEngine engine = new Bopomofo4jEngine();

	@Test
	public void getFirstLetterByBopomofo4jTest(){
		final String result = engine.getFirstLetter("林海", "");
		Assertions.assertEquals("lh", result);
	}

	@Test
	public void getPinyinByBopomofo4jTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		Assertions.assertEquals("ni haoh", pinyin);
	}

}
