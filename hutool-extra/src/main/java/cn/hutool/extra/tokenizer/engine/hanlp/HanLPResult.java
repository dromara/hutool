package cn.hutool.extra.tokenizer.engine.hanlp;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.Word;
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

	public HanLPResult(List<Term> termList) {
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
