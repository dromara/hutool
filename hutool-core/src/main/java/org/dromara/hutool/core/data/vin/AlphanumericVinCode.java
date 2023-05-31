package org.dromara.hutool.core.data.vin;

/**
 * 可以是字母或者数字的字码
 *
 * @author dax
 * @since 2023 /5/14 17:56
 */
class AlphanumericVinCode implements MaskVinCode {
	private final String code;
	private final int index;
	private final int mask;


	/**
	 * 该构造会校验字码是否符合GB16735标准
	 *
	 * @param code  字码值
	 * @param index 索引位
	 */
	AlphanumericVinCode(String code, int index) {
		this.code = code;
		this.index = index;
		int weight = WEIGHT_FACTORS.get(index);
		mask = NUMERIC.matcher(this.code).matches() ?
			Integer.parseInt(this.code) * weight :
			VinCodeMaskEnum.valueOf(this.code).getMaskCode() * weight;
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
		return "AlphanumericVinCode{" +
			"code='" + code + '\'' +
			", index=" + index +
			", mask=" + mask +
			'}';
	}
}
