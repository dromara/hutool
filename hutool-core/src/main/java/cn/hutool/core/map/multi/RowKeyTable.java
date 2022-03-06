package cn.hutool.core.map.multi;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.collection.TransIter;
import cn.hutool.core.util.ObjectUtil;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class RowKeyTable<R, C, V> implements Table<R, C, V> {

	final Map<R, Map<C, V>> raw;
	final Supplier<? extends Map<C, V>> supplier;

	public RowKeyTable(Map<R, Map<C, V>> raw) {
		this(raw, HashMap::new);
	}

	public RowKeyTable(Map<R, Map<C, V>> raw, Supplier<? extends Map<C, V>> columnMapSupplier) {
		this.raw = raw;
		this.supplier = null == columnMapSupplier ? HashMap::new : columnMapSupplier;
	}

	@Override
	public Map<R, Map<C, V>> rowMap() {
		return raw;
	}

	@Override
	public Map<C, Map<R, V>> columnMap() {
		// TODO 实现columnMap
		throw new UnsupportedOperationException("TODO implement this method");
	}

	@Override
	public Collection<V> values() {
		return this.values;
	}

	@Override
	public Set<Cell<R, C, V>> cellSet() {
		return this.cellSet;
	}

	@Override
	public V put(R rowKey, C columnKey, V value) {
		return raw.computeIfAbsent(rowKey, (key) -> supplier.get()).put(columnKey, value);
	}

	@Override
	public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
		if (null != table) {
			for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
				put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
			}
		}
	}

	@Override
	public V remove(R rowKey, C columnKey) {
		final Map<C, V> map = getRow(rowKey);
		if (null == map) {
			return null;
		}
		final V value = map.remove(columnKey);
		if (map.isEmpty()) {
			raw.remove(rowKey);
		}
		return value;
	}

	@Override
	public boolean isEmpty() {
		return raw.isEmpty();
	}

	@Override
	public void clear() {
		this.raw.clear();
	}

	@Override
	public Iterator<Cell<R, C, V>> iterator() {
		return new CellIterator();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof Table) {
			final Table<?, ?, ?> that = (Table<?, ?, ?>) obj;
			return this.cellSet().equals(that.cellSet());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return cellSet().hashCode();
	}

	@Override
	public String toString() {
		return rowMap().toString();
	}

	/**
	 * 基于{@link Cell}的{@link Iterator}实现
	 */
	private class CellIterator implements Iterator<Cell<R, C, V>> {
		final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = raw.entrySet().iterator();
		Map.Entry<R, Map<C, V>> rowEntry;
		Iterator<Map.Entry<C, V>> columnIterator = IterUtil.empty();

		@Override
		public boolean hasNext() {
			return rowIterator.hasNext() || columnIterator.hasNext();
		}

		@Override
		public Cell<R, C, V> next() {
			if (false == columnIterator.hasNext()) {
				rowEntry = rowIterator.next();
				columnIterator = rowEntry.getValue().entrySet().iterator();
			}
			final Map.Entry<C, V> columnEntry = columnIterator.next();
			return new SimpleCell<>(rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
		}

		@Override
		public void remove() {
			columnIterator.remove();
			if (rowEntry.getValue().isEmpty()) {
				rowIterator.remove();
			}
		}
	}

	/**
	 * 简单{@link Cell} 实现
	 *
	 * @param <R> 行类型
	 * @param <C> 列类型
	 * @param <V> 值类型
	 */
	private static class SimpleCell<R, C, V> implements Cell<R, C, V>, Serializable {
		private static final long serialVersionUID = 1L;

		private final R rowKey;
		private final C columnKey;
		private final V value;

		SimpleCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
			this.rowKey = rowKey;
			this.columnKey = columnKey;
			this.value = value;
		}

		@Override
		public R getRowKey() {
			return rowKey;
		}

		@Override
		public C getColumnKey() {
			return columnKey;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof Cell) {
				Cell<?, ?, ?> other = (Cell<?, ?, ?>) obj;
				return ObjectUtil.equal(rowKey, other.getRowKey())
						&& ObjectUtil.equal(columnKey, other.getColumnKey())
						&& ObjectUtil.equal(value, other.getValue());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(rowKey, columnKey, value);
		}

		@Override
		public String toString() {
			return "(" + rowKey + "," + columnKey + ")=" + value;
		}
	}

	private final Collection<V> values = new AbstractCollection<V>() {
		@Override
		public Iterator<V> iterator() {
			return new TransIter<>(cellSet().iterator(), Cell::getValue);
		}

		@Override
		public boolean contains(Object o) {
			//noinspection unchecked
			return containsValue((V) o);
		}

		@Override
		public void clear() {
			RowKeyTable.this.clear();
		}

		@Override
		public int size() {
			return RowKeyTable.this.size();
		}
	};

	private final Set<Cell<R, C, V>> cellSet = new AbstractSet<Cell<R, C, V>>() {
		@Override
		public boolean contains(Object o) {
			if (o instanceof Cell) {
				@SuppressWarnings("unchecked") final
				Cell<R, C, V> cell = (Cell<R, C, V>) o;
				Map<C, V> row = getRow(cell.getRowKey());
				if (null != row) {
					return ObjectUtil.equals(row.get(cell.getColumnKey()), cell.getValue());
				}
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (contains(o)) {
				@SuppressWarnings("unchecked")
				final Cell<R, C, V> cell = (Cell<R, C, V>) o;
				RowKeyTable.this.remove(cell.getRowKey(), cell.getColumnKey());
			}
			return false;
		}

		@Override
		public void clear() {
			RowKeyTable.this.clear();
		}

		@Override
		public Iterator<Table.Cell<R, C, V>> iterator() {
			return new CellIterator();
		}

		@Override
		public int size() {
			return RowKeyTable.this.size();
		}
	};
}
