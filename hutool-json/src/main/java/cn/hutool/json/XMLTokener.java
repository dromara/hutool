package cn.hutool.json;

/**
 * XML分析器，继承自JSONTokener，提供XML的语法分析
 *
 * @author JSON.org
 */
public class XMLTokener extends JSONTokener {

	/**
	 * The table of entity values.
	 * It initially contains Character values for amp, apos, gt, lt, quot.
	 */
	public static final java.util.HashMap<String, Character> entity;

	static {
		entity = new java.util.HashMap<>(8);
		entity.put("amp", XML.AMP);
		entity.put("apos", XML.APOS);
		entity.put("gt", XML.GT);
		entity.put("lt", XML.LT);
		entity.put("quot", XML.QUOT);
	}

	/**
	 * Construct an XMLTokener from a string.
	 *
	 * @param s      A source string.
	 * @param config JSON配置
	 */
	public XMLTokener(CharSequence s, JSONConfig config) {
		super(s, config);
	}

	/**
	 * Get the text in the CDATA block.
	 *
	 * @return The string up to the {@code ]]>}.
	 * @throws JSONException If the {@code ]]>} is not found.
	 */
	public String nextCDATA() throws JSONException {
		char c;
		int i;
		final StringBuilder sb = new StringBuilder();
		for (; ; ) {
			c = next();
			if (end()) {
				throw syntaxError("Unclosed CDATA");
			}
			sb.append(c);
			i = sb.length() - 3;
			if (i >= 0 && sb.charAt(i) == ']' && sb.charAt(i + 1) == ']' && sb.charAt(i + 2) == '>') {
				sb.setLength(i);
				return sb.toString();
			}
		}
	}

	/**
	 * Get the next XML outer token, trimming whitespace.
	 * There are two kinds of tokens: the '&gt;' character which begins a markup tag, and the content text between markup tags.
	 *
	 * @return A string, or a '&gt;' Character, or null if there is no more source text.
	 * @throws JSONException JSON
	 */
	public Object nextContent() throws JSONException {
		char c;
		final StringBuilder sb;
		do {
			c = next();
		} while (Character.isWhitespace(c));
		if (c == 0) {
			return null;
		}
		if (c == '<') {
			return XML.LT;
		}
		sb = new StringBuilder();
		for (; ; ) {
			if (c == '<' || c == 0) {
				back();
				return sb.toString().trim();
			}
			if (c == '&') {
				sb.append(nextEntity(c));
			} else {
				sb.append(c);
			}
			c = next();
		}
	}

	/**
	 * Return the next entity. These entities are translated to Characters: {@code &  '  >  <  "}.
	 *
	 * @param ampersand An ampersand character.
	 * @return A Character or an entity String if the entity is not recognized.
	 * @throws JSONException If missing ';' in XML entity.
	 */
	public Object nextEntity(char ampersand) throws JSONException {
		final StringBuilder sb = new StringBuilder();
		char c;
		for (; ; ) {
			c = next();
			if (Character.isLetterOrDigit(c) || c == '#') {
				sb.append(Character.toLowerCase(c));
			} else if (c == ';') {
				break;
			} else {
				throw syntaxError("Missing ';' in XML entity: &" + sb);
			}
		}
		return unescapeEntity(sb.toString());
	}

	/**
	 * Unescape an XML entity encoding;
	 *
	 * @param e entity (only the actual entity value, not the preceding & or ending ;
	 * @return Unescape str
	 */
	static String unescapeEntity(final String e) {
		// validate
		if (e == null || e.isEmpty()) {
			return "";
		}
		// if our entity is an encoded unicode point, parse it.
		if (e.charAt(0) == '#') {
			final int cp;
			if (e.charAt(1) == 'x' || e.charAt(1) == 'X') {
				// hex encoded unicode
				cp = Integer.parseInt(e.substring(2), 16);
			} else {
				// decimal encoded unicode
				cp = Integer.parseInt(e.substring(1));
			}
			return new String(new int[]{cp}, 0, 1);
		}
		final Character knownEntity = entity.get(e);
		if (knownEntity == null) {
			// we don't know the entity so keep it encoded
			return '&' + e + ';';
		}
		return knownEntity.toString();
	}

