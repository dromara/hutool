package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.lang.WeightRandom.WeightObj;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 *
 * @author xiaoleilu
 */
public class RandomUtil {

	/**
	 * 用于随机选的数字
	 */
	public static final String BASE_NUMBER = "0123456789";
	/**
	 * 用于随机选的字符
	 */
	public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * 用于随机选的字符和数字
	 */
	public static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;

	/**
	 * 获取随机数生成器对象<br>
	 * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
	 *
	 * <p>
	 * 注意：此方法返回的{@link ThreadLocalRandom}不可以在多线程环境下共享对象，否则有重复随机数问题。
	 * 见：https://www.jianshu.com/p/89dfe990295c
	 * </p>
	 *
	 * @return {@link ThreadLocalRandom}
	 * @since 3.1.2
	 */
	public static ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

	/**
	 * 创建{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)<br>
	 *
	 * @param seed 自定义随机种子
	 * @return {@link SecureRandom}
	 * @since 4.6.5
	 */
	public static SecureRandom createSecureRandom(byte[] seed) {
		return (null == seed) ? new SecureRandom() : new SecureRandom(seed);
	}

	/**
	 * 获取{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)<br>
	 * 注意：此方法获取的是伪随机序列发生器PRNG（pseudo-random number generator）
	 *
	 * <p>
	 * 相关说明见：https://stackoverflow.com/questions/137212/how-to-solve-slow-java-securerandom
	 *
	 * @return {@link SecureRandom}
	 * @since 3.1.2
	 */
	public static SecureRandom getSecureRandom() {
		try {
			return SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 获取随机数产生器
	 *
	 * @param isSecure 是否为强随机数生成器 (RNG)
	 * @return {@link Random}
	 * @see #getSecureRandom()
	 * @see #getRandom()
	 * @since 4.1.15
	 */
	public static Random getRandom(boolean isSecure) {
		return isSecure ? getSecureRandom() : getRandom();
	}

	/**
	 * 获得随机Boolean值
	 *
	 * @return true or false
	 * @since 4.5.9
	 */
	public static boolean randomBoolean() {
		return 0 == randomInt(2);
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 */
	public static int randomInt(int min, int max) {
		return getRandom().nextInt(min, max);
	}

	/**
	 * 获得随机数int值
	 *
	 * @return 随机数
	 */
	public static int randomInt() {
		return getRandom().nextInt();
	}

	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 限制随机数的范围，不包括这个数
	 * @return 随机数
	 */
	public static int randomInt(int limit) {
		return getRandom().nextInt(limit);
	}

	/**
	 * 获得指定范围内的随机数[min, max)
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static long randomLong(long min, long max) {
		return getRandom().nextLong(min, max);
	}

	/**
	 * 获得随机数
	 *
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static long randomLong() {
		return getRandom().nextLong();
	}

	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 限制随机数的范围，不包括这个数
	 * @return 随机数
	 */
	public static long randomLong(long limit) {
		return getRandom().nextLong(limit);
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static double randomDouble(double min, double max) {
		return getRandom().nextDouble(min, max);
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param min          最小数（包含）
	 * @param max          最大数（不包含）
	 * @param scale        保留小数位数
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 随机数
	 * @since 4.0.8
	 */
	public static double randomDouble(double min, double max, int scale, RoundingMode roundingMode) {
		return NumberUtil.round(randomDouble(min, max), scale, roundingMode).doubleValue();
	}

	/**
	 * 获得随机数[0, 1)
	 *
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static double randomDouble() {
		return getRandom().nextDouble();
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param scale        保留小数位数
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 随机数
	 * @since 4.0.8
	 */
	public static double randomDouble(int scale, RoundingMode roundingMode) {
		return NumberUtil.round(randomDouble(), scale, roundingMode).doubleValue();
	}

	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 限制随机数的范围，不包括这个数
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static double randomDouble(double limit) {
		return getRandom().nextDouble(limit);
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param limit        限制随机数的范围，不包括这个数
	 * @param scale        保留小数位数
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 随机数
	 * @since 4.0.8
	 */
	public static double randomDouble(double limit, int scale, RoundingMode roundingMode) {
		return NumberUtil.round(randomDouble(limit), scale, roundingMode).doubleValue();
	}

	/**
	 * 获得指定范围内的随机数[0, 1)
	 *
	 * @return 随机数
	 * @since 4.0.9
	 */
	public static BigDecimal randomBigDecimal() {
		return NumberUtil.toBigDecimal(getRandom().nextDouble());
	}

	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 最大数（不包含）
	 * @return 随机数
	 * @since 4.0.9
	 */
	public static BigDecimal randomBigDecimal(BigDecimal limit) {
		return NumberUtil.toBigDecimal(getRandom().nextDouble(limit.doubleValue()));
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 * @since 4.0.9
	 */
	public static BigDecimal randomBigDecimal(BigDecimal min, BigDecimal max) {
		return NumberUtil.toBigDecimal(getRandom().nextDouble(min.doubleValue(), max.doubleValue()));
	}

	/**
	 * 随机bytes
	 *
	 * @param length 长度
	 * @return bytes
	 */
	public static byte[] randomBytes(int length) {
		byte[] bytes = new byte[length];
		getRandom().nextBytes(bytes);
		return bytes;
	}

	/**
	 * 随机获得列表中的元素
	 *
	 * @param <T>  元素类型
	 * @param list 列表
	 * @return 随机元素
	 */
	public static <T> T randomEle(List<T> list) {
		return randomEle(list, list.size());
	}

	/**
	 * 随机获得列表中的元素
	 *
	 * @param <T>   元素类型
	 * @param list  列表
	 * @param limit 限制列表的前N项
	 * @return 随机元素
	 */
	public static <T> T randomEle(List<T> list, int limit) {
		if (list.size() < limit){
			limit = list.size();
		}
		return list.get(randomInt(limit));
	}

	/**
	 * 随机获得数组中的元素
	 *
	 * @param <T>   元素类型
	 * @param array 列表
	 * @return 随机元素
	 * @since 3.3.0
	 */
	public static <T> T randomEle(T[] array) {
		return randomEle(array, array.length);
	}

	/**
	 * 随机获得数组中的元素
	 *
	 * @param <T>   元素类型
	 * @param array 列表
	 * @param limit 限制列表的前N项
	 * @return 随机元素
	 * @since 3.3.0
	 */
	public static <T> T randomEle(T[] array, int limit) {
		if (array.length < limit){
			limit = array.length;
		}
		return array[randomInt(limit)];
	}

	/**
	 * 随机获得列表中的一定量元素
	 *
	 * @param <T>   元素类型
	 * @param list  列表
	 * @param count 随机取出的个数
	 * @return 随机元素
	 */
	public static <T> List<T> randomEles(List<T> list, int count) {
		final List<T> result = new ArrayList<>(count);
		int limit = list.size();
		while (result.size() < count) {
			result.add(randomEle(list, limit));
		}

		return result;
	}

	/**
	 * 随机获得列表中的一定量的元素，返回List<br>
	 * 此方法与{@link #randomEles(List, int)} 不同点在于，不会获取重复位置的元素
	 *
	 * @param source 列表
	 * @param count  随机取出的个数
	 * @param <T>    元素类型
	 * @return 随机列表
	 * @since 5.2.1
	 */
	public static <T> List<T> randomEleList(List<T> source, int count) {
		if (count >= source.size()) {
			return source;
		}
		final int[] randomList = ArrayUtil.sub(randomInts(source.size()), 0, count);
		List<T> result = new ArrayList<>();
		for (int e : randomList) {
			result.add(source.get(e));
		}
		return result;
	}

	/**
	 * 随机获得列表中的一定量的不重复元素，返回Set
	 *
	 * @param <T>        元素类型
	 * @param collection 列表
	 * @param count      随机取出的个数
	 * @return 随机元素
	 * @throws IllegalArgumentException 需要的长度大于给定集合非重复总数
	 */
	public static <T> Set<T> randomEleSet(Collection<T> collection, int count) {
		final ArrayList<T> source = CollUtil.distinct(collection);
		if (count > source.size()) {
			throw new IllegalArgumentException("Count is larger than collection distinct size !");
		}

		final Set<T> result = new LinkedHashSet<>(count);
		int limit = source.size();
		while (result.size() < count) {
			result.add(randomEle(source, limit));
		}

		return result;
	}

	/**
	 * 创建指定长度的随机索引
	 *
	 * @param length 长度
	 * @return 随机索引
	 * @since 5.2.1
	 */
	public static int[] randomInts(int length) {
		final int[] range = ArrayUtil.range(length);
		for (int i = 0; i < length; i++) {
			int random = randomInt(i, length);
			ArrayUtil.swap(range, i, random);
		}
		return range;
	}

	/**
	 * 获得一个随机的字符串（只包含数字和字符）
	 *
	 * @param length 字符串的长度
	 * @return 随机字符串
	 */
	public static String randomString(int length) {
		return randomString(BASE_CHAR_NUMBER, length);
	}

	/**
	 * 获得一个随机的字符串（只包含数字和大写字符）
	 *
	 * @param length 字符串的长度
	 * @return 随机字符串
	 * @since 4.0.13
	 */
	public static String randomStringUpper(int length) {
		return randomString(BASE_CHAR_NUMBER, length).toUpperCase();
	}

	/**
	 * 获得一个随机的字符串（只包含数字和字符） 并排除指定字符串
	 *
	 * @param length   字符串的长度
	 * @param elemData 要排除的字符串
	 * @return 随机字符串
	 */
	public static String randomStringWithoutStr(int length, String elemData) {
		String baseStr = BASE_CHAR_NUMBER;
		baseStr = StrUtil.removeAll(baseStr, elemData.toCharArray());
		return randomString(baseStr, length);
	}

	/**
	 * 获得一个只包含数字的字符串
	 *
	 * @param length 字符串的长度
	 * @return 随机字符串
	 */
	public static String randomNumbers(int length) {
		return randomString(BASE_NUMBER, length);
	}

	/**
	 * 获得一个随机的字符串
	 *
	 * @param baseString 随机字符选取的样本
	 * @param length     字符串的长度
	 * @return 随机字符串
	 */
	public static String randomString(String baseString, int length) {
		if (StrUtil.isEmpty(baseString)) {
			return StrUtil.EMPTY;
		}
		final StringBuilder sb = new StringBuilder(length);

		if (length < 1) {
			length = 1;
		}
		int baseLength = baseString.length();
		for (int i = 0; i < length; i++) {
			int number = randomInt(baseLength);
			sb.append(baseString.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 随机数字，数字为0~9单个数字
	 *
	 * @return 随机数字字符
	 * @since 3.1.2
	 */
	public static char randomNumber() {
		return randomChar(BASE_NUMBER);
	}

	/**
	 * 随机字母或数字，小写
	 *
	 * @return 随机字符
	 * @since 3.1.2
	 */
	public static char randomChar() {
		return randomChar(BASE_CHAR_NUMBER);
	}

	/**
	 * 随机字符
	 *
	 * @param baseString 随机字符选取的样本
	 * @return 随机字符
	 * @since 3.1.2
	 */
	public static char randomChar(String baseString) {
		return baseString.charAt(randomInt(baseString.length()));
	}

	/**
	 * 生成随机颜色
	 *
	 * @return 随机颜色
	 * @since 4.1.5
	 * @deprecated 使用ImgUtil.randomColor()
	 */
	@Deprecated
	public static Color randomColor() {
		final Random random = getRandom();
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	/**
	 * 带有权重的随机生成器
	 *
	 * @param <T>        随机对象类型
	 * @param weightObjs 带有权重的对象列表
	 * @return {@link WeightRandom}
	 * @since 4.0.3
	 */
	public static <T> WeightRandom<T> weightRandom(WeightObj<T>[] weightObjs) {
		return new WeightRandom<>(weightObjs);
	}

	/**
	 * 带有权重的随机生成器
	 *
	 * @param <T>        随机对象类型
	 * @param weightObjs 带有权重的对象列表
	 * @return {@link WeightRandom}
	 * @since 4.0.3
	 */
	public static <T> WeightRandom<T> weightRandom(Iterable<WeightObj<T>> weightObjs) {
		return new WeightRandom<>(weightObjs);
	}

	// ------------------------------------------------------------------- UUID

	/**
	 * @return 随机UUID
	 * @deprecated 请使用{@link IdUtil#randomUUID()}
	 */
	@Deprecated
	public static String randomUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 简化的UUID，去掉了横线
	 *
	 * @return 简化的UUID，去掉了横线
	 * @since 3.2.2
	 * @deprecated 请使用{@link IdUtil#simpleUUID()}
	 */
	@Deprecated
	public static String simpleUUID() {
		return UUID.randomUUID().toString(true);
	}

	/**
	 * 以当天为基准，随机产生一个日期
	 *
	 * @param min 偏移最小天，可以为负数表示过去的时间（包含）
	 * @param max 偏移最大天，可以为负数表示过去的时间（不包含）
	 * @return 随机日期（随机天，其它时间不变）
	 * @since 4.0.8
	 */
	public static DateTime randomDay(int min, int max) {
		return randomDate(DateUtil.date(), DateField.DAY_OF_YEAR, min, max);
	}

	/**
	 * 以给定日期为基准，随机产生一个日期
	 *
	 * @param baseDate  基准日期
	 * @param dateField 偏移的时间字段，例如时、分、秒等
	 * @param min       偏移最小量，可以为负数表示过去的时间（包含）
	 * @param max       偏移最大量，可以为负数表示过去的时间（不包含）
	 * @return 随机日期
	 * @since 4.5.8
	 */
	public static DateTime randomDate(Date baseDate, DateField dateField, int min, int max) {
		if (null == baseDate) {
			baseDate = DateUtil.date();
		}

		return DateUtil.offset(baseDate, dateField, randomInt(min, max));
	}

}
