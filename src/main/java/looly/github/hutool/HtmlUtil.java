package looly.github.hutool;

/**
 * HTML工具类
 * 
 * @author xiaoleilu
 * 
 */
public class HtmlUtil {

	public static final String RE_HTML_MARK = "(<.*?>)|(<[\\s]*?/.*?>)|(<.*?/[\\s]*?>)";
	public static final String RE_SCRIPT = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

	private static final char[][] TEXT = new char[64][];

	static {
		for (int i = 0; i < 64; i++) {
			TEXT[i] = new char[] { (char) i };
		}

		// special HTML characters
		TEXT['\''] = "&#039;".toCharArray(); // 单引号 ('&apos;' doesn't work - it is not by the w3 specs)
		TEXT['"'] = StrUtil.HTML_QUOTE.toCharArray(); // 双引号
		TEXT['&'] = StrUtil.HTML_AMP.toCharArray(); // &符
		TEXT['<'] = StrUtil.HTML_LT.toCharArray(); // 小于号
		TEXT['>'] = StrUtil.HTML_GT.toCharArray(); // 大于号
	}

	/**
	 * 还原被转义的HTML特殊字符
	 * 
	 * @param htmlStr 包含转义符的HTML内容
	 * @return 转换后的字符串
	 */
	public static String restoreEscaped(String htmlStr) {
		if (StrUtil.isBlank(htmlStr)) {
			return htmlStr;
		}
		return htmlStr
				.replace("&#39;", "'")
				.replace(StrUtil.HTML_LT, "<")
				.replace(StrUtil.HTML_GT, ">")
				.replace(StrUtil.HTML_AMP, "&")
				.replace(StrUtil.HTML_QUOTE, "\"")
				.replace(StrUtil.HTML_NBSP, " ");
	}

	// ---------------------------------------------------------------- encode text

	/**
	 * 转义文本中的HTML字符为安全的字符，以下字符被转义：
	 * <ul>
	 * <li>' with &amp;#039; (&amp;apos; doesn't work in HTML4)</li>
	 * <li>" with &amp;quot;</li>
	 * <li>&amp; with &amp;amp;</li>
	 * <li>&lt; with &amp;lt;</li>
	 * <li>&gt; with &amp;gt;</li>
	 * </ul>
	 * 
	 * @param text 被转义的文本
	 * @return 转义后的文本
	 */
	public static String encode(String text) {
		return encode(text, TEXT);
	}

	/**
	 * 清除所有HTML标签
	 * 
	 * @param content 文本
	 * @return 清除标签后的文本
	 */
	public static String cleanHtmlTag(String content) {
		return content.replaceAll(RE_HTML_MARK, "");
	}
	
	/**
	 * 清除指定HTML标签和被标签包围的内容<br>
	 * 不区分大小写
	 * @param content 文本
	 * @param tagNames 要清除的标签
	 * @return
	 */
	public static String removeHtmlTag(String content, String... tagNames) {
		String regex1 = null;
		String regex2 = null;
		for (String tagName : tagNames) {
			if(StrUtil.isBlank(tagName)) {
				continue;
			}
			tagName = tagName.trim();
			//(?i)表示其后面的表达式忽略大小写
			regex1 = StrUtil.format("(?i)<{}\\s?.*?/>", tagName);	
			regex2 = StrUtil.format("(?i)<{}\\s?.*?>.*?</{}>", tagName, tagName);
			
			content = content
					.replaceAll(regex1, StrUtil.EMPTY)									//自闭标签小写
//					.replaceAll(regex1.toUpperCase(), StrUtil.EMPTY)			//自闭标签大写
					.replaceAll(regex2, StrUtil.EMPTY);									//非自闭标签小写
//					.replaceAll(regex2.toUpperCase(), StrUtil.EMPTY);			//非自闭标签大写
		}
		return content;
	}

	/**
	 * Encoder.
	 */
	private static String encode(String text, char[][] array) {
		int len;
		if ((text == null) || ((len = text.length()) == 0)) {
			return StrUtil.EMPTY;
		}
		StringBuilder buffer = new StringBuilder(len + (len >> 2));
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (c < 64) {
				buffer.append(array[c]);
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}
	
}
