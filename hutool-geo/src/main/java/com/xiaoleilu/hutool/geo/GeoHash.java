package com.xiaoleilu.hutool.geo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * GeoHash实现<br>
 * 参考：https://github.com/kungfoo/geohash-java
 * 
 * @author Looly
 *
 */
public final class GeoHash implements Comparable<GeoHash>, Serializable {
	private static final long serialVersionUID = -8553214249630252175L;
	
	/** GeoHash长度 */
	private static final int MAX_BIT_PRECISION = 64;
	private static final int MAX_CHARACTER_PRECISION = 12;
	
	private static final int[] BITS = { 16, 8, 4, 2, 1 };
	private static final int BASE32_BITS = 5;
	public static final long FIRST_BIT_FLAGGED = 0x8000000000000000l;
	private static final char[] base32 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private final static Map<Character, Integer> decodeMap = new HashMap<>();

	static {
		int sz = base32.length;
		for (int i = 0; i < sz; i++) {
			decodeMap.put(base32[i], i);
		}
	}

	protected long bits = 0;
	/** 中点 */
	private Location point;
	private BoundingBox boundingBox;
	/** 有效bit位 */
	protected byte significantBits = 0;

	private GeoHash() {}

	/**
	 * 使用字符数限制精度
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param numberOfCharacters 字符数
	 * @return {@link GeoHash}
	 */
	public static GeoHash withCharacterPrecision(double latitude, double longitude, int numberOfCharacters) {
		if (numberOfCharacters > MAX_CHARACTER_PRECISION) {
			throw new IllegalArgumentException("A geohash can only be " + MAX_CHARACTER_PRECISION + " character long.");
		}
//		int desiredPrecision = (numberOfCharacters * 5 <= 60) ? numberOfCharacters * 5 : 60;
		return new GeoHash(latitude, longitude, numberOfCharacters * 5);
	}

	/**
	 * 创建 {@link GeoHash}，限制bit位精度
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param numberOfBits 限制bit位数
	 * @return {@link GeoHash}
	 */
	public static GeoHash withBitPrecision(double latitude, double longitude, int numberOfBits) {
		if (numberOfBits > MAX_BIT_PRECISION) {
			throw new IllegalArgumentException("A Geohash can only be " + MAX_BIT_PRECISION + " bits long!");
		}
		if (Math.abs(latitude) > 90.0 || Math.abs(longitude) > 180.0) {
			throw new IllegalArgumentException("Can't have lat/lon values out of (-90,90)/(-180/180)");
		}
		return new GeoHash(latitude, longitude, numberOfBits);
	}

	/**
	 * 二进制字符串转为 {@link GeoHash}
	 * @param binaryString 二进制字符串
	 * @return {@link GeoHash}
	 */
	public static GeoHash fromBinaryString(String binaryString) {
		GeoHash geohash = new GeoHash();
		for (int i = 0; i < binaryString.length(); i++) {
			if (binaryString.charAt(i) == '1') {
				geohash.addOnBitToEnd();
			} else if (binaryString.charAt(i) == '0') {
				geohash.addOffBitToEnd();
			} else {
				throw new IllegalArgumentException(binaryString + " is not a valid geohash as a binary string");
			}
		}
		geohash.bits <<= (MAX_BIT_PRECISION - geohash.significantBits);
		long[] latitudeBits = geohash.getRightAlignedLatitudeBits();
		long[] longitudeBits = geohash.getRightAlignedLongitudeBits();
		return geohash.recombineLatLonBitsToHash(latitudeBits, longitudeBits);
	}

	/**
	 * GeoHash字符串（base32编码后）转为 {@link GeoHash}对象
	 * @param geohash GeoHash字符串
	 * @return {@link GeoHash}
	 */
	public static GeoHash fromGeohashString(String geohash) {
		double[] latitudeRange = { -90.0, 90.0 };
		double[] longitudeRange = { -180.0, 180.0 };

		boolean isEvenBit = true;
		GeoHash hash = new GeoHash();

		for (int i = 0; i < geohash.length(); i++) {
			int cd = decodeMap.get(geohash.charAt(i));
			for (int j = 0; j < BASE32_BITS; j++) {
				int mask = BITS[j];
				if (isEvenBit) {//偶数表示经度
					divideRangeDecode(hash, longitudeRange, (cd & mask) != 0);
				} else {//奇数表示纬度
					divideRangeDecode(hash, latitudeRange, (cd & mask) != 0);
				}
				isEvenBit = !isEvenBit;
			}
		}

		hash.boundingBox = new BoundingBox(latitudeRange[0], latitudeRange[1], longitudeRange[0], longitudeRange[1]);
		hash.point = hash.boundingBox.getCenterPoint();
		hash.bits <<= (MAX_BIT_PRECISION - hash.significantBits);
		return hash;
	}

