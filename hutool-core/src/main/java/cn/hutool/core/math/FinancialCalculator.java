package cn.hutool.core.math;

/**
 * 金融计算器
 *
 * @author yhkang
 * @since 2021/9/22
 */
public class FinancialCalculator {
	private static final double FINANCIAL_PRECISION = 0.0000001;
	private static final int FINANCIAL_MAX_ITERATIONS = 20;

	/**
	 * 计算实际利率
	 * 参考 https://support.microsoft.com/zh-cn/office/rate-%E5%87%BD%E6%95%B0-9f665657-4a7e-4bb7-a030-83fc59e748ce
	 *
	 * @param nper 期数
	 * @param pmt  月供,通常包括本金和利息
	 * @param pv   现值,即一系列未来付款当前值的总和
	 * @return 每期利率
	 */
	public static double rate(int nper, double pmt, double pv) {
		return rate(nper, pmt, pv, 0, 0, 0.1);
	}

	/**
	 * 计算实际利率
	 * 参考 https://support.microsoft.com/zh-cn/office/rate-%E5%87%BD%E6%95%B0-9f665657-4a7e-4bb7-a030-83fc59e748ce
	 *
	 * @param nper  期数
	 * @param pmt   月供,通常包括本金和利息
	 * @param pv    现值,即一系列未来付款当前值的总和
	 * @param fv    未来值，或在最后一次付款后希望得到的现金余额
	 * @param type  先付：0，后付：1
	 * @param guess 预期利率（估计值）
	 * @return 每期利率
	 */
	public static double rate(int nper, double pmt, double pv, double fv, int type, double guess) {
		return rate(nper, pmt, pv, fv, type, guess, FINANCIAL_PRECISION, FINANCIAL_MAX_ITERATIONS);
	}

	/**
	 * 计算实际利率
	 * 参考 https://support.microsoft.com/zh-cn/office/rate-%E5%87%BD%E6%95%B0-9f665657-4a7e-4bb7-a030-83fc59e748ce
	 *
	 * @param nper          期数
	 * @param pmt           月供,通常包括本金和利息
	 * @param pv            现值,即一系列未来付款当前值的总和
	 * @param fv            未来值，或在最后一次付款后希望得到的现金余额
	 * @param type          先付：0，后付：1
	 * @param guess         预期利率（估计值）
	 * @param precision     收敛精度
	 * @param maxIterations 最大迭代次数
	 * @return 每期利率
	 */
	public static double rate(int nper, double pmt, double pv, double fv, int type, double guess, double precision, int maxIterations) {
		double rn = guess;
		int count = 0;
		while (count < maxIterations && Math.abs(nper - rn) > precision) {
			double rnp1 = rn - calcRate(rn, pv, nper, fv, pmt, type);
			count++;
			rn = rnp1;
		}
		return rn;
	}

	private static double calcRate(double rate, double pv, int nper, double fv, double pmt, int type) {
		double t1 = Math.pow(rate + 1, nper);
		double t2 = Math.pow(rate + 1, nper - 1);
		return (fv + pv * t1 + pmt * (1 + rate * type) * (t1 - 1) / rate) /
				(pv * nper * t2 - pmt * (1 + rate * type) * (t1 - 1) / Math.pow(rate, 2)
						+ pmt * (1 + rate * type) * nper * t2 / rate
						+ pmt * type * (t1 - 1) / rate);
	}

	public static void main(String[] args) {
		System.out.println(rate(24, 17018.59, -400000) * 12);
		System.out.println(rate(12, 43200, -480000) * 12);
	}
}
