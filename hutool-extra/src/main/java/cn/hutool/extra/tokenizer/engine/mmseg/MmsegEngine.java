package cn.hutool.extra.tokenizer.engine.mmseg;

import java.io.StringReader;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.Result;

/**
 * mmseg4j分词引擎实现<br>
 * 项目地址：https://github.com/chenlb/mmseg4j-core
 * 
 * @author looly
 *
 */
public class MmsegEngine implements TokenizerEngine {

	private MMSeg mmSeg;
	
	/**
	 * 构造
	 * 
	 * @param mode 模式{@link SegMode}
	 */
	public MmsegEngine() {
		final Dictionary dict = Dictionary.getInstance();
		final ComplexSeg seg = new ComplexSeg(dict);
		this.mmSeg = new MMSeg(new StringReader(""), seg);
	}
	
	/**
	 * 构造
	 * 
	 * @param mmSeg 模式{@link MMSeg}
	 */
	public MmsegEngine(MMSeg mmSeg) {
		this.mmSeg = mmSeg;
	}

	@Override
	public Result parse(CharSequence text) {
		this.mmSeg.reset(StrUtil.getReader(text));
		return new MmsegResult(this.mmSeg);
	}

}