	public static GeoHash fromLongValue(long hashVal, int significantBits) {
		String binaryString = Long.toBinaryString(hashVal);
		if(binaryString.length() > significantBits){
			binaryString = StrUtil.subPre(binaryString, significantBits);
		}
		return fromBinaryString(binaryString);
	}

	/**
	 * 坐标转为base32GeoHash编码
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param numberOfCharacters 字符长度
	 * @return GeoHash字符串
	 */
	public static String geoHashStringWithCharacterPrecision(double latitude, double longitude, int numberOfCharacters) {
		GeoHash hash = withCharacterPrecision(latitude, longitude, numberOfCharacters);
		return hash.toBase32();
	}

	/**
	 * 构造
	 * @param latitude 纬度
	 * @param longitude 经度
	 * @param desiredPrecision 限制长度
	 */
	private GeoHash(double latitude, double longitude, int desiredPrecision) {
		point = new Location(latitude, longitude);
		desiredPrecision = Math.min(desiredPrecision, MAX_BIT_PRECISION);

		boolean isEvenBit = true;
		double[] latitudeRange = { -90, 90 };
		double[] longitudeRange = { -180, 180 };

		while (significantBits < desiredPrecision) {
			if (isEvenBit) {
				divideRangeEncode(longitude, longitudeRange);
			} else {
				divideRangeEncode(latitude, latitudeRange);
			}
			isEvenBit = !isEvenBit;
		}

		this.boundingBox = new BoundingBox(latitudeRange[0], latitudeRange[1], longitudeRange[0], longitudeRange[1]);
		bits <<= (MAX_BIT_PRECISION - desiredPrecision);
	}

	public GeoHash next(int step) {
		return fromOrd(ord() + step, significantBits);
	}

	public GeoHash next() {
		return next(1);
	}

	public GeoHash prev() {
		return next(-1);
	}

	public long ord() {
		int insignificantBits = MAX_BIT_PRECISION - significantBits;
		return bits >>> insignificantBits;
	}

	/**
	 * Returns the number of characters that represent this hash.
	 * 
	 * @throws IllegalStateException
	 *             when the hash cannot be encoded in base32, i.e. when the
	 *             precision is not a multiple of 5.
	 */
	public int getCharacterPrecision() {
		if (significantBits % 5 != 0) {
			throw new IllegalStateException(
					"precision of GeoHash is not divisble by 5: " + this);
		}
		return significantBits / 5;
	}

	public static GeoHash fromOrd(long ord, int significantBits) {
		int insignificantBits = MAX_BIT_PRECISION - significantBits;
		return fromLongValue(ord << insignificantBits, significantBits);
	}

	/**
	 * Counts the number of geohashes contained between the two (ie how many
	 * times next() is called to increment from one to two) This value depends
	 * on the number of significant bits.
	 * 
	 * @param one
	 * @param two
	 * @return number of steps
	 */
	public static long stepsBetween(GeoHash one, GeoHash two) {
		if (one.significantBits() != two.significantBits()) {
			throw new IllegalArgumentException(
					"It is only valid to compare the number of steps between two hashes if they have the same number of significant bits");
		}
		return two.ord() - one.ord();
	}

	private void divideRangeEncode(double value, double[] range) {
		double mid = (range[0] + range[1]) / 2;
		if (value >= mid) {
			addOnBitToEnd();
			range[0] = mid;
		} else {
			addOffBitToEnd();
			range[1] = mid;
		}
	}

