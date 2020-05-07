package cn.hutool.extra.pinyin.engine.jpinyin;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.pinyin.PinyinEngine;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 封装了Jpinyin的引擎。
 *
 * <p>
 * jpinyin（github库作者已删除）封装。
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.github.stuxuhai&lt;/groupId&gt;
 *     &lt;artifactId&gt;jpinyin&lt;/artifactId&gt;
 *     &lt;version&gt;1.1.8&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 */
public class JPinyinEngine implements PinyinEngine {

	//设置汉子拼音输出的格式
	PinyinFormat format;

	public JPinyinEngine(){
		this(null);
	}

	public JPinyinEngine(PinyinFormat format){
		init(format);
	}

	public void init(PinyinFormat format){
		if(null == format){
			// 不加声调
			format = PinyinFormat.WITHOUT_TONE;
		}
		this.format = format;
	}


	@Override
	public String getPinyin(char c) {
		String[] results = PinyinHelper.convertToPinyinArray(c, format);
		return ArrayUtil.isEmpty(results) ? String.valueOf(c) : results[0];
	}

	@Override
	public String getPinyin(String str, String separator) {
		try {
			return PinyinHelper.convertToPinyinString(str, separator, format);
		} catch (PinyinException e) {
			throw new cn.hutool.extra.pinyin.PinyinException(e);
		}
	}
}
