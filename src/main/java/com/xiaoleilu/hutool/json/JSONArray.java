package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * JSON数组
 * @author looly
 */
public class JSONArray extends JSONGetter<Integer> implements JSON, Iterable<Object>{

	/**
	 * The arrayList where the JSONArray's properties are kept.
	 */
	private final ArrayList<Object> myArrayList;

	//-------------------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * Construct an empty JSONArray.
	 */
	public JSONArray() {
		this.myArrayList = new ArrayList<Object>();
	}

	/**
	 * Construct a JSONArray from a JSONTokener.
	 *
	 * @param x A JSONTokener
	 * @throws JSONException If there is a syntax error.
	 */
	public JSONArray(JSONTokener x) throws JSONException {
		this();
		if (x.nextClean() != '[') {
			throw x.syntaxError("A JSONArray text must start with '['");
		}
		if (x.nextClean() != ']') {
			x.back();
			for (;;) {
				if (x.nextClean() == ',') {
					x.back();
					this.myArrayList.add(JSONNull.NULL);
				} else {
					x.back();
					this.myArrayList.add(x.nextValue());
				}
				switch (x.nextClean()) {
					case ',':
						if (x.nextClean() == ']') {
							return;
						}
						x.back();
						break;
					case ']':
						return;
					default:
						throw x.syntaxError("Expected a ',' or ']'");
				}
			}
		}
	}

	/**
	 * Construct a JSONArray from a source JSON text.
	 *
	 * @param source A string that begins with <code>[</code>&nbsp;<small>(left bracket)</small> and ends with <code>]</code> &nbsp;<small>(right bracket)</small>.
	 * @throws JSONException If there is a syntax error.
	 */
	public JSONArray(String source) throws JSONException {
		this(new JSONTokener(source));
	}

	/**
	 * Construct a JSONArray from an array or Collection
	 *
	 * @throws JSONException If not an array.
	 */
	public JSONArray(Object arrayOrCollection) throws JSONException {
		this();
		if (arrayOrCollection.getClass().isArray()) {//数组
			int length = Array.getLength(arrayOrCollection);
			for (int i = 0; i < length; i += 1) {
				this.put(JSONUtil.wrap(Array.get(arrayOrCollection, i)));
			}
		} else if(arrayOrCollection instanceof Collection){//Collection
			for (Object o : (Collection<?>)arrayOrCollection) {
				this.put(JSONUtil.wrap(o));
			}
		}else{
			throw new JSONException("JSONArray initial value should be a string or collection or array.");
		}
	}
	//-------------------------------------------------------------------------------------------------------------------- Constructor start

	@Override
	public Iterator<Object> iterator() {
		return myArrayList.iterator();
	}

	/**
	 * Determine if the value is null.
	 *
	 * @param index The index must be between 0 and length() - 1.
	 * @return true if the value at the index is null, or if there is no value.
	 */
	public boolean isNull(int index) {
		return JSONNull.NULL.equals(this.getObj(index));
	}

