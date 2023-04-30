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
	public void wgs84ToGcj02Test() {
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.wgs84ToGcj02(116.404, 39.915);
		Assert.assertEquals(116.41024449916938D, coordinate.getLng(), 0);
		Assert.assertEquals(39.91640428150164D, coordinate.getLat(), 0);
	}

	@Test
	public void gcj02ToWgs84Test() {
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.gcj02ToWgs84(116.404, 39.915);
		Assert.assertEquals(116.39775550083061D, coordinate.getLng(), 0);
		Assert.assertEquals(39.91359571849836D, coordinate.getLat(), 0);
	}

	@Test
	public void wgs84toBd09Test() {
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.wgs84ToBd09(116.404, 39.915);
		Assert.assertEquals(116.41662724378733D, coordinate.getLng(), 0);
		Assert.assertEquals(39.922699552216216D, coordinate.getLat(), 0);
	}

	@Test
	public void wgs84toBd09Test2() {
		// https://tool.lu/coordinate/
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.wgs84ToBd09(122.99395597D, 44.99804071D);
		Assert.assertEquals(123.00636516028885D, coordinate.getLng(), 0);
		Assert.assertEquals(45.0063690918959D, coordinate.getLat(), 0);
	}

	@Test
	public void bd09toWgs84Test() {
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.bd09toWgs84(116.404, 39.915);
		Assert.assertEquals(116.3913836995125D, coordinate.getLng(), 0);
		Assert.assertEquals(39.907253214522164D, coordinate.getLat(), 0);
	}

	@Test
	public void gcj02ToBd09Test() {
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.gcj02ToBd09(116.404, 39.915);
		Assert.assertEquals(116.41036949371029D, coordinate.getLng(), 0);
		Assert.assertEquals(39.92133699351022D, coordinate.getLat(), 0);
	}

	@Test
	public void bd09toGcj02Test() {
		final CoordinateUtil.Coordinate coordinate = CoordinateUtil.bd09ToGcj02(116.404, 39.915);
		Assert.assertEquals(116.39762729119315D, coordinate.getLng(), 0);
		Assert.assertEquals(39.90865673957631D, coordinate.getLat(), 0);
	}
}
