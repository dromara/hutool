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

package org.dromara.hutool.extra.tokenizer.engine.hanlp;

import org.dromara.hutool.extra.tokenizer.Result;
import org.dromara.hutool.extra.tokenizer.Word;
import com.hankcs.hanlp.seg.common.Term;

import java.util.Iterator;
import java.util.List;

/**
 * HanLP分词结果实现<br>
 * 项目地址：https://github.com/hankcs/HanLP
 *
 * @author looly
 *
 */
public class HanLPResult implements Result {

	Iterator<Term> result;

	public HanLPResult(final List<Term> termList) {
		this.result = termList.iterator();
	}

	@Override
	public boolean hasNext() {
		return result.hasNext();
	}

	@Override
	public Word next() {
		return new HanLPWord(result.next());
	}

	@Override
	public void remove() {
		result.remove();
	}
}
