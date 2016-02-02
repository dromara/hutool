package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.xiaoleilu.hutool.getter.OptNullBasicTypeFromObjectGetter;
import com.xiaoleilu.hutool.lang.Conver;

/**
 * A JSONArray is an ordered sequence of values. Its external text form is a string wrapped in square brackets with commas separating the values. The internal form is an object having <code>get</code>
 * and <code>opt</code> methods for accessing the values by index, and <code>put</code> methods for adding or replacing values. The values can be any of these types: <code>Boolean</code>,
 * <code>JSONArray</code>, <code>JSONObject</code>, <code>Number</code>, <code>String</code>, or the <code>JSONNull.NULL object</code>.
 * <p>
 * The constructor can convert a JSON text into a Java object. The <code>toString</code> method converts to JSON text.
 * <p>
 * A <code>get</code> method returns a value if one can be found, and throws an exception if one cannot be found. An <code>opt</code> method returns a default value instead of throwing an exception,
 * and so is useful for obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an object which you can cast or query for type. There are also typed <code>get</code> and <code>opt</code> methods that do type
 * checking and type coercion for you.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to JSON syntax rules. The constructors are more forgiving in the texts they will accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just before the closing bracket.</li>
 * <li>The <code>null</code> value will be inserted when there is <code>,</code> &nbsp;<small>(comma)</small> elision.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a quote or single quote, and if they do not contain leading or trailing spaces, and if they do not contain any of these
 * characters: <code>{ } [ ] / \ : , #</code> and if they do not look like numbers and if they are not the reserved words <code>true</code>, <code>false</code>, or <code>null</code>.</li>
 * </ul>
 *
 * @author JSON.org
 * @version 2015-10-29
 */
public class JSONArray extends OptNullBasicTypeFromObjectGetter<Integer> implements JSON, Iterable<Object>{

	/**
	 * The arrayList where the JSONArray's properties are kept.
	 */
	private final ArrayList<Object> myArrayList;

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
	 * Construct a JSONArray from a Collection.
	 *
	 * @param collection A Collection.
	 */
	public JSONArray(Collection<?> collection) {
		this.myArrayList = new ArrayList<Object>();
		if (collection != null) {
			for (Object o : collection) {
				this.myArrayList.add(JSONUtil.wrap(o));
			}
		}
	}

