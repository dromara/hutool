package cn.hutool.swing.img.color;

import cn.hutool.core.lang.ansi.Ansi4BitColor;

import java.util.LinkedHashMap;

/**
 * ANSI 4bit 颜色和Lab颜色映射关系
 *
 * @author Tom Xin
 */
public class Ansi4bitMapping extends AnsiLabMapping {
	public static final Ansi4bitMapping INSTANCE = new Ansi4bitMapping();

	public Ansi4bitMapping() {
		ansiLabMap = new LinkedHashMap<>(16, 1);
		ansiLabMap.put(Ansi4BitColor.BLACK, new LabColor(0x000000));
		ansiLabMap.put(Ansi4BitColor.RED, new LabColor(0xAA0000));
		ansiLabMap.put(Ansi4BitColor.GREEN, new LabColor(0x00AA00));
		ansiLabMap.put(Ansi4BitColor.YELLOW, new LabColor(0xAA5500));
		ansiLabMap.put(Ansi4BitColor.BLUE, new LabColor(0x0000AA));
		ansiLabMap.put(Ansi4BitColor.MAGENTA, new LabColor(0xAA00AA));
		ansiLabMap.put(Ansi4BitColor.CYAN, new LabColor(0x00AAAA));
		ansiLabMap.put(Ansi4BitColor.WHITE, new LabColor(0xAAAAAA));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_BLACK, new LabColor(0x555555));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_RED, new LabColor(0xFF5555));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_GREEN, new LabColor(0x55FF00));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_YELLOW, new LabColor(0xFFFF55));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_BLUE, new LabColor(0x5555FF));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_MAGENTA, new LabColor(0xFF55FF));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_CYAN, new LabColor(0x55FFFF));
		ansiLabMap.put(Ansi4BitColor.BRIGHT_WHITE, new LabColor(0xFFFFFF));
	}
}
