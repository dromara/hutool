package cn.hutool.core.convert;

/**
 * @author totalo
 * @since 5.6.0
 * 中文转数字结构
 */
public final class ChineseNameValue {
	/**
	 * 中文权名称
	 */
	String name;
	/**
	 * 10的倍数值
	 */
	int value;

	/**
	 * 是否为节权位
	 */
	boolean secUnit;

	public ChineseNameValue(String name, int value, boolean secUnit) {
		this.name = name;
		this.value = value;
		this.secUnit = secUnit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isSecUnit() {
		return secUnit;
	}

	public void setSecUnit(boolean secUnit) {
		this.secUnit = secUnit;
	}
}
