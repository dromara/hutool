package org.dromara.hutool.extra.vehicle.vin;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 支持掩码的字码
 *
 * @author dax
 * @since 2023 /5/15 10:43
 */
interface MaskVinCode extends VinCode {
	/**
	 * 字码权重因子
	 */
	List<Integer> WEIGHT_FACTORS = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2);
	/**
	 * 数字位校验
	 */
	Pattern NUMERIC = Pattern.compile("^\\d$");

	/**
	 * 获取掩码，字码编码*加权值
	 *
	 * @return the mask
	 */
	int getMask();

	/**
	 * 所在位置索引,[0,16]
	 *
	 * @return the index
	 */
	int getIndex();
}
