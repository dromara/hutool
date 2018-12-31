package cn.hutool.extra.tokenizer.engine.ikanalyzer;

import org.wltea.analyzer.core.IKSegmenter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Engine;
import cn.hutool.extra.tokenizer.Result;

/**
 * IKAnalyzer分词引擎实现<br>
 * 项目地址：https://github.com/yozhao/IKAnalyzer
 * 
 * @author looly
 *
 */
public class IKAnalyzerEngine implements Engine {

	private IKSegmenter seg;
	
	/**
	 * 构造
	 * 
	 */
	public IKAnalyzerEngine() {
		this.seg = new IKSegmenter(null, true);
	}

	@Override
	public Result parse(CharSequence text) {
		this.seg.reset(StrUtil.getReader(text));
		return new IKAnalyzerResult(this.seg);
	}

}
