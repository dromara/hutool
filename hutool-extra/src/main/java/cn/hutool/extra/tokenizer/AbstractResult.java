package cn.hutool.extra.tokenizer;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 对于未实现{@link Iterator}接口的普通结果类，装饰为{@link Result}<br>
 * 普通的结果类只需实现{@link #nextWord()} 即可
 * 
 * @author looly
 *
 */
public abstract class AbstractResult implements Result{
	
	private Word cachedWord;
	
	@Override
	public boolean hasNext() {
		if (this.cachedWord != null) {
			return true;
		}

		final Word next = nextWord();
		if(null != next) {
			this.cachedWord = next;
			return true;
		}
		return false;
	}
	
	/**
	 * 下一个单词，通过实现此方法获取下一个单词，null表示无下一个结果。
	 * @return 下一个单词或null
	 */
	protected abstract Word nextWord();
	
	@Override
	public Word next() {
		if (false == hasNext()) {
			throw new NoSuchElementException("No more word !");
		}
		final Word currentWord = this.cachedWord;
		this.cachedWord = null;
		return currentWord;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Jcseg result not allow to remove !");
	}

	@Override
	public Iterator<Word> iterator() {
		return this;
	}
}
