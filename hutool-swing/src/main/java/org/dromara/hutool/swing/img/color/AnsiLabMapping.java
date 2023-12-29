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

package org.dromara.hutool.swing.img.color;

import org.dromara.hutool.core.lang.ansi.AnsiElement;

import java.awt.Color;
import java.util.Map;

/**
 * ANSI颜色和Lab颜色的对应查找表，
 *
 * @author Tom Xin
 */
public abstract class AnsiLabMapping {
	protected Map<AnsiElement, LabColor> ansiLabMap;

	/**
	 * 查找与给定LabColor最接近的AnsiElement颜色
	 *
	 * @param color {@link LabColor}
	 * @return 最接近的Ansi4BitColor
	 */
	public AnsiElement lookupClosest(final Color color) {
		return lookupClosest(new LabColor(color));
	}

	/**
	 * 查找与给定LabColor最接近的AnsiElement颜色
	 *
	 * @param color {@link LabColor}
	 * @return 最接近的Ansi4BitColor
	 */
	public AnsiElement lookupClosest(final LabColor color) {
		AnsiElement closest = null;
		double closestDistance = Float.MAX_VALUE;
		for (final Map.Entry<AnsiElement, LabColor> entry : ansiLabMap.entrySet()) {
			final double candidateDistance = color.getDistance(entry.getValue());
			if (closest == null || candidateDistance < closestDistance) {
				closestDistance = candidateDistance;
				closest = entry.getKey();
			}
		}
		return closest;
	}
}
