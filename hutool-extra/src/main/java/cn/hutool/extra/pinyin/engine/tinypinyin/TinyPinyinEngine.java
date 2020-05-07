package cn.hutool.extra.pinyin.engine.tinypinyin;

import cn.hutool.extra.pinyin.PinyinEngine;
import com.github.promeg.pinyinhelper.Pinyin;

/**
 * 封装了TinyPinyin的引擎。
 *
 * <p>
 * TinyPinyin(https://github.com/promeG/TinyPinyin)提供者未提交Maven中央库，<br>
 * 因此使用
 * https://github.com/biezhi/TinyPinyin打包的版本
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;io.github.biezhi&lt;/groupId&gt;
 *     &lt;artifactId&gt;TinyPinyin&lt;/artifactId&gt;
 *     &lt;version&gt;2.0.3.RELEASE&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 */
public class TinyPinyinEngine implements PinyinEngine {

	/**
	 * 构造
	 */
	public TinyPinyinEngine(){
	}

	/**
	 * 构造
	 * @param config 配置
	 */
	public TinyPinyinEngine(Pinyin.Config config){
		Pinyin.init(config);
	}

	@Override
	public String getPinyin(char c) {
		if(false == Pinyin.isChinese(c)){
			return String.valueOf(c);
		}
		return Pinyin.toPinyin(c).toLowerCase();
	}

	@Override
	public String getPinyin(String str, String separator) {
		return Pinyin.toPinyin(str, separator).toLowerCase();
	}

}
