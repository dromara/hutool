package cn.hutool.extra.tokenizer.engine.ansj;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.Word;
import org.ansj.domain.Term;

import java.util.Iterator;

/**
 * Ansj分词结果实现<br>
 * 项目地址：https://github.com/NLPchina/ansj_seg
 *
 * @author looly
 */
public class AnsjResult implements Result {

	private final Iterator<Term> result;

	/**
	 * 构造
	 *
	 * @param ansjResult 分词结果
	 */
	public AnsjResult(org.ansj.domain.Result ansjResult) {
		this.result = ansjResult.iterator();
	}

	@Override
	public boolean hasNext() {
		return result.hasNext();
	}

	@Override
	public Word next() {
		return new AnsjWord(result.next());
	}

	@Override
	public void remove() {
		result.remove();
	}

	@Override
	public Iterator<Word> iterator() {
		return this;
	}

}