	/**
	 * 当值为1在range的右半区，为0在左半区
	 * @param hash {@link GeoHash}
	 * @param range 区域
	 * @param b bit值
	 */
	private static void divideRangeDecode(GeoHash hash, double[] range, boolean b) {
		double mid = (range[0] + range[1]) / 2;
		if (b) {
			hash.addOnBitToEnd();
			range[0] = mid;
		} else {
			hash.addOffBitToEnd();
			range[1] = mid;
		}
	}

	/**
	 * 获得相邻8个GeoHash<br>
	 * 顺序：北, 东北, 东, 东南, 南, 西南, 西, 西北
	 * @return 相邻8个GeoHash
	 */
	public GeoHash[] getAdjacent() {
		GeoHash northern = getNorthernNeighbour();
		GeoHash eastern = getEasternNeighbour();
		GeoHash southern = getSouthernNeighbour();
		GeoHash western = getWesternNeighbour();
		return new GeoHash[] { 
				northern,	//北
				northern.getEasternNeighbour(), //东北
				eastern, 	//东
				southern.getEasternNeighbour(),//东南
				southern,//南
				southern.getWesternNeighbour(), //西南
				western, //西
				northern.getWesternNeighbour() //西北
		};
	}

	/**
	 * 有效bit位
	 * @return 有效bit位
	 */
	public int significantBits() {
		return significantBits;
	}

	/**
	 * long值
	 * @return long值
	 */
	public long longValue() {
		return bits;
	}

	/**
	 * {@link GeoHash}的base32值
	 * @return {@link GeoHash}的base32值
	 */
	public String toBase32() {
		if (significantBits % 5 != 0) {
			throw new IllegalStateException("Cannot convert a geohash to base32 if the precision is not a multiple of 5.");
		}
		StringBuilder buf = new StringBuilder();

		long firstFiveBitsMask = 0xf800000000000000l;
		long bitsCopy = bits;
		int partialChunks = (int) Math.ceil(((double) significantBits / 5));

		for (int i = 0; i < partialChunks; i++) {
			int pointer = (int) ((bitsCopy & firstFiveBitsMask) >>> 59);
			buf.append(base32[pointer]);
			bitsCopy <<= 5;
		}
		return buf.toString();
	}

	/**
	 * returns true iff this is within the given geohash bounding box.
	 */
	
	public boolean within(GeoHash boundingBox) {
		return (bits & boundingBox.mask()) == boundingBox.bits;
	}

	/**
	 * find out if the given point lies within this hashes bounding box.<br>
	 * <i>Note: this operation checks the bounding boxes coordinates, i.e. does
	 * not use the {@link GeoHash}s special abilities.s</i>
	 */
	public boolean contains(Location point) {
		return boundingBox.contains(point);
	}

	/**
	 * returns the {@link Location} that was originally used to set up this.<br>
	 * If it was built from a base32-{@link String}, this is the center point of
	 * the bounding box.
	 */
	public Location getPoint() {
		return point;
	}

