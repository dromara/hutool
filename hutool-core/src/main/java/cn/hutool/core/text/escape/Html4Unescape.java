package cn.hutool.core.text.escape;

import cn.hutool.core.text.replacer.LookupReplacer;
import cn.hutool.core.text.replacer.ReplacerChain;

/**
 * HTML4的UNESCAPE
 * 
 * @author looly
 *
 */
public class Html4Unescape extends ReplacerChain {
	
	protected static final String[][] BASIC_UNESCAPE  = InternalEscapeUtil.invert(Html4Escape.BASIC_ESCAPE);
	protected static final String[][] ISO8859_1_UNESCAPE  = InternalEscapeUtil.invert(Html4Escape.ISO8859_1_ESCAPE);
	protected static final String[][] HTML40_EXTENDED_UNESCAPE  = InternalEscapeUtil.invert(Html4Escape.HTML40_EXTENDED_ESCAPE);

	public Html4Unescape() {
		addChain(new LookupReplacer(BASIC_UNESCAPE));
		addChain(new LookupReplacer(ISO8859_1_UNESCAPE));
		addChain(new LookupReplacer(HTML40_EXTENDED_UNESCAPE));
		addChain(new NumericEntityUnescaper());
	}
}
