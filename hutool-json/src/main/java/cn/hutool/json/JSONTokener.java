package cn.hutool.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * JSON解析器，用于将JSON字符串解析为JSONObject或者JSONArray
 * 
 * @author from JSON.org
 */
public class JSONTokener {

	private long character;
	/** 是否结尾 End of stream */
	private boolean eof;
	/** 在Reader的位置（解析到第几个字符） */
	private long index;
	/** 当前所在行 */
	private long line;
	/** 前一个字符 */
	private char previous;
	/** 是否使用前一个字符 */
	private boolean usePrevious;
	/** 源 */
	private Reader reader;

	// ------------------------------------------------------------------------------------ Constructor start
	/**
	 * 从Reader中构建
	 * 
	 * @param reader Reader
	 */
	public JSONTokener(Reader reader) {
		this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
		this.eof = false;
		this.usePrevious = false;
		this.previous = 0;
		this.index = 0;
		this.character = 1;
		this.line = 1;
	}

	/**
	 * 从InputStream中构建
	 * 
	 * @param inputStream InputStream
	 */
	public JSONTokener(InputStream inputStream) throws JSONException {
		this(new InputStreamReader(inputStream));
	}

	/**
	 * 从字符串中构建
	 * 
	 * @param s JSON字符串
	 */
	public JSONTokener(String s) {
		this(new StringReader(s));
	}
	// ------------------------------------------------------------------------------------ Constructor end

	/**
	 * 将标记回退到第一个字符，重新开始解析新的JSON
	 */
	public void back() throws JSONException {
		if (this.usePrevious || this.index <= 0) {
			throw new JSONException("Stepping back two steps is not supported");
		}
		this.index -= 1;
		this.character -= 1;
		this.usePrevious = true;
		this.eof = false;
	}

	/**
	 * @return 是否进入结尾
	 */
	public boolean end() {
		return this.eof && false == this.usePrevious;
	}

	/**
	 * 源字符串是否有更多的字符
	 * 
	 * @return 如果未达到结尾返回true，否则false
	 */
	public boolean more() throws JSONException {
		this.next();
		if (this.end()) {
			return false;
		}
		this.back();
		return true;
	}

	/**
	 * 获得源字符串中的下一个字符
	 * 
	 * @return 下一个字符, or 0 if past the end of the source string.
	 * @throws JSONException JSON异常，包装IO异常
	 */
	public char next() throws JSONException {
		int c;
		if (this.usePrevious) {
			this.usePrevious = false;
			c = this.previous;
		} else {
			try {
				c = this.reader.read();
			} catch (IOException exception) {
				throw new JSONException(exception);
			}

			if (c <= 0) { // End of stream
				this.eof = true;
				c = 0;
			}
		}
		this.index += 1;
		if (this.previous == '\r') {
			this.line += 1;
			this.character = c == '\n' ? 0 : 1;
		} else if (c == '\n') {
			this.line += 1;
			this.character = 0;
		} else {
			this.character += 1;
		}
		this.previous = (char) c;
		return this.previous;
	}

	/**
	 * 读取下一个字符，并比对是否和指定字符匹配
	 * 
	 * @param c 被匹配的字符
	 * @return The character 匹配到的字符
	 * @throws JSONException 如果不匹配抛出此异常
	 */
	public char next(char c) throws JSONException {
		char n = this.next();
		if (n != c) {
			throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
		}
		return n;
	}

	/**
	 * 获得接下来的n个字符
	 *
	 * @param n 字符数
	 * @return 获得的n个字符组成的字符串
	 * @throws JSONException 如果源中余下的字符数不足以提供所需的字符数，抛出此异常
	 */
	public String next(int n) throws JSONException {
		if (n == 0) {
			return "";
		}

		char[] chars = new char[n];
		int pos = 0;
		while (pos < n) {
			chars[pos] = this.next();
			if (this.end()) {
				throw this.syntaxError("Substring bounds error");
			}
			pos += 1;
		}
		return new String(chars);
	}

	/**
	 * 获得下一个字符，跳过空白符
	 * 
	 * @throws JSONException 获得下一个字符时抛出的异常
	 * @return 获得的字符，0表示没有更多的字符
	 */
	public char nextClean() throws JSONException {
		char c;
		while (true) {
			c = this.next();
			if (c == 0 || c > ' ') {
				return c;
			}
		}
	}

