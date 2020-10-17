package cn.hutool.extra.pinyin.engine.bopomofo4j;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinEngine;
import com.rnkrsoft.bopomofo4j.Bopomofo4j;
import com.rnkrsoft.bopomofo4j.ToneType;

/**
 * 封装了Bopomofo4j的引擎。
 *
 * <p>
 * Bopomofo4j封装，项目：https://gitee.com/rnkrsoft/Bopomofo4j。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.rnkrsoft.bopomofo4j&lt;/groupId&gt;
 *     &lt;artifactId&gt;bopomofo4j&lt;/artifactId&gt;
 *     &lt;version&gt;1.0.0&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 * @since 5.4.5
 */
public class Bopomofo4jEngine implements PinyinEngine {

	public Bopomofo4jEngine(){
		Bopomofo4j.local();
	}

	@Override
	public String getPinyin(char c) {
		return Bopomofo4j.pinyin(String.valueOf(c), ToneType.WITHOUT_TONE, false, false, StrUtil.EMPTY);
	}

	@Override
	public String getPinyin(String str, String separator) {
		return Bopomofo4j.pinyin(str, ToneType.WITHOUT_TONE, false, false, separator);
	}
}
