package cn.hutool.core.math;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrUtil;

import java.math.BigInteger;
import java.util.List;

/**
 * 数学相关方法工具类<br>
 * 此工具类与{@link NumberUtil}属于一类工具，NumberUtil偏向于简单数学计算的封装，MathUtil偏向复杂数学计算
 *
 * @author looly
 * @since 4.0.7
 */
public class MathUtil {

	/**
	 * 0-20对应的阶乘，超过20的阶乘会超过Long.MAX_VALUE
	 */
	private static final long[] FACTORIALS = new long[]{
			1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L,
			87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L,
			2432902008176640000L};

	//--------------------------------------------------------------------------------------------- Arrangement
	/**
	 * 计算排列数，即A(n, m) = n!/(n-m)!
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 排列数
	 */
	public static long arrangementCount(final int n, final int m) {
		return Arrangement.count(n, m);
	}

	/**
	 * 计算排列数，即A(n, n) = n!
	 *
	 * @param n 总数
	 * @return 排列数
	 */
	public static long arrangementCount(final int n) {
		return Arrangement.count(n);
	}

	/**
	 * 排列选择（从列表中选择n个排列）
	 *
	 * @param datas 待选列表
	 * @param m 选择个数
	 * @return 所有排列列表
	 */
	public static List<String[]> arrangementSelect(final String[] datas, final int m) {
		return new Arrangement(datas).select(m);
	}

	/**
	 * 全排列选择（列表全部参与排列）
	 *
	 * @param datas 待选列表
	 * @return 所有排列列表
	 */
	public static List<String[]> arrangementSelect(final String[] datas) {
		return new Arrangement(datas).select();
	}

	//--------------------------------------------------------------------------------------------- Combination
	/**
	 * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
	 *
	 * @param n 总数
	 * @param m 选择的个数
	 * @return 组合数
	 */
	public static long combinationCount(final int n, final int m) {
		return Combination.count(n, m);
	}

	/**
	 * 组合选择（从列表中选择n个组合）
	 *
	 * @param datas 待选列表
	 * @param m 选择个数
	 * @return 所有组合列表
	 */
	public static List<String[]> combinationSelect(final String[] datas, final int m) {
		return new Combination(datas).select(m);
	}

	/**
	 * 金额元转换为分
	 *
	 * @param yuan 金额，单位元
	 * @return 金额，单位分
	 * @since 5.7.11
	 */
	public static long yuanToCent(final double yuan) {
		return new Money(yuan).getCent();
	}

	/**
	 * 金额分转换为元
	 *
	 * @param cent 金额，单位分
	 * @return 金额，单位元
	 * @since 5.7.11
	 */
	public static double centToYuan(final long cent) {
		final long yuan = cent / 100;
		final int centPart = (int) (cent % 100);
		return new Money(yuan, centPart).getAmount().doubleValue();
	}

	/**
	 * 计算阶乘
	 * <p>
	 * n! = n * (n-1) * ... * 2 * 1
	 * </p>
	 *
	 * @param n 阶乘起始
	 * @return 结果
	 * @since 5.6.0
	 */
	public static BigInteger factorial(final BigInteger n) {
		if (n.equals(BigInteger.ZERO)) {
			return BigInteger.ONE;
		}
		return factorial(n, BigInteger.ZERO);
	}

	/**
	 * 计算范围阶乘
	 * <p>
	 * factorial(start, end) = start * (start - 1) * ... * (end + 1)
	 * </p>
	 *
	 * @param start 阶乘起始（包含）
	 * @param end   阶乘结束，必须小于起始（不包括）
	 * @return 结果
	 * @since 5.6.0
	 */
	public static BigInteger factorial(BigInteger start, BigInteger end) {
		Assert.notNull(start, "Factorial start must be not null!");
		Assert.notNull(end, "Factorial end must be not null!");
		if (start.compareTo(BigInteger.ZERO) < 0 || end.compareTo(BigInteger.ZERO) < 0) {
			throw new IllegalArgumentException(StrUtil.format("Factorial start and end both must be > 0, but got start={}, end={}", start, end));
		}

		if (start.equals(BigInteger.ZERO)) {
			start = BigInteger.ONE;
		}

		if (end.compareTo(BigInteger.ONE) < 0) {
			end = BigInteger.ONE;
		}

		BigInteger result = start;
		end = end.add(BigInteger.ONE);
		while (start.compareTo(end) > 0) {
			start = start.subtract(BigInteger.ONE);
			result = result.multiply(start);
		}
		return result;
	}

