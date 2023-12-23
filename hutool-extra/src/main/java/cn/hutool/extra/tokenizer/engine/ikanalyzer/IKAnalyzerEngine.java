package cn.hutool.extra.tokenizer.engine.ikanalyzer;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import org.wltea.analyzer.core.IKSegmenter;

/**
 * IKAnalyzer分词引擎实现<br>
 * 项目地址：https://github.com/yozhao/IKAnalyzer
 *
 * @author looly
 */
public class IKAnalyzerEngine implements TokenizerEngine {

	/**
	 * 构造
	 */
	public IKAnalyzerEngine() {
	}

	/**
	 * 构造
	 *
	 * @param seg {@link IKSegmenter}
	 * @deprecated 并发问题，导致无法共用IKSegmenter，因此废弃
	 */
	@Deprecated
	public IKAnalyzerEngine(IKSegmenter seg) {
	}

	@Override
	public Result parse(CharSequence text) {
		final IKSegmenter copySeg = new IKSegmenter(null, true);
		copySeg.reset(StrUtil.getReader(text));
		return new IKAnalyzerResult(copySeg);
	}
}
