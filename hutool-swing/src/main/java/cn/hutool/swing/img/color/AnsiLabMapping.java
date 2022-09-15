package cn.hutool.swing.img.color;

import cn.hutool.core.lang.ansi.AnsiElement;

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
	public AnsiElement lookupClosest(Color color) {
		return lookupClosest(new LabColor(color));
	}

	/**
	 * 查找与给定LabColor最接近的AnsiElement颜色
	 *
	 * @param color {@link LabColor}
	 * @return 最接近的Ansi4BitColor
	 */
	public AnsiElement lookupClosest(LabColor color) {
		AnsiElement closest = null;
		double closestDistance = Float.MAX_VALUE;
		for (Map.Entry<AnsiElement, LabColor> entry : ansiLabMap.entrySet()) {
			double candidateDistance = color.getDistance(entry.getValue());
			if (closest == null || candidateDistance < closestDistance) {
				closestDistance = candidateDistance;
				closest = entry.getKey();
			}
		}
		return closest;
	}
}
