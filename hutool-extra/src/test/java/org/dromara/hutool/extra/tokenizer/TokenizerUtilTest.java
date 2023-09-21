/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.tokenizer;

import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.extra.tokenizer.engine.TokenizerEngine;
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
		final TokenizerEngine engine = TokenizerUtil.createEngine("hanlp");
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两 个 方法 的 区别 在于 返回 值", resultStr);
	}

	@Test
	public void ikAnalyzerTest() {
		final TokenizerEngine engine = TokenizerUtil.createEngine("IKAnalyzer");
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这两个 方法 的 区别 在于 返回值", resultStr);
	}

	@Test
	public void jcsegTest() {
		final TokenizerEngine engine = TokenizerUtil.createEngine("Jcseg");
		final Result result = engine.parse(text);
		checkResult(result);
	}

	@Test
	public void jiebaTest() {
		final TokenizerEngine engine = TokenizerUtil.createEngine("Jieba");
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两个 方法 的 区别 在于 返回值", resultStr);
	}

	@Test
	public void mmsegTest() {
		final TokenizerEngine engine = TokenizerUtil.createEngine("Mmseg");
		final Result result = engine.parse(text);
		checkResult(result);
	}

	@Test
	public void smartcnTest() {
		final TokenizerEngine engine = TokenizerUtil.createEngine("Smartcn");
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两 个 方法 的 区别 在于 返回 值", resultStr);
	}

	@Test
	public void wordTest() {
		final TokenizerEngine engine = TokenizerUtil.createEngine("Word");
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这两个 方法 的 区别 在于 返回值", resultStr);
	}

	@Test
	public void mynlpTest() {
		final TokenizerEngine engine = TokenizerUtil.createEngine("Mynlp");
		final Result result = engine.parse(text);
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两个 方法 的 区别 在于 返回 值", resultStr);
	}

	private void checkResult(final Result result) {
		final String resultStr = IterUtil.join(result, " ");
		Assertions.assertEquals("这 两个 方法 的 区别 在于 返回 值", resultStr);
	}
}
