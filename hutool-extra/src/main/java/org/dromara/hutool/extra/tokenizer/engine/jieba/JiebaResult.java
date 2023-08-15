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

package org.dromara.hutool.extra.tokenizer.engine.jieba;

import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.Word;
import com.huaban.analysis.jieba.SegToken;

import java.util.Iterator;
import java.util.List;

/**
 * Jieba分词结果实现<br>
 * 项目地址：https://github.com/huaban/jieba-analysis
 *
 * @author looly
 *
 */
public class JiebaResult implements Result{

	Iterator<SegToken> result;

	/**
	 * 构造
	 * @param segTokenList 分词结果
	 */
	public JiebaResult(final List<SegToken> segTokenList) {
		this.result = segTokenList.iterator();
	}

	@Override
	public boolean hasNext() {
		return result.hasNext();
	}

	@Override
	public Word next() {
		return new JiebaWord(result.next());
	}

	@Override
	public void remove() {
		result.remove();
	}
}
