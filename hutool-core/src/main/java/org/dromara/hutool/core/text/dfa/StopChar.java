/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.text.dfa;

import org.dromara.hutool.core.collection.set.SetUtil;

import java.util.Set;

/**
 * 过滤词及一些简单处理
 *
 * @author Looly
 */
public class StopChar {
	/** 不需要处理的词，如标点符号、空格等 */
	public static final Set<Character> STOP_WORD = SetUtil.of(' ', '\'', '、', '。', //
			'·', 'ˉ', 'ˇ', '々', '—', '～', '‖', '…', '‘', '’', '“', '”', '〔', '〕', '〈', '〉', '《', '》', '「', '」', '『', //
			'』', '〖', '〗', '【', '】', '±', '＋', '－', '×', '÷', '∧', '∨', '∑', '∏', '∪', '∩', '∈', '√', '⊥', '⊙', '∫', //
			'∮', '≡', '≌', '≈', '∽', '∝', '≠', '≮', '≯', '≤', '≥', '∞', '∶', '∵', '∴', '∷', '♂', '♀', '°', '′', '〃', //
			'℃', '＄', '¤', '￠', '￡', '‰', '§', '☆', '★', '〇', '○', '●', '◎', '◇', '◆', '□', '■', '△', '▽', '⊿', '▲', //
			'▼', '◣', '◤', '◢', '◥', '▁', '▂', '▃', '▄', '▅', '▆', '▇', '█', '▉', '▊', '▋', '▌', '▍', '▎', '▏', '▓', //
			'※', '→', '←', '↑', '↓', '↖', '↗', '↘', '↙', '〓', 'ⅰ', 'ⅱ', 'ⅲ', 'ⅳ', 'ⅴ', 'ⅵ', 'ⅶ', 'ⅷ', 'ⅸ', 'ⅹ', '①', //
			'②', '③', '④', '⑤', '⑥', '⑦', '⑧', '⑨', '⑩', '⒈', '⒉', '⒊', '⒋', '⒌', '⒍', '⒎', '⒏', '⒐', '⒑', '⒒', '⒓', //
			'⒔', '⒕', '⒖', '⒗', '⒘', '⒙', '⒚', '⒛', '⑴', '⑵', '⑶', '⑷', '⑸', '⑹', '⑺', '⑻', '⑼', '⑽', '⑾', '⑿', '⒀', //
			'⒁', '⒂', '⒃', '⒄', '⒅', '⒆', '⒇', 'Ⅰ', 'Ⅱ', 'Ⅲ', 'Ⅳ', 'Ⅴ', 'Ⅵ', 'Ⅶ', 'Ⅷ', 'Ⅸ', 'Ⅹ', 'Ⅺ', 'Ⅻ', '！', '”', //
			'＃', '￥', '％', '＆', '’', '（', '）', '＊', '＋', '，', '－', '．', '／', '０', '１', '２', '３', '４', '５', '６', '７', //
			'８', '９', '：', '；', '＜', '＝', '＞', '？', '＠', '〔', '＼', '〕', '＾', '＿', '‘', '｛', '｜', '｝', '∏', 'Ρ', '∑', //
			'Υ', 'Φ', 'Χ', 'Ψ', 'Ω', 'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 'θ', 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'π', //
			'ρ', 'σ', 'τ', 'υ', 'φ', 'χ', 'ψ', 'ω', '（', '）', '〔', '〕', '＾', '﹊', '﹍', '╭', '╮', '╰', '╯', '', '_', //
			'', '^', '（', '^', '：', '！', '/', '\\', '\"', '<', '>', '`', '·', '。', '{', '}', '~', '～', '(', ')', '-', //
			'√', '$', '@', '*', '&', '#', '卐', '㎎', '㎏', '㎜', '㎝', '㎞', '㎡', '㏄', '㏎', '㏑', '㏒', '㏕', '+', '=', '?',
	                ':', '.', '!', ';', ']','|','%');

	/**
	 * 判断指定的词是否是不处理的词。 如果参数为空，则返回true，因为空也属于不处理的字符。
	 *
	 * @param ch 指定的词
	 * @return 是否是不处理的词
	 */
	public static boolean isStopChar(final char ch) {
		return Character.isWhitespace(ch) || STOP_WORD.contains(ch);
	}

	/**
	 * 是否为合法字符（待处理字符）
	 *
	 * @param ch 指定的词
	 * @return 是否为合法字符（待处理字符）
	 */
	public static boolean isNotStopChar(final char ch) {
		return !isStopChar(ch);
	}
}
