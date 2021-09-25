package cn.hutool.extra.tokenizer;

import cn.hutool.core.collection.ComputeIter;

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
