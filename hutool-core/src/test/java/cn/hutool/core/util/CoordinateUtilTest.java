package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 坐标转换工具类单元测试<br>
 * 测试参考：https://github.com/wandergis/coordtransform
 *
 * @author hongzhe.qin, looly
 */
public class CoordinateUtilTest {

	@Test
	public void gcj02ToBd09Test() {
		final CoordinateUtil.Coordinate gcj02 = CoordinateUtil.gcj02ToBd09(116.404, 39.915);
		Assert.assertEquals(116.41036949371029D, gcj02.getLng(), 15);
		Assert.assertEquals(39.92133699351021D, gcj02.getLat(), 15);
	}

	@Test
	public void bd09toGcj02Test(){
		final CoordinateUtil.Coordinate gcj02 = CoordinateUtil.bd09ToGcj02(116.404, 39.915);
		Assert.assertEquals(116.39762729119315D, gcj02.getLng(), 15);
		Assert.assertEquals(39.90865673957631D, gcj02.getLat(), 15);
	}

	@Test
	public void gcj02ToWgs84(){
		final CoordinateUtil.Coordinate gcj02 = CoordinateUtil.wgs84ToGcj02(116.404, 39.915);
		Assert.assertEquals(116.39775550083061D, gcj02.getLng(), 15);
		Assert.assertEquals(39.91359571849836D, gcj02.getLat(), 15);
	}

	@Test
	public void wgs84ToGcj02Test(){
		final CoordinateUtil.Coordinate gcj02 = CoordinateUtil.wgs84ToGcj02(116.404, 39.915);
		Assert.assertEquals(116.41024449916938D, gcj02.getLng(), 15);
		Assert.assertEquals(39.91640428150164D, gcj02.getLat(), 15);
	}

	@Test
	public void wgs84toBd09(){

	}

}
