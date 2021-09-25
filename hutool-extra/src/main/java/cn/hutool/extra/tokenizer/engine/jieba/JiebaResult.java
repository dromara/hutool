package cn.hutool.extra.tokenizer.engine.jieba;

import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.Word;
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
	public JiebaResult(List<SegToken> segTokenList) {
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