	/**
	 * Returns the next XML meta token. This is used for skipping over &lt;!...&gt; and &lt;?...?&gt; structures.
	 *
	 * @return Syntax characters ({@code < > / = ! ?}) are returned as Character, and strings and names are returned as Boolean. We don't care what the values actually are.
	 * @throws JSONException 字符串中属性未关闭或XML结构错误抛出此异常。If a string is not properly closed or if the XML is badly structured.
	 */
	public Object nextMeta() throws JSONException {
		char c;
		char q;
		do {
			c = next();
		} while (Character.isWhitespace(c));
		switch (c) {
			case 0:
				throw syntaxError("Misshaped meta tag");
			case '<':
				return XML.LT;
			case '>':
				return XML.GT;
			case '/':
				return XML.SLASH;
			case '=':
				return XML.EQ;
			case '!':
				return XML.BANG;
			case '?':
				return XML.QUEST;
			case '"':
			case '\'':
				q = c;
				for (; ; ) {
					c = next();
					if (c == 0) {
						throw syntaxError("Unterminated string");
					}
					if (c == q) {
						return Boolean.TRUE;
					}
				}
			default:
				for (; ; ) {
					c = next();
					if (Character.isWhitespace(c)) {
						return Boolean.TRUE;
					}
					switch (c) {
						case 0:
						case '<':
						case '>':
						case '/':
						case '=':
						case '!':
						case '?':
						case '"':
						case '\'':
							back();
							return Boolean.TRUE;
					}
				}
		}
	}

	/**
	 * Get the next XML Token. These tokens are found inside of angle brackets. <br>
	 * It may be one of these characters: {@code / > = ! ?} or it may be a string wrapped in single quotes or double
	 * quotes, or it may be a name.
	 *
	 * @return a String or a Character.
	 * @throws JSONException If the XML is not well formed.
	 */
	public Object nextToken() throws JSONException {
		char c;
		char q;
		StringBuilder sb;
		do {
			c = next();
		} while (Character.isWhitespace(c));
		switch (c) {
			case 0:
				throw syntaxError("Misshaped element");
			case '<':
				throw syntaxError("Misplaced '<'");
			case '>':
				return XML.GT;
			case '/':
				return XML.SLASH;
			case '=':
				return XML.EQ;
			case '!':
				return XML.BANG;
			case '?':
				return XML.QUEST;

			// Quoted string

			case '"':
			case '\'':
				q = c;
				sb = new StringBuilder();
				for (; ; ) {
					c = next();
					if (c == 0) {
						throw syntaxError("Unterminated string");
					}
					if (c == q) {
						return sb.toString();
					}
					if (c == '&') {
						sb.append(nextEntity(c));
					} else {
						sb.append(c);
					}
				}
			default:

				// Name

				sb = new StringBuilder();
				for (; ; ) {
					sb.append(c);
					c = next();
					if (Character.isWhitespace(c)) {
						return sb.toString();
					}
					switch (c) {
						case 0:
							return sb.toString();
						case '>':
						case '/':
						case '=':
						case '!':
						case '?':
						case '[':
						case ']':
							back();
							return sb.toString();
						case '<':
						case '"':
						case '\'':
							throw syntaxError("Bad character in a name");
					}
				}
		}
	}

	/**
	 * Skip characters until past the requested string. If it is not found, we are left at the end of the source with a result of false.
	 *
	 * @param to A string to skip past.
	 * @return 是否成功skip
	 * @throws JSONException JSON异常
	 */
	public boolean skipPast(String to) throws JSONException {
		boolean b;
		char c;
		int i;
		int j;
		int offset = 0;
		int length = to.length();
		char[] circle = new char[length];

		/*
		 * First fill the circle buffer with as many characters as are in the to string. If we reach an early end, bail.
		 */

		for (i = 0; i < length; i += 1) {
			c = next();
			if (c == 0) {
				return false;
			}
			circle[i] = c;
		}

		/* We will loop, possibly for all of the remaining characters. */

		for (; ; ) {
			j = offset;
			b = true;

			/* Compare the circle buffer with the to string. */

			for (i = 0; i < length; i += 1) {
				if (circle[j] != to.charAt(i)) {
					b = false;
					break;
				}
				j += 1;
				if (j >= length) {
					j -= length;
				}
			}

			/* If we exit the loop with b intact, then victory is ours. */

			if (b) {
				return true;
			}

			/* Get the next character. If there isn't one, then defeat is ours. */

			c = next();
			if (c == 0) {
				return false;
			}
			/*
			 * Shove the character in the circle buffer and advance the circle offset. The offset is mod n.
			 */
			circle[offset] = c;
			offset += 1;
			if (offset >= length) {
				offset -= length;
			}
		}
	}
}
