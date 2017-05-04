package com.xiaoleilu.hutool.geo;

import java.io.Serializable;

/**
 * 矩形坐标区域<br>
 * 两组经纬度值（4个值）确定一个矩形区域<br>
 * 通常一个坐标点经过固定位数GeoHash计算后并不是一个确定值，而是一个范围，经纬度两个值产生的范围组成一个矩形区域
 * 
 * @author Looly
 *
 */
public class BoundingBox implements Serializable {
	private static final long serialVersionUID = -7145192134410261076L;

	/** 最小纬度 */
	private double minLat;
	/** 最大纬度 */
	private double maxLat;
	/** 最小经度 */
	private double minLon;
	/** 最大经度 */
	private double maxLon;

	/**
	 * 构造
	 * 
	 * @param p1 坐标1
	 * @param p2 坐标2
	 */
	public BoundingBox(Location p1, Location p2) {
		this(p1.getLatitude(), p2.getLatitude(), p1.getLongitude(), p2.getLongitude());
	}

	/**
	 * 构造
	 * 
	 * @param latitude1 纬度1
	 * @param latitude2 纬度2
	 * @param longitude1 经度1
	 * @param longgitude2 经度2
	 */
	public BoundingBox(double latitude1, double latitude2, double longitude1, double longgitude2) {
		minLat = Math.min(latitude1, latitude2);
		maxLat = Math.max(latitude1, latitude2);
		minLon = Math.min(longgitude2, longgitude2);
		maxLon = Math.max(longitude1, longgitude2);
	}

	public BoundingBox(BoundingBox that) {
		this(that.minLat, that.maxLat, that.minLon, that.maxLon);
	}
	
	/**
	 * 坐标点是否在矩形区域内
	 * 
	 * @param point 坐标点
	 * @return 是否在矩形区域内
	 */
	public boolean contains(Location point) {
		double latitude = point.getLatitude();
		double longitude = point.getLongitude();
		return (latitude >= minLat) && (longitude >= minLon) //
				&& (latitude <= maxLat) && (longitude <= maxLon);
	}

	/**
	 * 此矩形区域和其它区域是否有交集
	 * @param other 其它矩形区域
	 * @return 是否有交集
	 */
	public boolean intersects(BoundingBox other) {
		return false == (other.minLon > maxLon || other.maxLon < minLon || other.minLat > maxLat || other.maxLat < minLat);
	}

	/**
	 * 矩形中心点区域
	 * @return 矩形中心点区域
	 */
	public Location getCenterPoint() {
		double centerLatitude = (minLat + maxLat) / 2;
		double centerLongitude = (minLon + maxLon) / 2;
		return new Location(centerLatitude, centerLongitude);
	}

	/**
	 * 将两个矩形区域组合为一个更大的矩形区域
	 * @param other 其它矩形区域
	 */
	public void expandToInclude(BoundingBox other) {
		if (other.minLon < minLon) {
			minLon = other.minLon;
		}
		if (other.maxLon > maxLon) {
			maxLon = other.maxLon;
		}
		if (other.minLat < minLat) {
			minLat = other.minLat;
		}
		if (other.maxLat > maxLat) {
			maxLat = other.maxLat;
		}
	}

	/**
	 * @return 最小纬度
	 */
	public double getMinLat() {
		return minLat;
	}
	
	/**
	 * @return 最小经度
	 */
	public double getMinLon() {
		return minLon;
	}

	/**
	 * @return 最大纬度
	 */
	public double getMaxLat() {
		return maxLat;
	}

	/**
	 * @return 最大经度
	 */
	public double getMaxLon() {
		return maxLon;
	}

	/**
	 * 获得左上角坐标
	 * 
	 * @return 左上角坐标
	 */
	public Location getUpperLeft() {
		return new Location(maxLat, minLon);
	}

	/**
	 * 获得右下角坐标
	 * 
	 * @return 右下角坐标
	 */
	public Location getLowerRight() {
		return new Location(minLat, maxLon);
	}

	/**
	 * 获得纬度差
	 * 
	 * @return 纬度差
	 */
	public double getLatitudeSize() {
		return maxLat - minLat;
	}

	/**
	 * 获得经度差
	 * 
	 * @return 经度差
	 */
	public double getLongitudeSize() {
		return maxLon - minLon;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof BoundingBox) {
			BoundingBox other = (BoundingBox) obj;
			return Double.valueOf(minLat).equals(Double.valueOf(other.minLat)) 
					&& Double.valueOf(minLon).equals(Double.valueOf(other.minLon)) 
					&& Double.valueOf(maxLat).equals(Double.valueOf(other.maxLat)) 
					&& Double.valueOf(maxLon).equals(Double.valueOf(other.maxLon));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + hashCode(minLat);
		result = 37 * result + hashCode(maxLat);
		result = 37 * result + hashCode(minLon);
		result = 37 * result + hashCode(maxLon);
		return result;
	}

	/**
	 * 计算Hash值
	 * 
	 * @param x 坐标经纬度值
	 * @return hash值
	 */
	private static int hashCode(double x) {
		long f = Double.doubleToLongBits(x);
		return (int) (f ^ (f >>> 32));
	}
	
	@Override
	public String toString() {
		return getUpperLeft() + " -> " + getLowerRight();
	}
}
