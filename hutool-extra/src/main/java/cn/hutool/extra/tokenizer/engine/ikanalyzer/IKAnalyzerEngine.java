package cn.hutool.extra.tokenizer.engine.ikanalyzer;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;

/**
 * IKAnalyzer分词引擎实现<br>
 * 项目地址：https://github.com/yozhao/IKAnalyzer
 *
 * @author looly
 */
public class IKAnalyzerEngine implements TokenizerEngine {

	private final IKSegmenter seg;

	/**
	 * 构造
	 */
	public IKAnalyzerEngine() {
		this(new IKSegmenter(null, true));
	}

	/**
	 * 构造
	 *
	 * @param seg {@link IKSegmenter}
	 */
	public IKAnalyzerEngine(IKSegmenter seg) {
		this.seg = seg;
	}

	@Override
	public Result parse(CharSequence text) {
		IKSegmenter copySeg = new IKSegmenter(null, (Configuration) ReflectUtil.getFieldValue(this.seg, "cfg"));
		copySeg.reset(StrUtil.getReader(text));
		return new IKAnalyzerResult(copySeg);
	}
}
