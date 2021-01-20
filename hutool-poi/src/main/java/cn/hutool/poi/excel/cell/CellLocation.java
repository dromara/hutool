package cn.hutool.poi.excel.cell;

import java.io.Serializable;
import java.util.Objects;

/**
 * 单元格位置
 *
 * @author Looly
 * @since 5.1.4
 */
public class CellLocation implements Serializable {
	private static final long serialVersionUID = 1L;

	private int x;
	private int y;

	/**
	 * 构造
	 *
	 * @param x 列号，从0开始
	 * @param y 行号，从0开始
	 */
	public CellLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CellLocation that = (CellLocation) o;
		return x == that.x && y == that.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "CellLocation{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
