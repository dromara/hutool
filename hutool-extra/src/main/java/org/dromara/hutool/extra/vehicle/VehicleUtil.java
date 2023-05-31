package org.dromara.hutool.extra.vehicle;

import org.dromara.hutool.extra.vehicle.vin.Vin;

/**
 * 汽车工具类封装
 *
 * @author VampireAchao
 */
public class VehicleUtil {

	/**
	 * 解析车辆识别代码
	 *
	 * @param vin 车辆识别代码
	 * @return 解析后的结果
	 */
	public static Vin parseVin(String vin) {
		return Vin.of(vin);
	}

}