	/**
	 * return the center of this {@link GeoHash}s bounding box. this is rarely
	 * the same point that was used to build the hash.
	 */
	// TODO: make sure this method works as intented for corner cases!
	public Location getBoundingBoxCenterPoint() {
		return boundingBox.getCenterPoint();
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public boolean enclosesCircleAroundPoint(Location point, double radius) {
		return false;
	}

	/**
	 * 组合经纬度bit
	 * @param latBits 纬度bit
	 * @param lonBits 经度bit
	 * @return {@link GeoHash}
	 */
	protected GeoHash recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
		GeoHash hash = new GeoHash();
		boolean isEvenBit = false;
		latBits[0] <<= (MAX_BIT_PRECISION - latBits[1]);
		lonBits[0] <<= (MAX_BIT_PRECISION - lonBits[1]);
		double[] latitudeRange = { -90.0, 90.0 };
		double[] longitudeRange = { -180.0, 180.0 };

		for (int i = 0; i < latBits[1] + lonBits[1]; i++) {
			if (isEvenBit) {
				divideRangeDecode(hash, latitudeRange, (latBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
				latBits[0] <<= 1;
			} else {
				divideRangeDecode(hash, longitudeRange, (lonBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
				lonBits[0] <<= 1;
			}
			isEvenBit = !isEvenBit;
		}
		hash.bits <<= (MAX_BIT_PRECISION - hash.significantBits);
		this.boundingBox = new BoundingBox(latitudeRange[0], latitudeRange[1], longitudeRange[0], longitudeRange[1]);
		hash.point = hash.boundingBox.getCenterPoint();
		return hash;
	}

	public GeoHash getNorthernNeighbour() {
		long[] latitudeBits = getRightAlignedLatitudeBits();
		long[] longitudeBits = getRightAlignedLongitudeBits();
		latitudeBits[0] += 1;
		latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
		return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
	}

	public GeoHash getSouthernNeighbour() {
		long[] latitudeBits = getRightAlignedLatitudeBits();
		long[] longitudeBits = getRightAlignedLongitudeBits();
		latitudeBits[0] -= 1;
		latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
		return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
	}

	public GeoHash getEasternNeighbour() {
		long[] latitudeBits = getRightAlignedLatitudeBits();
		long[] longitudeBits = getRightAlignedLongitudeBits();
		longitudeBits[0] += 1;
		longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
		return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
	}

	public GeoHash getWesternNeighbour() {
		long[] latitudeBits = getRightAlignedLatitudeBits();
		long[] longitudeBits = getRightAlignedLongitudeBits();
		longitudeBits[0] -= 1;
		longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
		return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
	}

	protected long[] getRightAlignedLatitudeBits() {
		long copyOfBits = bits << 1;
		long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[0]);
		return new long[] { value, getNumberOfLatLonBits()[0] };
	}

	protected long[] getRightAlignedLongitudeBits() {
		long copyOfBits = bits;
		long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[1]);
		return new long[] { value, getNumberOfLatLonBits()[1] };
	}

	private long extractEverySecondBit(long copyOfBits, int numberOfBits) {
		long value = 0;
		for (int i = 0; i < numberOfBits; i++) {
			if ((copyOfBits & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
				value |= 0x1;
			}
			value <<= 1;
			copyOfBits <<= 2;
		}
		value >>>= 1;
		return value;
	}

	protected int[] getNumberOfLatLonBits() {
		if (significantBits % 2 == 0) {
			return new int[] { significantBits / 2, significantBits / 2 };
		} else {
			return new int[] { significantBits / 2, significantBits / 2 + 1 };
		}
	}

	protected final void addOnBitToEnd() {
		significantBits++;
		bits <<= 1;
		bits = bits | 0x1;
	}

	protected final void addOffBitToEnd() {
		significantBits++;
		bits <<= 1;
	}

	@Override
	public String toString() {
		if (significantBits % 5 == 0) {
			return String.format("%s -> %s -> %s", Long.toBinaryString(bits), boundingBox, toBase32());
		} else {
			return String.format("%s -> %s, bits: %d", Long.toBinaryString(bits), boundingBox, significantBits);
		}
	}

	public String toBinaryString() {
		StringBuilder bui = new StringBuilder();
		long bitsCopy = bits;
		for (int i = 0; i < significantBits; i++) {
			if ((bitsCopy & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
				bui.append('1');
			} else {
				bui.append('0');
			}
			bitsCopy <<= 1;
		}
		return bui.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof GeoHash) {
			GeoHash other = (GeoHash) obj;
			if (other.significantBits == significantBits && other.bits == bits) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int f = 17;
		f = 31 * f + (int) (bits ^ (bits >>> 32));
		f = 31 * f + significantBits;
		return f;
	}

	/**
	 * return a long mask for this hashes significant bits.
	 */
	private long mask() {
		if (significantBits == 0) {
			return 0;
		} else {
			long value = FIRST_BIT_FLAGGED;
			value >>= (significantBits - 1);
			return value;
		}
	}

	private long maskLastNBits(long value, long n) {
		long mask = 0xffffffffffffffffl;
		mask >>>= (MAX_BIT_PRECISION - n);
		return value & mask;
	}

	@Override
	public int compareTo(GeoHash o) {
		int bitsCmp = Long.compare(bits ^ FIRST_BIT_FLAGGED, o.bits ^ FIRST_BIT_FLAGGED);
		if (bitsCmp != 0) {
			return bitsCmp;
		} else {
			return Integer.compare(significantBits, o.significantBits);
		}
	}
}


