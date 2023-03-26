/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.extra.tokenizer;

import cn.hutool.core.collection.iter.ComputeIter;

import java.util.Iterator;

/**
 * 对于未实现{@link Iterator}接口的普通结果类，装饰为{@link Result}<br>
 * 普通的结果类只需实现{@link #nextWord()} 即可
 *
 * @author looly
 *
 */
public abstract class AbstractResult extends ComputeIter<Word> implements Result{

	/**
	 * 下一个单词，通过实现此方法获取下一个单词，null表示无下一个结果。
	 * @return 下一个单词或null
	 */
	protected abstract Word nextWord();

	@Override
	protected Word computeNext() {
		return nextWord();
	}
}