	/**
	 * Construct a JSONArray from an array
	 *
	 * @throws JSONException If not an array.
	 */
	public JSONArray(Object array) throws JSONException {
		this();
		if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			for (int i = 0; i < length; i += 1) {
				this.put(JSONUtil.wrap(Array.get(array, i)));
			}
		} else {
			throw new JSONException("JSONArray initial value should be a string or collection or array.");
		}
	}

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
		return JSONNull.NULL.equals(this.get(index));
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
	 * Get the enum value associated with a key.
	 * 
	 * @param clazz The type of enum to retrieve.
	 * @param index The index must be between 0 and length() - 1.
	 * @return The enum value at the index location or null if not found
	 */
	public <E extends Enum<E>> E getEnum(Class<E> clazz, int index) {
		return this.getEnum(clazz, index, null);
	}

	/**
	 * Get the enum value associated with a key.
	 * 
	 * @param clazz The type of enum to retrieve.
	 * @param index The index must be between 0 and length() - 1.
	 * @param defaultValue The default in case the value is not found
	 * @return The enum value at the index location or defaultValue if the value is not found or cannot be assigned to clazz
	 */
	public <E extends Enum<E>> E getEnum(Class<E> clazz, int index, E defaultValue) {
		return Conver.toEnum(clazz, this.get(index), defaultValue);
	}

	/**
	 * Get the optional JSONArray associated with an index.
	 *
	 * @param index subscript
	 * @return A JSONArray value, or null if the index has no value, or if the value is not a JSONArray.
	 */
	public JSONArray getJSONArray(int index) {
		Object o = this.get(index);
		return o instanceof JSONArray ? (JSONArray) o : null;
	}

	/**
	 * Get the optional JSONObject associated with an index. Null is returned if the key is not found, or null if the index has no value, or if the value is not a JSONObject.
	 *
	 * @param index The index must be between 0 and length() - 1.
	 * @return A JSONObject value.
	 */
	public JSONObject getJSONObject(int index) {
		Object o = this.get(index);
		return o instanceof JSONObject ? (JSONObject) o : null;
	}

	/**
	 * Append a boolean value. This increases the array's length by one.
	 *
	 * @param value A boolean value.
	 * @return this.
	 */
	public JSONArray put(boolean value) {
		this.put(value ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	/**
	 * Put a value in the JSONArray, where the value will be a JSONArray which is produced from a Collection.
	 *
	 * @param value A Collection value.
	 * @return this.
	 */
	public JSONArray put(Collection<?> value) {
		this.put(new JSONArray(value));
		return this;
	}

	/**
	 * Append a double value. This increases the array's length by one.
	 *
	 * @param value A double value.
	 * @throws JSONException if the value is not finite.
	 * @return this.
	 */
	public JSONArray put(double value) throws JSONException {
		Double d = new Double(value);
		JSONUtil.testValidity(d);
		this.put(d);
		return this;
	}

	/**
	 * Append an int value. This increases the array's length by one.
	 *
	 * @param value An int value.
	 * @return this.
	 */
	public JSONArray put(int value) {
		this.put(new Integer(value));
		return this;
	}

	/**
	 * Append an long value. This increases the array's length by one.
	 *
	 * @param value A long value.
	 * @return this.
	 */
	public JSONArray put(long value) {
		this.put(new Long(value));
		return this;
	}

	/**
	 * Put a value in the JSONArray, where the value will be a JSONObject which is produced from a Map.
	 *
	 * @param value A Map value.
	 * @return this.
	 */
	public JSONArray put(Map<?, ?> value) {
		this.put(new JSONObject(value));
		return this;
	}

	/**
	 * Append an object value. This increases the array's length by one.
	 *
	 * @param value An object value. The value should be a Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONNull.NULL object.
	 * @return this.
	 */
	public JSONArray put(Object value) {
		this.myArrayList.add(value);
		return this;
	}

	/**
	 * Put or replace a boolean value in the JSONArray. If the index is greater than the length of the JSONArray, then null elements will be added as necessary to pad it out.
	 *
	 * @param index The subscript.
	 * @param value A boolean value.
	 * @return this.
	 * @throws JSONException If the index is negative.
	 */
	public JSONArray put(int index, boolean value) throws JSONException {
		this.put(index, value ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	/**
	 * Put a value in the JSONArray, where the value will be a JSONArray which is produced from a Collection.
	 *
	 * @param index The subscript.
	 * @param value A Collection value.
	 * @return this.
	 * @throws JSONException If the index is negative or if the value is not finite.
	 */
	public JSONArray put(int index, Collection<?> value) throws JSONException {
		this.put(index, new JSONArray(value));
		return this;
	}

	/**
	 * Put or replace a double value. If the index is greater than the length of the JSONArray, then null elements will be added as necessary to pad it out.
	 *
	 * @param index The subscript.
	 * @param value A double value.
	 * @return this.
	 * @throws JSONException If the index is negative or if the value is not finite.
	 */
	public JSONArray put(int index, double value) throws JSONException {
		this.put(index, new Double(value));
		return this;
	}

	/**
	 * Put or replace an int value. If the index is greater than the length of the JSONArray, then null elements will be added as necessary to pad it out.
	 *
	 * @param index The subscript.
	 * @param value An int value.
	 * @return this.
	 * @throws JSONException If the index is negative.
	 */
	public JSONArray put(int index, int value) throws JSONException {
		this.put(index, new Integer(value));
		return this;
	}

	/**
	 * Put or replace a long value. If the index is greater than the length of the JSONArray, then null elements will be added as necessary to pad it out.
	 *
	 * @param index The subscript.
	 * @param value A long value.
	 * @return this.
	 * @throws JSONException If the index is negative.
	 */
	public JSONArray put(int index, long value) throws JSONException {
		this.put(index, new Long(value));
		return this;
	}

	/**
	 * Put a value in the JSONArray, where the value will be a JSONObject that is produced from a Map.
	 *
	 * @param index The subscript.
	 * @param value The Map value.
	 * @return this.
	 * @throws JSONException If the index is negative or if the the value is an invalid number.
	 */
	public JSONArray put(int index, Map<?, ?> value) throws JSONException {
		this.put(index, new JSONObject(value));
		return this;
	}

	/**
	 * Put or replace an object value in the JSONArray. If the index is greater than the length of the JSONArray, then null elements will be added as necessary to pad it out.
	 *
	 * @param index The subscript.
	 * @param value The value to put into the array. The value should be a Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONNull.NULL object.
	 * @return this.
	 * @throws JSONException If the index is negative or if the the value is an invalid number.
	 */
	public JSONArray put(int index, Object value) throws JSONException {
		JSONUtil.testValidity(value);
		if (index < 0) {
			throw new JSONException("JSONArray[" + index + "] not found.");
		}
		if (index < this.length()) {
			this.myArrayList.set(index, value);
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
			Object valueThis = this.get(i);
			Object valueOther = ((JSONArray) other).get(i);
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
			jo.put(names.getStr(i), this.get(i));
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
