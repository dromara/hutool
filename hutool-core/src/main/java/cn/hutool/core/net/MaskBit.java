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

	private static final Map<Integer, String> MASK_BIT_MAP;
	static {
		MASK_BIT_MAP = new HashMap<>(32);
		MASK_BIT_MAP.put(1, "128.0.0.0");
		MASK_BIT_MAP.put(2, "192.0.0.0");
		MASK_BIT_MAP.put(3, "224.0.0.0");
		MASK_BIT_MAP.put(4, "240.0.0.0");
		MASK_BIT_MAP.put(5, "248.0.0.0");
		MASK_BIT_MAP.put(6, "252.0.0.0");
		MASK_BIT_MAP.put(7, "254.0.0.0");
		MASK_BIT_MAP.put(8, "255.0.0.0");
		MASK_BIT_MAP.put(9, "255.128.0.0");
		MASK_BIT_MAP.put(10, "255.192.0.0");
		MASK_BIT_MAP.put(11, "255.224.0.0");
		MASK_BIT_MAP.put(12, "255.240.0.0");
		MASK_BIT_MAP.put(13, "255.248.0.0");
		MASK_BIT_MAP.put(14, "255.252.0.0");
		MASK_BIT_MAP.put(15, "255.254.0.0");
		MASK_BIT_MAP.put(16, "255.255.0.0");
		MASK_BIT_MAP.put(17, "255.255.128.0");
		MASK_BIT_MAP.put(18, "255.255.192.0");
		MASK_BIT_MAP.put(19, "255.255.224.0");
		MASK_BIT_MAP.put(20, "255.255.240.0");
		MASK_BIT_MAP.put(21, "255.255.248.0");
		MASK_BIT_MAP.put(22, "255.255.252.0");
		MASK_BIT_MAP.put(23, "255.255.254.0");
		MASK_BIT_MAP.put(24, "255.255.255.0");
		MASK_BIT_MAP.put(25, "255.255.255.128");
		MASK_BIT_MAP.put(26, "255.255.255.192");
		MASK_BIT_MAP.put(27, "255.255.255.224");
		MASK_BIT_MAP.put(28, "255.255.255.240");
		MASK_BIT_MAP.put(29, "255.255.255.248");
		MASK_BIT_MAP.put(30, "255.255.255.252");
		MASK_BIT_MAP.put(31, "255.255.255.254");
		MASK_BIT_MAP.put(32, "255.255.255.255");
	}

	/**
	 * 根据掩码位获取掩码
	 *
	 * @param maskBit 掩码位
	 * @return 掩码
	 */
	public static String get(int maskBit) {
		return MASK_BIT_MAP.get(maskBit);
	}
}
