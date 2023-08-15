/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.img;

import java.awt.Image;

/**
 * 图片缩略算法类型
 *
 * @author looly
 * @since 4.5.8
 */
public enum ScaleType {

	/**
	 * 默认
	 */
	DEFAULT(Image.SCALE_DEFAULT),
	/**
	 * 快速
	 */
	FAST(Image.SCALE_FAST),
	/**
	 * 平滑
	 */
	SMOOTH(Image.SCALE_SMOOTH),
	/**
	 * 使用 ReplicateScaleFilter 类中包含的图像缩放算法
	 */
	REPLICATE(Image.SCALE_REPLICATE),
	/**
	 * Area Averaging算法
	 */
	AREA_AVERAGING(Image.SCALE_AREA_AVERAGING);

	private final int value;
	/**
	 * 构造
	 *
	 * @param value 缩放方式
	 * @see Image#SCALE_DEFAULT
	 * @see Image#SCALE_FAST
	 * @see Image#SCALE_SMOOTH
	 * @see Image#SCALE_REPLICATE
	 * @see Image#SCALE_AREA_AVERAGING
	 */
	ScaleType(final int value) {
		this.value = value;
	}

	/**
	 * 获取值
	 *
	 * @return 值
	 */
	public int getValue() {
		return this.value;
	}
}
