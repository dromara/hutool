package cn.hutool.core.util;

/**
 * 坐标转换相关工具类<br>
 *
 * 坐标转换相关参考网址: https://tool.lu/coordinate/
 * @author hongzhe.qin
 * @email qin462328037@163.com
 * @since 6.0
 */
public class W84Util {

	/**
	 * 坐标转换参数：(暂时位置具体名称)
	 */
	public static final Double X_PI = 3.14159265358979324 * 3000.0 / 180.0;

	/**
	 * 坐标转换参数：π
	 */
	public static final Double PI = 3.1415926535897932384626;

	/**
	 * 地球半径
	 */
	public static final Double RADIUS = 6378245.0;

	/**
	 * 修正参数
	 */
	public static final Double CORRECTION_PARAM = 0.00669342162296594323;


	/**
	 * 计算维度坐标
	 *
	 * @param lng 经度
	 * @param lat 维度
	 * @return ret 计算完成后的
	 */
	private static Double transForMLat(double lng, double lat) {
		Double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
		ret = transCore(ret, lng, lat);
		ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * 计算经度坐标
	 *
	 * @param lng 经度坐标
	 * @param lat 维度坐标
	 * @return ret 计算完成后的
	 */
	private static Double transForMLng(Double lng, Double lat) {
		Double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
		ret = transCore(ret, lng, lat);
		ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * 转换坐标公共核心
	 *
	 * @param ret 计算需要返回结果
	 * @param lng 经度坐标
	 * @param lat 维度坐标
	 * @return 返回结果
	 */
	private static Double transCore(Double ret, Double lng, Double lat) {
		ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
		return ret;
	}

	private static Double transW84Core(Double lat){
		return 1 - CORRECTION_PARAM * Math.sin(lat / 180.0 * PI) * Math.sin(lat / 180.0 * PI);
	}

	/**
	 * 火星坐标系 (GCJ-02) 核心计算
	 * @param lng 经度值
	 * @param lat 纬度值
	 * @return 坐标
	 */
	private static Double[] transGCJ02Core(Double lng,Double lat){
		Double dlat = transForMLat(lng - 105.0, lat - 35.0);
		Double dlng = transForMLng(lng - 105.0, lat - 35.0);
		Double magic = transW84Core(lat);
		Double sqrtmagic = Math.sqrt(magic);
		dlat = (dlat * 180.0) / ((RADIUS * (1 - CORRECTION_PARAM)) / (magic * sqrtmagic) * PI);
		dlng = (dlng * 180.0) / (RADIUS / sqrtmagic * Math.cos(lat / 180.0 * PI) * PI);
		Double mglat = lat + dlat;
		Double mglng = lng + dlng;
		return new Double[]{mglng,mglat};
	}

	/**
	 * WGS84 坐标转为 百度坐标系 (BD-09) 坐标
	 * @param lng 经度值
	 * @param lat 维度值
	 * @return bd09 坐标
	 */
	public static Double[] wgs84tobd09(Double lng, Double lat) {
		// 第一次转换
		Double dlat = transForMLat(lng - 105.0, lat - 35.0);
		Double dlng = transForMLng(lng - 105.0, lat - 35.0);
		Double magic = transW84Core(lat);
		Double sqrtmagic = Math.sqrt(1 - CORRECTION_PARAM * Math.sin(lat / 180.0 * PI) * Math.sin(lat / 180.0 * PI));
		Double mglat = lat + (dlat * 180.0) / ((RADIUS * (1 - CORRECTION_PARAM)) / (magic * sqrtmagic) * PI);
		Double mglng = lng + (dlng * 180.0) / (RADIUS / sqrtmagic * Math.cos(lat / 180.0 * PI) * PI);
		// 第二次转换
		Double z = Math.sqrt(mglng * mglng + mglat * mglat) + 0.00002 * Math.sin(mglat * X_PI);
		Double theta = Math.atan2(mglat, mglng) + 0.000003 * Math.cos(mglng * X_PI);
		Double bd_lng = z * Math.cos(theta) + 0.0065;
		Double bd_lat = z * Math.sin(theta) + 0.006;
		return new Double[]{bd_lng,bd_lat};
	}

	/**
	 * WGS84 转换为 火星坐标系 (GCJ-02)
	 *
	 * @param lng
	 * @param lat
	 * @returns {*[]}
	 */
	public static Double[] wgs84togcj02(Double lng, Double lat) {
		return transGCJ02Core(lng,lat);
	}

	/**
	 * 火星坐标系 (GCJ-02) 转换为 WGS84
	 * @param lng 经度坐标
	 * @param lat 维度坐标
	 * @return WGS84 坐标
	 */
	public static Double[] gcj02towgs84(Double lng, Double lat) {
		return transGCJ02Core(lng,lat);
	}

	/**
	 *  百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
	 *  即 百度 转 谷歌、高德
	 * @param bd_lon 经度值
	 * @param bd_lat 纬度值
	 * @return GCJ-02 坐标
	 */
	public static Double[] bd09togcj02(Double bd_lon, Double bd_lat) {
		Double x = bd_lon - 0.0065;
		Double y = bd_lat - 0.006;
		Double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
		Double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
		Double gg_lng = z * Math.cos(theta);
		Double gg_lat = z * Math.sin(theta);
		return new Double[]{gg_lng,gg_lat};
	}

	/**
	 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
	 * @param lng 经度值
	 * @param lat 纬度值
	 * @return BD-09 坐标
	 */
	public static Double[] gcj02tobd09(Double lng, Double lat) {
		double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
		double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * X_PI);
		double bd_lng = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new Double[]{bd_lng,bd_lat};
	}

}
