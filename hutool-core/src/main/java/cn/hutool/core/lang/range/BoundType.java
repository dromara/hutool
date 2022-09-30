package cn.hutool.core.lang.range;

/**
 * 边界类型
 *
 * @author huangchengxing
 * @since 6.0.0
 */
public enum BoundType {

	/**
	 * 表示一个左闭区间，等同于{@code {x | x >= a}}
	 */
	CLOSE_LOWER_BOUND("[", ">=", -2),

	/**
	 * 表示一个左开区间，等同于{@code {x | x > a}}
	 */
	OPEN_LOWER_BOUND("(", ">", -1),

	/**
	 * 表示一个右开区间，等同于{@code {x | x < a}}
	 */
	OPEN_UPPER_BOUND(")", "<", 1),

	/**
	 * 表示一个右闭区间，等同于{@code {x | x <= a}}
	 */
	CLOSE_UPPER_BOUND("]", "<=", 2);

	/**
	 * 符号
	 */
	private final String symbol;

	/**
	 * 运算符
	 */
	private final String operator;

	/**
	 * 是否为开区间
	 */
	private final int code;

	/**
	 * 构造
	 */
	BoundType(final String symbol, final String operator, final int code) {
		this.symbol = symbol;
		this.operator = operator;
		this.code = code;
	}

	/**
	 * 获取符号
	 *
	 * @return 符号
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * 获取code
	 *
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 获取运算符
	 *
	 * @return 运算符
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 该边界类型是否与当前边界错位，即一个的左边界，一个是右边界
	 *
	 * @param boundType 另一边界类型
	 * @return 是否
	 */
	public boolean isDislocated(BoundType boundType) {
		return code * boundType.code < 0;
	}

	/**
	 * 是下界
	 *
	 * @return 是否
	 */
	public boolean isLowerBound() {
		return code < 0;
	}

	/**
	 * 是上界
	 *
	 * @return 是否
	 */
	public boolean isUpperBound() {
		return code > 0;
	}

	/**
	 * 是闭区间
	 *
	 * @return 是否
	 */
	public boolean isClose() {
		return (code & 1) == 0;
	}

	/**
	 * 是开区间
	 *
	 * @return 是否
	 */
	public boolean isOpen() {
		return (code & 1) == 1;
	}

	/**
	 * 对边界类型取反
	 *
	 * @return 取反后的边界类型
	 */
	public BoundType negate() {
		if (isLowerBound()) {
			return isOpen() ? CLOSE_UPPER_BOUND : OPEN_UPPER_BOUND;
		}
		return isOpen() ? CLOSE_LOWER_BOUND : OPEN_LOWER_BOUND;
	}

}
