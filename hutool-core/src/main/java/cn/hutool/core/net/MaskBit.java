package cn.hutool.core.net;

import java.util.HashMap;
import java.util.Map;

/**
 * 掩码位和掩码之间的Map对应
 *
 * @since 5.4.1
 * @author looly
 */
public class MaskBit {

	private static final Map<Integer, String> maskBitMap;
	static {
		maskBitMap = new HashMap<>(32);
		maskBitMap.put(1, "128.0.0.0");
		maskBitMap.put(2, "192.0.0.0");
		maskBitMap.put(3, "224.0.0.0");
		maskBitMap.put(4, "240.0.0.0");
		maskBitMap.put(5, "248.0.0.0");
		maskBitMap.put(6, "252.0.0.0");
		maskBitMap.put(7, "254.0.0.0");
		maskBitMap.put(8, "255.0.0.0");
		maskBitMap.put(9, "255.128.0.0");
		maskBitMap.put(10, "255.192.0.0");
		maskBitMap.put(11, "255.224.0.0");
		maskBitMap.put(12, "255.240.0.0");
		maskBitMap.put(13, "255.248.0.0");
		maskBitMap.put(14, "255.252.0.0");
		maskBitMap.put(15, "255.254.0.0");
		maskBitMap.put(16, "255.255.0.0");
		maskBitMap.put(17, "255.255.128.0");
		maskBitMap.put(18, "255.255.192.0");
		maskBitMap.put(19, "255.255.224.0");
		maskBitMap.put(20, "255.255.240.0");
		maskBitMap.put(21, "255.255.248.0");
		maskBitMap.put(22, "255.255.252.0");
		maskBitMap.put(23, "255.255.254.0");
		maskBitMap.put(24, "255.255.255.0");
		maskBitMap.put(25, "255.255.255.128");
		maskBitMap.put(26, "255.255.255.192");
		maskBitMap.put(27, "255.255.255.224");
		maskBitMap.put(28, "255.255.255.240");
		maskBitMap.put(29, "255.255.255.248");
		maskBitMap.put(30, "255.255.255.252");
		maskBitMap.put(31, "255.255.255.254");
		maskBitMap.put(32, "255.255.255.255");
	}

	/**
	 * 根据掩码位获取掩码
	 *
	 * @param maskBit 掩码位
	 * @return 掩码
	 */
	public static String get(int maskBit) {
		return maskBitMap.get(maskBit);
	}
}
