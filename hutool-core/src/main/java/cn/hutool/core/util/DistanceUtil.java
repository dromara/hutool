package cn.hutool.core.util;

import java.awt.geom.Point2D;

/**
 * 通过经纬度获取地球上两点之间的距离
 * @author dazer & neusoft
 * @date 2021/4/4 23:42
 * @since 5.6.3
 * thank you：JAVA通过经纬度获取两点之间的距离 https://www.cnblogs.com/pxblog/p/13359801.html
 * thank you：JAVA根据经纬度获取两点之间的距离 https://blog.csdn.net/weixin_35815479/article/details/106972772
 * 百度地图拾取系统：http://api.map.baidu.com/lbsapi/getpoint/index.html
 * 高德地图拾取：https://lbs.amap.com/tools/picker
 */
public class DistanceUtil {
	/**地球半径，单位KM*/
	private static final double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
	 * “经度”【longitude】、“纬度”【 Latitude】。
	 * 参数为String类型
	 * @param lat1Str 第一个位置经度
	 * @param lng1Str 第一个位置纬度
	 * @param lat2Str 第二个位置经度
	 * @param lng2Str 第二个位置纬度
	 * @return 地球上两点之间的距离，单位KM
	 */
	public static String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
		double lat1 = 0;
		double lng1 = 0;
		double lat2 = 0;
		double lng2 = 0;
		try {
			lat1 = Double.parseDouble(lat1Str);
			lng1 = Double.parseDouble(lng1Str);
			lat2 = Double.parseDouble(lat2Str);
			lng2 = Double.parseDouble(lng2Str);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("经纬度入参不合法，请检查!", e);
		}

		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double difference = radLat1 - radLat2;
		double mdifference = rad(lng1) - rad(lng2);
		double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(mdifference / 2), 2)));
		distance = distance * EARTH_RADIUS;
		distance = Math.round(distance * 10000) / 10000.0;
		String distanceStr = distance+"";
		distanceStr = distanceStr.
				substring(0, distanceStr.indexOf("."));
		return distanceStr;
	}

	/**
	 * 通过AB点经纬度获取距离 整数
	 * @param pointA A点(经，纬)
	 * @param pointB B点(经，纬)
	 * @return 距离(单位：米)
	 */
	public static long getDistance(Point2D pointA, Point2D pointB) {
		// 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
		// A经弧度
		double radiansAx = Math.toRadians(pointA.getX());
		// A纬弧度
		double radiansAy = Math.toRadians(pointA.getY());
		// B经弧度
		double radiansBx = Math.toRadians(pointB.getX());
		// B纬弧度
		double radiansBy = Math.toRadians(pointB.getY());
		// 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
		double cos = Math.cos(radiansAy) * Math.cos(radiansBy) * Math.cos(radiansAx - radiansBx)
				+ Math.sin(radiansAy) * Math.sin(radiansBy);
		// 反余弦值
		double acos = Math.acos(cos);
		// 最终结果
		double h = EARTH_RADIUS * acos;

		//四舍五入
		//保留小数后两位
        /** BigDecimal b = new BigDecimal(h);
        double f1 = b.setScale(2, RoundingMode.HALF_UP).doubleValue();*/
		return Math.round(h);
	}
}
