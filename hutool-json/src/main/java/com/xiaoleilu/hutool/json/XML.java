package com.xiaoleilu.hutool.json;

import java.util.Iterator;

/**
 * 提供静态方法在XML和JSONObject之间转换
 * 
 * @author JSON.org
 */
public class XML {

	/** The Character '&amp;'. */
	public static final Character AMP = '&';

	/** The Character '''. */
	public static final Character APOS = '\'';

	/** The Character '!'. */
	public static final Character BANG = '!';

	/** The Character '='. */
	public static final Character EQ = '=';

	/** The Character '&gt;'. */
	public static final Character GT = '>';

	/** The Character '&lt;'. */
	public static final Character LT = '<';

	/** The Character '?'. */
	public static final Character QUEST = '?';

	/** The Character '"'. */
	public static final Character QUOT = '"';

	/** The Character '/'. */
	public static final Character SLASH = '/';

	/**
	 * Replace special characters with XML escapes:
	 * 
	 * <pre>
	 * &amp; (ampersand) is replaced by &amp;amp;
	 * &lt; (less than) is replaced by &amp;lt;
	 * &gt; (greater than) is replaced by &amp;gt;
	 * &quot; (double quote) is replaced by &amp;quot;
	 * </pre>
	 * 
	 * @param string The string to be escaped.
	 * @return The escaped string.
	 */
	public static String escape(String string) {
		StringBuilder sb = new StringBuilder(string.length());
		for (int i = 0, length = string.length(); i < length; i++) {
			char c = string.charAt(i);
			switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&apos;");
					break;
				default:
					sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Throw an exception if the string contains whitespace. Whitespace is not allowed in tagNames and attributes.
	 * 
	 * @param string A string.
	 * @throws JSONException Thrown if the string contains whitespace or is empty.
	 */
	public static void noSpace(String string) throws JSONException {
		int i, length = string.length();
		if (length == 0) {
			throw new JSONException("Empty string.");
		}
		for (i = 0; i < length; i += 1) {
			if (Character.isWhitespace(string.charAt(i))) {
				throw new JSONException("'" + string + "' contains a space character.");
			}
		}
	}

	/**
	 * Scan the content following the named tag, attaching it to the context.
	 * 
	 * @param x The XMLTokener containing the source string.
	 * @param context The JSONObject that will include the new material.
	 * @param name The tag name.
	 * @return true if the close tag is processed.
	 * @throws JSONException
	 */
	private static boolean parse(XMLTokener x, JSONObject context, String name, boolean keepStrings) throws JSONException {
		char c;
		int i;
		JSONObject jsonobject = null;
		String string;
		String tagName;
		Object token;

		// Test for and skip past these forms:
		// <!-- ... -->
		// <! ... >
		// <![ ... ]]>
		// <? ... ?>
		// Report errors for these forms:
		// <>
		// <=
		// <<

		token = x.nextToken();

		// <!

		if (token == BANG) {
			c = x.next();
			if (c == '-') {
				if (x.next() == '-') {
					x.skipPast("-->");
					return false;
				}
				x.back();
			} else if (c == '[') {
				token = x.nextToken();
				if ("CDATA".equals(token)) {
					if (x.next() == '[') {
						string = x.nextCDATA();
						if (string.length() > 0) {
							context.accumulate("content", string);
						}
						return false;
					}
				}
				throw x.syntaxError("Expected 'CDATA['");
			}
			i = 1;
			do {
				token = x.nextMeta();
				if (token == null) {
					throw x.syntaxError("Missing '>' after '<!'.");
				} else if (token == LT) {
					i += 1;
				} else if (token == GT) {
					i -= 1;
				}
			} while (i > 0);
			return false;
		} else if (token == QUEST) {

			// <?
			x.skipPast("?>");
			return false;
		} else if (token == SLASH) {

			// Close tag </

			token = x.nextToken();
			if (name == null) {
				throw x.syntaxError("Mismatched close tag " + token);
			}
			if (!token.equals(name)) {
				throw x.syntaxError("Mismatched " + name + " and " + token);
			}
			if (x.nextToken() != GT) {
				throw x.syntaxError("Misshaped close tag");
			}
			return true;

		} else if (token instanceof Character) {
			throw x.syntaxError("Misshaped tag");

			// Open tag <

		} else {
			tagName = (String) token;
			token = null;
			jsonobject = new JSONObject();
			for (;;) {
				if (token == null) {
					token = x.nextToken();
				}

				// attribute = value
				if (token instanceof String) {
					string = (String) token;
					token = x.nextToken();
					if (token == EQ) {
						token = x.nextToken();
						if (!(token instanceof String)) {
							throw x.syntaxError("Missing value");
						}
						jsonobject.accumulate(string, keepStrings ? token : InternalJSONUtil.stringToValue((String) token));
						token = null;
					} else {
						jsonobject.accumulate(string, "");
					}

				} else if (token == SLASH) {
					// Empty tag <.../>
					if (x.nextToken() != GT) {
						throw x.syntaxError("Misshaped tag");
					}
					if (jsonobject.size() > 0) {
						context.accumulate(tagName, jsonobject);
					} else {
						context.accumulate(tagName, "");
					}
					return false;

				} else if (token == GT) {
					// Content, between <...> and </...>
					for (;;) {
						token = x.nextContent();
						if (token == null) {
							if (tagName != null) {
								throw x.syntaxError("Unclosed tag " + tagName);
							}
							return false;
						} else if (token instanceof String) {
							string = (String) token;
							if (string.length() > 0) {
								jsonobject.accumulate("content", keepStrings ? token : InternalJSONUtil.stringToValue(string));
							}

						} else if (token == LT) {
							// Nested element
							if (parse(x, jsonobject, tagName, keepStrings)) {
								if (jsonobject.size() == 0) {
									context.accumulate(tagName, "");
								} else if (jsonobject.size() == 1 && jsonobject.get("content") != null) {
									context.accumulate(tagName, jsonobject.get("content"));
								} else {
									context.accumulate(tagName, jsonobject);
								}
								return false;
							}
						}
					}
				} else {
					throw x.syntaxError("Misshaped tag");
				}
			}
		}
	}

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 * Content text may be placed in a "content" member. Comments, prologs, DTDs, and <code>&lt;[ [ ]]&gt;</code> are ignored.
	 * 
	 * @param string The source string.
	 * @return A JSONObject containing the structured data from the XML string.
	 * @throws JSONException Thrown if there is an errors while parsing the string
	 */
	public static JSONObject toJSONObject(String string) throws JSONException {
		return toJSONObject(string, false);
	}

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 * Content text may be placed in a "content" member. Comments, prologs, DTDs, and <code>&lt;[ [ ]]&gt;</code> are ignored.
	 * All values are converted as strings, for 1, 01, 29.0 will not be coerced to numbers but will instead be the exact value as seen in the XML document.
	 * 
	 * @param string The source string.
	 * @param keepStrings If true, then values will not be coerced into boolean or numeric values and will instead be left as strings
	 * @return A JSONObject containing the structured data from the XML string.
	 * @throws JSONException Thrown if there is an errors while parsing the string
	 */
	public static JSONObject toJSONObject(String string, boolean keepStrings) throws JSONException {
		JSONObject jo = new JSONObject();
		XMLTokener x = new XMLTokener(string);
		while (x.more() && x.skipPast("<")) {
			parse(x, jo, null, keepStrings);
		}
		return jo;
	}

	/**
	 * 转换XML为JSONObject
	 * Convert a JSONObject into a well-formed, element-normal XML string.
	 * 
	 * @param object A JSONObject.
	 * @return A string.
	 * @throws JSONException Thrown if there is an error parsing the string
	 */
	public static String toString(Object object) throws JSONException {
		return toString(object, null);
	}

	/**
	 * 转换XML为JSONObject
	 * Convert a JSONObject into a well-formed, element-normal XML string.
	 * 
	 * @param object A JSONObject.
	 * @param tagName The optional name of the enclosing tag.
	 * @return A string.
	 * @throws JSONException Thrown if there is an error parsing the string
	 */
	public static String toString(Object object, String tagName) throws JSONException {
		StringBuilder sb = new StringBuilder();
		JSONArray ja;
		JSONObject jo;
		String key;
		Iterator<String> keys;
		String string;
		Object value;

		if (object instanceof JSONObject) {

			// Emit <tagName>
			if (tagName != null) {
				sb.append('<');
				sb.append(tagName);
				sb.append('>');
			}

			// Loop thru the keys.
			jo = (JSONObject) object;
			keys = jo.keySet().iterator();
			while (keys.hasNext()) {
				key = keys.next();
				value = jo.get(key);
				if (value == null) {
					value = "";
				} else if (value.getClass().isArray()) {
					value = new JSONArray(value);
				}
				string = value instanceof String ? (String) value : null;

				// Emit content in body
				if ("content".equals(key)) {
					if (value instanceof JSONArray) {
						ja = (JSONArray) value;
						int i = 0;
						for (Object val : ja) {
							if (i > 0) {
								sb.append('\n');
							}
							sb.append(escape(val.toString()));
							i++;
						}
					} else {
						sb.append(escape(value.toString()));
					}

					// Emit an array of similar keys

				} else if (value instanceof JSONArray) {
					ja = (JSONArray) value;
					for (Object val : ja) {
						if (val instanceof JSONArray) {
							sb.append('<');
							sb.append(key);
							sb.append('>');
							sb.append(toString(val));
							sb.append("</");
							sb.append(key);
							sb.append('>');
						} else {
							sb.append(toString(val, key));
						}
					}
				} else if ("".equals(value)) {
					sb.append('<');
					sb.append(key);
					sb.append("/>");

					// Emit a new tag <k>

				} else {
					sb.append(toString(value, key));
				}
			}
			if (tagName != null) {

				// Emit the </tagname> close tag
				sb.append("</");
				sb.append(tagName);
				sb.append('>');
			}
			return sb.toString();

		}

		if (object != null) {
			if (object.getClass().isArray()) {
				object = new JSONArray(object);
			}

			if (object instanceof JSONArray) {
				ja = (JSONArray) object;
				for (Object val : ja) {
					// XML does not have good support for arrays. If an array
					// appears in a place where XML is lacking, synthesize an
					// <array> element.
					sb.append(toString(val, tagName == null ? "array" : tagName));
				}
				return sb.toString();
			}
		}

		string = (object == null) ? "null" : escape(object.toString());
		return (tagName == null) ? "\"" + string + "\"" : (string.length() == 0) ? "<" + tagName + "/>" : "<" + tagName + ">" + string + "</" + tagName + ">";

	}
}
