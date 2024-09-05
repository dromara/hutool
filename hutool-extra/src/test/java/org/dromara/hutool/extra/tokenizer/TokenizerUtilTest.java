/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
