package cn.hutool.extra.tokenizer;

import cn.hutool.extra.tokenizer.engine.TokenizerFactory;

/**
 * 分词工具类
 * 
 * @author looly
 * @since 4.3.3
 */
public class TokenizerUtil {

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 * 
	 * @return {@link TokenizerEngine}
	 */
	public static TokenizerEngine createEngine() {
		return TokenizerFactory.create();
	}
}
