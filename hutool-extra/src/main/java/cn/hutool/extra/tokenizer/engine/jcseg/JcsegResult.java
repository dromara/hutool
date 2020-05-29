package cn.hutool.extra.tokenizer.engine.jcseg;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerException;
import cn.hutool.extra.tokenizer.Word;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.IWord;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Jcseg分词结果包装<br>
 * 项目地址：https://gitee.com/lionsoul/jcseg
 * 
 * @author looly
 *
 */
public class JcsegResult implements Result{
	
	private final ISegment result;
	private Word cachedWord;
	
	/**
	 * 构造
	 * @param segment 分词结果
	 */
	public JcsegResult(ISegment segment) {
		this.result = segment;
	}

	@Override
	public boolean hasNext() {
		if (this.cachedWord != null) {
			return true;
		}
		IWord next;
		try {
			next = this.result.next();
		} catch (IOException e) {
			throw new TokenizerException(e); 
		}
		if(null != next) {
			this.cachedWord = new JcsegWord(next);
			return true;
		}
		return false;
	}

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
