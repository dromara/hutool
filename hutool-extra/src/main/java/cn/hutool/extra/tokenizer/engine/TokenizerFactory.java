package cn.hutool.extra.tokenizer.engine;

import cn.hutool.core.util.ServiceLoaderUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerException;
import cn.hutool.log.StaticLog;

/**
 * 简单分词引擎工厂，用于根据用户引入的分词引擎jar，自动创建对应的引擎
 * 
 * @author looly
 *
 */
public class TokenizerFactory {
	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 * 
	 * @return {@link TokenizerEngine}
	 */
	public static TokenizerEngine create() {
		final TokenizerEngine engine = doCreate();
		StaticLog.debug("Use [{}] Tokenizer Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 * 
	 * @return {@link TokenizerEngine}
	 */
	private static TokenizerEngine doCreate() {
		final TokenizerEngine engine = ServiceLoaderUtil.loadFirstAvailable(TokenizerEngine.class);
		if(null != engine){
			return engine;
		}

		throw new TokenizerException("No tokenizer found ! Please add some tokenizer jar to your project !");
	}
}