	/**
	 * 返回当前位置到指定引号前的所有字符，反斜杠的转义符也会被处理。<br>
	 * 标准的JSON是不允许使用单引号包含字符串的，但是此实现允许。
	 * 
	 * @param quote 字符引号, 包括 <code>"</code>（双引号） 或 <code>'</code>（单引号）。
	 * @return 截止到引号前的字符串
	 * @throws JSONException 出现无结束的字符串时抛出此异常
	 */
	public String nextString(char quote) throws JSONException {
		char c;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			c = this.next();
			switch (c) {
			case 0:
			case '\n':
			case '\r':
				throw this.syntaxError("Unterminated string");
			case '\\':// 转义符
				c = this.next();
				switch (c) {
				case 'b':
					sb.append('\b');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 'u':// Unicode符
					sb.append((char) Integer.parseInt(this.next(4), 16));
					break;
				case '"':
				case '\'':
				case '\\':
				case '/':
					sb.append(c);
					break;
				default:
					throw this.syntaxError("Illegal escape.");
				}
				break;
			default:
				if (c == quote) {
					return sb.toString();
				}
				sb.append(c);
			}
		}
	}

	/**
	 * Get the text up but not including the specified character or the end of line, whichever comes first. <br>
	 * 获得从当前位置直到分隔符（不包括分隔符）或行尾的的所有字符。
	 * 
	 * @param delimiter 分隔符
	 * @return 字符串
	 */
	public String nextTo(char delimiter) throws JSONException {
		StringBuilder sb = new StringBuilder();
		for (;;) {
			char c = this.next();
			if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
				if (c != 0) {
					this.back();
				}
				return sb.toString().trim();
			}
			sb.append(c);
		}
	}

	/**
	 * Get the text up but not including one of the specified delimiter characters or the end of line, whichever comes first.
	 * 
	 * @param delimiters A set of delimiter characters.
	 * @return A string, trimmed.
	 */
	public String nextTo(String delimiters) throws JSONException {
		char c;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			c = this.next();
			if (delimiters.indexOf(c) >= 0 || c == 0 || c == '\n' || c == '\r') {
				if (c != 0) {
					this.back();
				}
				return sb.toString().trim();
			}
			sb.append(c);
		}
	}

	/**
	 * 获得下一个值，值类型可以是Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONObject.NULL
	 * 
	 * @throws JSONException 语法错误
	 *
	 * @return Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONObject.NULL
	 */
	public Object nextValue() throws JSONException {
		char c = this.nextClean();
		String string;

		switch (c) {
		case '"':
		case '\'':
			return this.nextString(c);
		case '{':
			this.back();
			return new JSONObject(this);
		case '[':
			this.back();
			return new JSONArray(this);
		}

		/*
		 * Handle unquoted text. This could be the values true, false, or null, or it can be a number. 
		 * An implementation (such as this one) is allowed to also accept non-standard forms. Accumulate
		 * characters until we reach the end of the text or a formatting character.
		 */

		StringBuilder sb = new StringBuilder();
		while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
			sb.append(c);
			c = this.next();
		}
		this.back();

		string = sb.toString().trim();
		if ("".equals(string)) {
			throw this.syntaxError("Missing value");
		}
		return InternalJSONUtil.stringToValue(string);
	}

	/**
	 * Skip characters until the next character is the requested character. If the requested character is not found, no characters are skipped. 在遇到指定字符前，跳过其它字符。如果字符未找到，则不跳过任何字符。
	 * 
	 * @param to 需要定位的字符
	 * @return 定位的字符，如果字符未找到返回0
	 */
	public char skipTo(char to) throws JSONException {
		char c;
		try {
			long startIndex = this.index;
			long startCharacter = this.character;
			long startLine = this.line;
			this.reader.mark(1000000);
			do {
				c = this.next();
				if (c == 0) {
					this.reader.reset();
					this.index = startIndex;
					this.character = startCharacter;
					this.line = startLine;
					return c;
				}
			} while (c != to);
		} catch (IOException exception) {
			throw new JSONException(exception);
		}
		this.back();
		return c;
	}

	/**
	 * Make a JSONException to signal a syntax error. <br>
	 * 构建 JSONException 用于表示语法错误
	 *
	 * @param message 错误消息
	 * @return A JSONException object, suitable for throwing
	 */
	public JSONException syntaxError(String message) {
		return new JSONException(message + this.toString());
	}

	/**
	 * 转为 {@link JSONArray}
	 * 
	 * @return {@link JSONArray}
	 */
	public JSONArray toJSONArray() {
		JSONArray jsonArray = new JSONArray();
		if (this.nextClean() != '[') {
			throw this.syntaxError("A JSONArray text must start with '['");
		}
		if (this.nextClean() != ']') {
			this.back();
			while (true) {
				if (this.nextClean() == ',') {
					this.back();
					jsonArray.add(JSONNull.NULL);
				} else {
					this.back();
					jsonArray.add(this.nextValue());
				}
				switch (this.nextClean()) {
				case ',':
					if (this.nextClean() == ']') {
						return jsonArray;
					}
					this.back();
					break;
				case ']':
					return jsonArray;
				default:
					throw this.syntaxError("Expected a ',' or ']'");
				}
			}
		}
		return jsonArray;
	}

	/**
	 * Make a printable string of this JSONTokener.
	 *
	 * @return " at {index} [character {character} line {line}]"
	 */
	@Override
	public String toString() {
		return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
	}
}
