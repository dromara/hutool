package cn.hutool.extra.tokenizer.engine.jcseg;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerException;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.dic.ADictionary;
import org.lionsoul.jcseg.dic.DictionaryFactory;
import org.lionsoul.jcseg.segmenter.SegmenterConfig;

import java.io.IOException;
import java.io.StringReader;

/**
 * Jcseg分词引擎实现<br>
 * 项目地址：https://gitee.com/lionsoul/jcseg
 * 
 * @author looly
 *
 */
public class JcsegEngine implements TokenizerEngine {

	private final ISegment segment;

	/**
	 * 构造
	 */
	public JcsegEngine() {
		// 创建SegmenterConfig分词配置实例，自动查找加载jcseg.properties配置项来初始化
		final SegmenterConfig config = new SegmenterConfig(true);
		// 创建默认单例词库实现，并且按照config配置加载词库
		final ADictionary dic = DictionaryFactory.createSingletonDictionary(config);

		// 依据给定的ADictionary和SegmenterConfig来创建ISegment
		this.segment = ISegment.COMPLEX.factory.create(config, dic);
	}

	/**
	 * 构造
	 * 
	 * @param segment {@link ISegment}
	 */
	public JcsegEngine(ISegment segment) {
		this.segment = segment;
	}

	@Override
	public Result parse(CharSequence text) {
		try {
			this.segment.reset(new StringReader(StrUtil.str(text)));
		} catch (IOException e) {
			throw new TokenizerException(e);
		}
		return new JcsegResult(this.segment);
	}

}
