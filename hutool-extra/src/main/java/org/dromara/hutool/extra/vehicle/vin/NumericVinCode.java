package org.dromara.hutool.extra.vehicle.vin;

/**
 * 数字字码.
 *
 * @author dax
 * @since 2023 /5/14 17:42
 */
class NumericVinCode implements MaskVinCode {
	private final String code;
	private final int index;
	private final int mask;

	/**
	 * 该构造会校验字码是否符合GB16735标准
	 *
	 * @param code  字码值
	 * @param index 索引位
	 * @throws IllegalArgumentException 校验
	 */
	NumericVinCode(String code, int index) throws IllegalArgumentException {

		if (!NUMERIC.matcher(code).matches()) {
			throw new IllegalArgumentException("索引为 " + index + " 的字码必须是数字");
		}
		this.code = code;
		this.index = index;
		this.mask = Integer.parseInt(code) * WEIGHT_FACTORS.get(index);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getMask() {
		return mask;
	}

	@Override
	public int getIndex() {
		return index;
	}


	@Override
	public String toString() {
		return "NumericCode{" +
			"code='" + code + '\'' +
			", index=" + index +
			", mask=" + mask +
			'}';
	}
}
