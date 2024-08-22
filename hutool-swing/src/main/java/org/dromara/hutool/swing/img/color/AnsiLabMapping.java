/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
