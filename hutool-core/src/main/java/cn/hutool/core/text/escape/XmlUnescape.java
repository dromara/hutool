package cn.hutool.core.text.escape;

import cn.hutool.core.text.replacer.LookupReplacer;
import cn.hutool.core.text.replacer.ReplacerChain;

/**
 * XML的UNESCAPE
 *
 * @author looly
 * @since 5.7.2
 */
public class XmlUnescape extends ReplacerChain {
	private static final long serialVersionUID = 1L;

	protected static final String[][] BASIC_UNESCAPE  = InternalEscapeUtil.invert(XmlEscape.BASIC_ESCAPE);

	public XmlUnescape() {
		addChain(new LookupReplacer(BASIC_UNESCAPE));
		addChain(new NumericEntityUnescaper());
	}
}
