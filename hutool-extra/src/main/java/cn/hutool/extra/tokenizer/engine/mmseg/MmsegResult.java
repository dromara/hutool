package cn.hutool.extra.tokenizer.engine.mmseg;

import cn.hutool.extra.tokenizer.AbstractResult;
import cn.hutool.extra.tokenizer.TokenizerException;
import cn.hutool.extra.tokenizer.Word;
import com.chenlb.mmseg4j.MMSeg;

import java.io.IOException;

/**
 * mmseg4j分词结果实现<br>
 * 项目地址：https://github.com/chenlb/mmseg4j-core
 * 
 * @author looly
 *
 */
public class MmsegResult extends AbstractResult {

	private final MMSeg mmSeg;

	/**
	 * 构造
	 * 
	 * @param mmSeg 分词结果
	 */
	public MmsegResult(MMSeg mmSeg) {
		this.mmSeg = mmSeg;
	}

	@Override
	protected Word nextWord() {
		com.chenlb.mmseg4j.Word next;
		try {
			next = this.mmSeg.next();
		} catch (IOException e) {
			throw new TokenizerException(e);
		}
		if (null != next) {
			return new MmsegWord(next);
		}
		return null;
	}
}
