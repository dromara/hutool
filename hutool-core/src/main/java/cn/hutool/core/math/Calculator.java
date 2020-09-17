package cn.hutool.core.math;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Stack;

/**
 * 数学表达式计算工具类<br>
 * 见：https://github.com/looly/hutool/issues/1090#issuecomment-693750140
 *
 * @author trainliang, looly
 * @since 5.4.3
 */
public class Calculator {
	private final Stack<String> postfixStack = new Stack<>();// 后缀式栈
	private final Stack<Character> opStack = new Stack<>();// 运算符栈
	private final int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};// 运用运算符ASCII码-40做索引的运算符优先级

	/**
	 * 计算表达式的值
	 *
	 * @param expression 表达式
	 * @return 计算结果
	 */
	public static double conversion(String expression) {
		final Calculator cal = new Calculator();
		expression = transform(expression);
		return cal.calculate(expression);
	}

	/**
	 * 将表达式中负数的符号更改
	 *
	 * @param expression 例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
	 * @return 转换后的字符串
	 */
	private static String transform(String expression) {
		expression = StrUtil.cleanBlank(expression);
		expression = StrUtil.removeSuffix(expression, "=");
		final char[] arr = expression.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == '-') {
				if (i == 0) {
					arr[i] = '~';
				} else {
					char c = arr[i - 1];
					if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
						arr[i] = '~';
					}
				}
			}
		}
		if (arr[0] == '~' || arr[1] == '(') {
			arr[0] = '-';
			return "0" + new String(arr);
		} else {
			return new String(arr);
		}
	}

	/**
	 * 按照给定的表达式计算
	 *
	 * @param expression 要计算的表达式例如:5+12*(3+5)/7
	 * @return 计算结果
	 */
	public double calculate(String expression) {
		Stack<String> resultStack = new Stack<>();
		prepare(expression);
		Collections.reverse(postfixStack);// 将后缀式栈反转
		String firstValue, secondValue, currentValue;// 参与计算的第一个值，第二个值和算术运算符
		while (false == postfixStack.isEmpty()) {
			currentValue = postfixStack.pop();
			if (false == isOperator(currentValue.charAt(0))) {// 如果不是运算符则存入操作数栈中
				currentValue = currentValue.replace("~", "-");
				resultStack.push(currentValue);
			} else {// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
				secondValue = resultStack.pop();
				firstValue = resultStack.pop();

				// 将负数标记符改为负号
				firstValue = firstValue.replace("~", "-");
				secondValue = secondValue.replace("~", "-");

				BigDecimal tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
				resultStack.push(tempResult.toString());
			}
		}
		return Double.parseDouble(resultStack.pop());
	}

	/**
	 * 数据准备阶段将表达式转换成为后缀式栈
	 *
	 * @param expression 表达式
	 */
	private void prepare(String expression) {
		opStack.push(',');// 运算符放入栈底元素逗号，此符号优先级最低
		char[] arr = expression.toCharArray();
		int currentIndex = 0;// 当前字符的位置
		int count = 0;// 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
		char currentOp, peekOp;// 当前操作符和栈顶操作符
		for (int i = 0; i < arr.length; i++) {
			currentOp = arr[i];
			if (isOperator(currentOp)) {// 如果当前字符是运算符
				if (count > 0) {
					postfixStack.push(new String(arr, currentIndex, count));// 取两个运算符之间的数字
				}
				peekOp = opStack.peek();
				if (currentOp == ')') {// 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
					while (opStack.peek() != '(') {
						postfixStack.push(String.valueOf(opStack.pop()));
					}
					opStack.pop();
				} else {
					while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
						postfixStack.push(String.valueOf(opStack.pop()));
						peekOp = opStack.peek();
					}
					opStack.push(currentOp);
				}
				count = 0;
				currentIndex = i + 1;
			} else {
				count++;
			}
		}
		if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
			postfixStack.push(new String(arr, currentIndex, count));
		}

		while (opStack.peek() != ',') {
			postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
		}
	}

	/**
	 * 判断是否为算术符号
	 *
	 * @param c 字符
	 * @return 是否为算术符号
	 */
	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
	}

	/**
	 * 利用ASCII码-40做下标去算术符号优先级
	 *
	 * @param cur  下标
	 * @param peek peek
	 * @return 优先级
	 */
	public boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
		boolean result = false;
		if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
			result = true;
		}
		return result;
	}

	/**
	 * 按照给定的算术运算符做计算
	 *
	 * @param firstValue  第一个值
	 * @param secondValue 第二个值
	 * @param currentOp   算数符，只支持'+'、'-'、'*'、'/'
	 * @return 结果
	 */
	private BigDecimal calculate(String firstValue, String secondValue, char currentOp) {
		BigDecimal result;
		switch (currentOp) {
			case '+':
				result = NumberUtil.add(firstValue, secondValue);
				break;
			case '-':
				result = NumberUtil.sub(firstValue, secondValue);
				break;
			case '*':
				result = NumberUtil.mul(firstValue, secondValue);
				break;
			case '/':
				result = NumberUtil.div(firstValue, secondValue);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + currentOp);
		}
		return result;
	}
}