	/**
	 * Make a string from the contents of this JSONArray. The <code>separator</code> string is inserted between each element. Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param separator A string that will be inserted between the elements.
	 * @return a string.
	 * @throws JSONException If the array contains an invalid number.
	 */
	public String join(String separator) throws JSONException {
		int len = this.length();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i += 1) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(JSONUtil.valueToString(this.myArrayList.get(i)));
		}
		return sb.toString();
	}

	/**
	 * Get the number of elements in the JSONArray, included nulls.
	 *
	 * @return The length (or size).
	 */
	public int length() {
		return this.myArrayList.size();
	}

	/**
	 * Get the optional object value associated with an index.
	 *
	 * @param index The index must be between 0 and length() - 1.
	 * @return An object value, or null if there is no object at that index.
	 */
	public Object get(int index) {
		return getObj(index);
	}
	
	@Override
	public Object getObj(Integer index, Object defaultValue) {
		return (index < 0 || index >= this.length()) ? defaultValue : this.myArrayList.get(index);
	}
	
	/**
	 * Append an object value. This increases the array's length by one.
	 *
	 * @param value An object value. The value should be a Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONNull.NULL object.
	 * @return this.
	 */
	public JSONArray put(Object value) {
		this.myArrayList.add(JSONUtil.wrap(value));
		return this;
	}

	/**
	 * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
	 *
	 * @param index 位置
	 * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException index < 0 或者非有限的数字
	 */
	public JSONArray put(int index, Object value) throws JSONException {
		if (index < 0) {
			throw new JSONException("JSONArray[" + index + "] not found.");
		}
		if (index < this.length()) {
			JSONUtil.testValidity(value);
			this.myArrayList.set(index, JSONUtil.wrap(value));
		} else {
			while (index != this.length()) {
				this.put(JSONNull.NULL);
			}
			this.put(value);
		}
		return this;
	}

	/**
	 * Remove an index and close the hole.
	 *
	 * @param index The index of the element to be removed.
	 * @return The value that was associated with the index, or null if there was no value.
	 */
	public Object remove(int index) {
		return index >= 0 && index < this.length() ? this.myArrayList.remove(index) : null;
	}

	/**
	 * Determine if two JSONArrays are similar. They must contain similar sequences.
	 *
	 * @param other The other JSONArray
	 * @return true if they are equal
	 */
	public boolean similar(Object other) {
		if (!(other instanceof JSONArray)) {
			return false;
		}
		int len = this.length();
		if (len != ((JSONArray) other).length()) {
			return false;
		}
		for (int i = 0; i < len; i += 1) {
			Object valueThis = this.getObj(i);
			Object valueOther = ((JSONArray) other).getObj(i);
			if (valueThis instanceof JSONObject) {
				if (!((JSONObject) valueThis).similar(valueOther)) {
					return false;
				}
			} else if (valueThis instanceof JSONArray) {
				if (!((JSONArray) valueThis).similar(valueOther)) {
					return false;
				}
			} else if (!valueThis.equals(valueOther)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Produce a JSONObject by combining a JSONArray of names with the values of this JSONArray.
	 *
	 * @param names A JSONArray containing a list of key strings. These will be paired with the values.
	 * @return A JSONObject, or null if there are no names or if this JSONArray has no values.
	 * @throws JSONException If any of the names are null.
	 */
	public JSONObject toJSONObject(JSONArray names) throws JSONException {
		if (names == null || names.length() == 0 || this.length() == 0) {
			return null;
		}
		JSONObject jo = new JSONObject();
		for (int i = 0; i < names.length(); i += 1) {
			jo.put(names.getStr(i), this.getObj(i));
		}
		return jo;
	}

	/**
	 * Make a JSON text of this JSONArray. For compactness, no unnecessary whitespace is added. If it is not possible to produce a syntactically correct JSON text then null will be returned instead.
	 * This could occur if the array contains an invalid number.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @return a printable, displayable, transmittable representation of the array.
	 */
	@Override
	public String toString() {
		try {
			return this.toJSONString(0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Make a prettyprinted JSON text of this JSONArray. Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param indentFactor The number of spaces to add to each level of indentation.
	 * @return a printable, displayable, transmittable representation of the object, beginning with <code>[</code>&nbsp;<small>(left bracket)</small> and ending with <code>]</code> &nbsp;<small>(right
	 *         bracket)</small>.
	 * @throws JSONException
	 */
	@Override
	public String toJSONString(int indentFactor) throws JSONException {
		StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0).toString();
		}
	}

	@Override
	public Writer write(Writer writer) throws JSONException {
		return this.write(writer, 0, 0);
	}

	@Override
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		try {
			boolean commanate = false;
			int length = this.length();
			writer.write('[');

			if (length == 1) {
				JSONUtil.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
			} else if (length != 0) {
				final int newindent = indent + indentFactor;

				for (int i = 0; i < length; i += 1) {
					if (commanate) {
						writer.write(',');
					}
					if (indentFactor > 0) {
						writer.write('\n');
					}
					JSONUtil.indent(writer, newindent);
					JSONUtil.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent);
					commanate = true;
				}
				if (indentFactor > 0) {
					writer.write('\n');
				}
				JSONUtil.indent(writer, indent);
			}
			writer.write(']');
			return writer;
		} catch (IOException e) {
			throw new JSONException(e);
		}
	}
}
