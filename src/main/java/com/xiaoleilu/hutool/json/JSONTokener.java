package com.xiaoleilu.hutool.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/**
 * A JSONTokener takes a source string and extracts characters and tokens from it. <br>
 * It is used by the JSONObject and JSONArray constructors to parse JSON source strings.
 * 
 * @author JSON.org
 * @version 2014-05-03
 */
public class JSONTokener {

	private long character;
	private boolean eof;
	private long index;
	private long line;
	private char previous;
	private Reader reader;
	private boolean usePrevious;

	/**
	 * Construct a JSONTokener from a Reader.
	 *
	 * @param reader A reader.
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
	 * Construct a JSONTokener from an InputStream.
	 * 
	 * @param inputStream The source.
	 */
	public JSONTokener(InputStream inputStream) throws JSONException {
		this(new InputStreamReader(inputStream));
	}

	/**
	 * Construct a JSONTokener from a string.
	 *
	 * @param s A source string.
	 */
	public JSONTokener(String s) {
		this(new StringReader(s));
	}

	/**
	 * Back up one character. <br>
	 * This provides a sort of lookahead capability, <br>
	 * so that you can test for a digit or letter before attempting to parse the next number or identifier.
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
	 * Get the hex value of a character (base16).
	 * 
	 * @param c A character between '0' and '9' or between 'A' and 'F' or between 'a' and 'f'.
	 * @return An int between 0 and 15, or -1 if c was not a hex digit.
	 */
	public static int dehexchar(char c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		}
		if (c >= 'A' && c <= 'F') {
			return c - ('A' - 10);
		}
		if (c >= 'a' && c <= 'f') {
			return c - ('a' - 10);
		}
		return -1;
	}

	public boolean end() {
		return this.eof && !this.usePrevious;
	}

	/**
	 * Determine if the source string still contains characters that next() can consume.
	 * 
	 * @return true if not yet at the end of the source.
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
	 * Get the next character in the source string.
	 *
	 * @return The next character, or 0 if past the end of the source string.
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
	 * Consume the next character, and check that it matches a specified character.
	 * 
	 * @param c The character to match.
	 * @return The character.
	 * @throws JSONException if the character does not match.
	 */
	public char next(char c) throws JSONException {
		char n = this.next();
		if (n != c) {
			throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
		}
		return n;
	}

	/**
	 * Get the next n characters.
	 *
	 * @param n The number of characters to take.
	 * @return A string of n characters.
	 * @throws JSONException Substring bounds error if there are not n characters remaining in the source string.
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
	 * Get the next char in the string, skipping whitespace.
	 * 
	 * @throws JSONException
	 * @return A character, or 0 if there are no more characters.
	 */
	public char nextClean() throws JSONException {
		for (;;) {
			char c = this.next();
			if (c == 0 || c > ' ') {
				return c;
			}
		}
	}

	/**
	 * Return the characters up to the next close quote character. Backslash processing is done. <br>
	 * The formal JSON format does not allow strings in single quotes, but an implementation is allowed to accept them.
	 * 
	 * @param quote The quoting character, either <code>"</code>&nbsp;<small>(double quote)</small> or <code>'</code>&nbsp;<small>(single quote)</small>.
	 * @return A String.
	 * @throws JSONException Unterminated string.
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
				case '\\':
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
						case 'u':
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
	 * Get the text up but not including the specified character or the end of line, whichever comes first.
	 * 
	 * @param delimiter A delimiter character.
	 * @return A string.
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
	 * Get the next value. The value can be a Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONObject.NULL object.
	 * 
	 * @throws JSONException If syntax error.
	 *
	 * @return An object.
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
		 * Handle unquoted text. This could be the values true, false, or null, or it can be a number. An implementation (such as this one) is allowed to also accept non-standard forms. Accumulate
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
		return JSONUtil.stringToValue(string);
	}

	/**
	 * Skip characters until the next character is the requested character. If the requested character is not found, no characters are skipped.
	 * 
	 * @param to A character to skip to.
	 * @return The requested character, or zero if the requested character is not found.
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
	 * Make a JSONException to signal a syntax error.
	 *
	 * @param message The error message.
	 * @return A JSONException object, suitable for throwing
	 */
	public JSONException syntaxError(String message) {
		return new JSONException(message + this.toString());
	}

	/**
	 * Make a printable string of this JSONTokener.
	 *
	 * @return " at {index} [character {character} line {line}]"
	 */
	public String toString() {
		return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
	}
}