	/**
	 * 计算范围阶乘
	 * <p>
	 * factorial(start, end) = start * (start - 1) * ... * (end + 1)
	 * </p>
	 *
	 * @param start 阶乘起始（包含）
	 * @param end   阶乘结束，必须小于起始（不包括）
	 * @return 结果
	 * @since 4.1.0
	 */
	public static long factorial(final long start, final long end) {
		// 负数没有阶乘
		if (start < 0 || end < 0) {
			throw new IllegalArgumentException(StrUtil.format("Factorial start and end both must be >= 0, but got start={}, end={}", start, end));
		}
		if (0L == start || start == end) {
			return 1L;
		}
		if (start < end) {
			return 0L;
		}
		return factorialMultiplyAndCheck(start, factorial(start - 1, end));
	}

	/**
	 * 计算范围阶乘中校验中间的计算是否存在溢出，factorial提前做了负数和0的校验，因此这里没有校验数字的正负
	 *
	 * @param a 乘数
	 * @param b 被乘数
	 * @return 如果 a * b的结果没有溢出直接返回，否则抛出异常
	 */
	private static long factorialMultiplyAndCheck(final long a, final long b) {
		if (a <= Long.MAX_VALUE / b) {
			return a * b;
		}
		throw new IllegalArgumentException(StrUtil.format("Overflow in multiplication: {} * {}", a, b));
	}

	/**
	 * 计算阶乘
	 * <p>
	 * n! = n * (n-1) * ... * 2 * 1
	 * </p>
	 *
	 * @param n 阶乘起始
	 * @return 结果
	 */
	public static long factorial(final long n) {
		if (n < 0 || n > 20) {
			throw new IllegalArgumentException(StrUtil.format("Factorial must have n >= 0 and n <= 20 for n!, but got n = {}", n));
		}
		return FACTORIALS[(int) n];
	}

	/**
	 * 平方根算法<br>
	 * 推荐使用 {@link Math#sqrt(double)}
	 *
	 * @param x 值
	 * @return 平方根
	 */
	public static long sqrt(long x) {
		long y = 0;
		long b = (~Long.MAX_VALUE) >>> 1;
		while (b > 0) {
			if (x >= y + b) {
				x -= y + b;
				y >>= 1;
				y += b;
			} else {
				y >>= 1;
			}
			b >>= 2;
		}
		return y;
	}

	/**
	 * 可以用于计算双色球、大乐透注数的方法<br>
	 * 比如大乐透35选5可以这样调用processMultiple(7,5); 就是数学中的：C75=7*6/2*1
	 *
	 * @param selectNum 选中小球个数
	 * @param minNum    最少要选中多少个小球
	 * @return 注数
	 */
	public static int processMultiple(final int selectNum, final int minNum) {
		final int result;
		result = mathSubNode(selectNum, minNum) / mathNode(selectNum - minNum);
		return result;
	}

	/**
	 * 最大公约数
	 *
	 * @param m 第一个值
	 * @param n 第二个值
	 * @return 最大公约数
	 */
	public static int divisor(int m, int n) {
		while (m % n != 0) {
			final int temp = m % n;
			m = n;
			n = temp;
		}
		return n;
	}

	/**
	 * 最小公倍数
	 *
	 * @param m 第一个值
	 * @param n 第二个值
	 * @return 最小公倍数
	 */
	public static int multiple(final int m, final int n) {
		return m * n / divisor(m, n);
	}

	private static int mathSubNode(final int selectNum, final int minNum) {
		if (selectNum == minNum) {
			return 1;
		} else {
			return selectNum * mathSubNode(selectNum - 1, minNum);
		}
	}

	private static int mathNode(final int selectNum) {
		if (selectNum == 0) {
			return 1;
		} else {
			return selectNum * mathNode(selectNum - 1);
		}
	}
}
