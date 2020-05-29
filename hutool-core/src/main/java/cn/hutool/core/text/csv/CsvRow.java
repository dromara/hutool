package cn.hutool.core.text.csv;

import cn.hutool.core.bean.BeanUtil;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * CSV中一行的表示
 *
 * @author Looly
 */
public final class CsvRow implements List<String> {

	/** 原始行号 */
	private final long originalLineNumber;

	final Map<String, Integer> headerMap;
	final List<String> fields;

	/**
	 * 构造
	 * 
	 * @param originalLineNumber 对应文件中的第几行
	 * @param headerMap 标题Map
	 * @param fields 数据列表
	 */
	public CsvRow(final long originalLineNumber, final Map<String, Integer> headerMap, final List<String> fields) {

		this.originalLineNumber = originalLineNumber;
		this.headerMap = headerMap;
		this.fields = fields;
	}

	/**
	 * 获取原始行号，多行情况下为首行行号。
	 *
	 * @return the original line number 行号
	 */
	public long getOriginalLineNumber() {
		return originalLineNumber;
	}

	/**
	 * 获取标题对应的字段内容
	 *
	 * @param name 标题名
	 * @return 字段值，null表示无此字段值
	 * @throws IllegalStateException CSV文件无标题行抛出此异常
	 */
	public String getByName(final String name) {
		if (headerMap == null) {
			throw new IllegalStateException("No header available");
		}

		final Integer col = headerMap.get(name);
		if (col != null) {
			return get(col);
		}
		return null;
	}

	/**
	 * 获取本行所有字段值列表
	 *
	 * @return 字段值列表
	 */
	public List<String> getRawList() {
		return fields;
	}

	/**
	 * 获取标题与字段值对应的Map
	 *
	 * @return 标题与字段值对应的Map
	 * @throws IllegalStateException CSV文件无标题行抛出此异常
	 */
	public Map<String, String> getFieldMap() {
		if (headerMap == null) {
			throw new IllegalStateException("No header available");
		}

		final Map<String, String> fieldMap = new LinkedHashMap<>(headerMap.size(), 1);
		String key;
		Integer col;
		String val;
		for (final Map.Entry<String, Integer> header : headerMap.entrySet()) {
			key = header.getKey();
			col = headerMap.get(key);
			val = null == col ? null : get(col);
			fieldMap.put(key, val);
		}

		return fieldMap;
	}

	/**
	 * 一行数据转换为Bean对象
	 *
	 * @param <T> Bean类型
	 * @param clazz bean类
	 * @return Bean
	 * @since 5.3.6
	 */
	public <T> T toBean(Class<T> clazz){
		return BeanUtil.mapToBean(getFieldMap(), clazz, true);
	}

	/**
	 * 获取字段格式
	 *
	 * @return 字段格式
	 */
	public int getFieldCount() {
		return fields.size();
	}

	@Override
	public int size() {
		return this.fields.size();
	}

	@Override
	public boolean isEmpty() {
		return this.fields.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.fields.contains(o);
	}

	@Override
	public Iterator<String> iterator() {
		return this.fields.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.fields.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		//noinspection SuspiciousToArrayCall
		return this.fields.toArray(a);
	}

	@Override
	public boolean add(String e) {
		return this.fields.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.fields.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.fields.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		return this.fields.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends String> c) {
		return this.fields.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.fields.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.fields.retainAll(c);
	}

	@Override
	public void clear() {
		this.fields.clear();
	}

	@Override
	public String get(int index) {
		return index >= fields.size() ? null : fields.get(index);
	}

	@Override
	public String set(int index, String element) {
		return this.fields.set(index, element);
	}

	@Override
	public void add(int index, String element) {
		this.fields.add(index, element);
	}

	@Override
	public String remove(int index) {
		return this.fields.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.fields.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.fields.lastIndexOf(o);
	}

	@Override
	public ListIterator<String> listIterator() {
		return this.fields.listIterator();
	}

	@Override
	public ListIterator<String> listIterator(int index) {
		return this.fields.listIterator(index);
	}

	@Override
	public List<String> subList(int fromIndex, int toIndex) {
		return this.fields.subList(fromIndex, toIndex);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CsvRow{");
		sb.append("originalLineNumber=");
		sb.append(originalLineNumber);
		sb.append(", ");

		sb.append("fields=");
		if (headerMap != null) {
			sb.append('{');
			for (final Iterator<Map.Entry<String, String>> it = getFieldMap().entrySet().iterator(); it.hasNext();) {

				final Map.Entry<String, String> entry = it.next();
				sb.append(entry.getKey());
				sb.append('=');
				if (entry.getValue() != null) {
					sb.append(entry.getValue());
				}
				if (it.hasNext()) {
					sb.append(", ");
				}
			}
			sb.append('}');
		} else {
			sb.append(fields.toString());
		}

		sb.append('}');
		return sb.toString();
	}
}
