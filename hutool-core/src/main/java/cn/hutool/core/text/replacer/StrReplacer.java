package cn.hutool.core.text.replacer;

import cn.hutool.core.lang.Replacer;
import cn.hutool.core.text.StrBuilder;

import java.io.Serializable;

/**
 * 抽象字符串替换类<br>
 * 通过实现replace方法实现局部替换逻辑
 *
 * @author looly
 * @since 4.1.5
 */
public abstract class StrReplacer implements Replacer<CharSequence>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 抽象的字符串替换方法，通过传入原字符串和当前位置，执行替换逻辑，返回处理或替换的字符串长度部分。
	 *
	 * @param str 被处理的字符串
	 * @param pos 当前位置
	 * @param out 输出
	 * @return 处理的原字符串长度，0表示跳过此字符
	 */
	protected abstract int replace(CharSequence str, int pos, StrBuilder out);

	@Override
	public CharSequence replace(CharSequence t) {
		final int len = t.length();
		final StrBuilder builder = StrBuilder.create(len);
		int pos = 0;//当前位置
		int consumed;//处理过的字符数
		while (pos < len) {
			consumed = replace(t, pos, builder);
			if (0 == consumed) {
				//0表示未处理或替换任何字符，原样输出本字符并从下一个字符继续
				builder.append(t.charAt(pos));
				pos++;
			}
			pos += consumed;
		}
		return builder;
	}
}
