package cn.hutool.extra.tokenizer.engine;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Engine;
import cn.hutool.extra.tokenizer.TokenizerException;
import cn.hutool.extra.tokenizer.engine.ansj.AnsjEngine;
import cn.hutool.extra.tokenizer.engine.hanlp.HanLPEngine;
import cn.hutool.extra.tokenizer.engine.ikanalyzer.IKAnalyzerEngine;
import cn.hutool.extra.tokenizer.engine.jcseg.JcsegEngine;
import cn.hutool.extra.tokenizer.engine.jieba.JiebaEngine;
import cn.hutool.extra.tokenizer.engine.mmseg.MmsegEngine;
import cn.hutool.log.StaticLog;

/**
 * 简单分词引擎工厂，用于根据用户引入的分词引擎jar，自动创建对应的引擎
 * 
 * @author looly
 *
 */
public class EngineFactory {
	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 * 
	 * @return {@link Engine}
	 */
	public static Engine create() {
		final Engine engine = doCreate();
		StaticLog.debug("Use [{}] Tokenizer Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的分词引擎jar，自动创建对应的分词引擎对象
	 * 
	 * @return {@link Engine}
	 */
	private static Engine doCreate() {
		try {
			return new AnsjEngine();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new HanLPEngine();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new IKAnalyzerEngine();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new JcsegEngine();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new JiebaEngine();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new MmsegEngine();
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		throw new TokenizerException("No template found ! Please add one of [Beetl,Freemarker,Velocity,Rythm] jar to your project !");
	}
}
