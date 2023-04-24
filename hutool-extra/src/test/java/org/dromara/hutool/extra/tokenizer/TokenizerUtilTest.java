package org.dromara.hutool.extra.tokenizer;

import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
import org.dromara.hutool.extra.tokenizer.engine.analysis.SmartcnEngine;
import org.dromara.hutool.extra.tokenizer.engine.hanlp.HanLPEngine;
import org.dromara.hutool.extra.tokenizer.engine.ikanalyzer.IKAnalyzerEngine;
import org.dromara.hutool.extra.tokenizer.engine.jcseg.JcsegEngine;
import org.dromara.hutool.extra.tokenizer.engine.jieba.JiebaEngine;
import org.dromara.hutool.extra.tokenizer.engine.mmseg.MmsegEngine;
import org.dromara.hutool.extra.tokenizer.engine.mynlp.MynlpEngine;
import org.dromara.hutool.extra.tokenizer.engine.word.WordEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 模板引擎单元测试
 *
 * @author looly
 *
 */
public class TokenizerUtilTest {

	String text = "这两个方法的区别在于返回值";

	@Test
	void parseTest() {
		final Result result = TokenizerUtil.parse(text);
		checkResult(result);
	}

	@Test
	public void createEngineTest() {
		// 默认分词引擎，此处为Ansj
		final TokenizerEngine engine = TokenizerUtil.getEngine();
		final Result result = engine.parse(text);
		checkResult(result);
	}

	@Test
	public void hanlpTest() {
		final TokenizerEngine engine = new HanLPEngine();
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两 个 方法 的 区别 在于 返回 值", resultStr);
	}

	@Test
	public void ikAnalyzerTest() {
		final TokenizerEngine engine = new IKAnalyzerEngine();
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这两个 方法 的 区别 在于 返回值", resultStr);
	}

	@Test
	public void jcsegTest() {
		final TokenizerEngine engine = new JcsegEngine();
		final Result result = engine.parse(text);
		checkResult(result);
	}

	@Test
	public void jiebaTest() {
		final TokenizerEngine engine = new JiebaEngine();
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两个 方法 的 区别 在于 返回值", resultStr);
	}

	@Test
	public void mmsegTest() {
		final TokenizerEngine engine = new MmsegEngine();
		final Result result = engine.parse(text);
		checkResult(result);
	}

	@Test
	public void smartcnTest() {
		final TokenizerEngine engine = new SmartcnEngine();
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两 个 方法 的 区别 在于 返回 值", resultStr);
	}

	@Test
	public void wordTest() {
		final TokenizerEngine engine = new WordEngine();
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这两个 方法 的 区别 在于 返回值", resultStr);
	}

	@Test
	public void mynlpTest() {
		final TokenizerEngine engine = new MynlpEngine();
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两个 方法 的 区别 在于 返回 值", resultStr);
	}

	private void checkResult(final Result result) {
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两个 方法 的 区别 在于 返回 值", resultStr);
	}
}
