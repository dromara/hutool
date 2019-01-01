package cn.hutool.extra.tokenizer.engine.jcseg;

import java.io.IOException;
import java.io.StringReader;

import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.ISegment;
import org.lionsoul.jcseg.tokenizer.core.JcsegException;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.lionsoul.jcseg.tokenizer.core.SegmentFactory;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerException;

/**
 * Jcseg分词引擎实现<br>
 * 项目地址：https://gitee.com/lionsoul/jcseg
 * 
 * @author looly
 *
 */
public class JcsegEngine implements TokenizerEngine {

	private ISegment segment;

	/**
	 * 构造
	 */
	public JcsegEngine() {
		// 创建JcsegTaskConfig分词配置实例，自动查找加载jcseg.properties配置项来初始化
		JcsegTaskConfig config = new JcsegTaskConfig(true);
		// 创建默认单例词库实现，并且按照config配置加载词库
		ADictionary dic = DictionaryFactory.createSingletonDictionary(config);

		// 依据给定的ADictionary和JcsegTaskConfig来创建ISegment
		try {
			this.segment = SegmentFactory.createJcseg(//
					JcsegTaskConfig.COMPLEX_MODE, //
					new Object[] { config, dic });
		} catch (JcsegException e) {
			throw new TokenizerException(e);
		}
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
