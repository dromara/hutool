package cn.hutool.core.lang;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 控制台打印表格工具
 *
 * @author 孙宇
 */
public class ConsoleTableUtil {
	/**
	 * 每列最大字符个数
	 */
	private List<Integer> columnCharNumber;
	/**
	 * 表格头信息
	 */
	private final List<List<String>> HEADER_LIST = new ArrayList<>();
	/**
	 * 表格体信息
	 */
	private final List<List<String>> BODY_LIST = new ArrayList<>();

	/**
	 * 添加头信息
	 *
	 * @param columns 列名
	 * @return 自身对象
	 */
	public ConsoleTableUtil addHeader(String... columns) {
		columnCharNumber = new ArrayList<>(Collections.nCopies(columns.length, 0));
		List<String> l = new ArrayList<>();
		HEADER_LIST.add(l);
		for (int i = 0; i < columns.length; i++) {
			String column = columns[i];
			String col = Convert.toSBC(column);
			l.add(col);
			int width = col.length();
			columnCharNumber.set(i, width);
		}
		return this;
	}

	/**
	 * 添加体信息
	 *
	 * @param values 列值
	 * @return 自身对象
	 */
	public ConsoleTableUtil addBody(String... values) {
		List<String> l = new ArrayList<>();
		BODY_LIST.add(l);
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			String val = Convert.toSBC(value);
			l.add(val);
			int width = val.length();
			if (width > columnCharNumber.get(i)) {
				columnCharNumber.set(i, width);
			}
		}
		return this;
	}

	/**
	 * 获取表格字符串
	 *
	 * @return 表格字符串
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		border(sb);
		for (List<String> headers : HEADER_LIST) {
			for (int i = 0; i < headers.size(); i++) {
				if (i == 0) {
					sb.append('|');
				}
				String header = headers.get(i);
				sb.append(Convert.toSBC(" "));
				sb.append(header);
				sb.append(Convert.toSBC(" "));
				int l = header.length();
				int lw = columnCharNumber.get(i);
				if (lw > l) {
					for (int j = 0; j < (lw - l); j++) {
						sb.append(Convert.toSBC(" "));
					}
				}
				sb.append('|');
			}
			sb.append('\n');
		}
		border(sb);
		for (List<String> bodys : BODY_LIST) {
			for (int i = 0; i < bodys.size(); i++) {
				if (i == 0) {
					sb.append('|');
				}
				String body = bodys.get(i);
				sb.append(Convert.toSBC(" "));
				sb.append(body);
				sb.append(Convert.toSBC(" "));
				int l = body.length();
				int lw = columnCharNumber.get(i);
				if (lw > l) {
					for (int j = 0; j < (lw - l); j++) {
						sb.append(Convert.toSBC(" "));
					}
				}
				sb.append('|');
			}
			sb.append('\n');
		}
		border(sb);
		return sb.toString();
	}

	private void border(StringBuilder sb) {
		sb.append('*');
		for (Integer width : columnCharNumber) {
			sb.append(Convert.toSBC(StrUtil.fillAfter("", '-', width + 2)));
			sb.append('*');
		}
		sb.append('\n');
	}

	public void print() {
		Console.print(toString());
	}

	public static void main(String[] args) {
		ConsoleTableUtil t = new ConsoleTableUtil();
		t.addHeader("姓名", "年龄");
		t.addBody("张三", "15");
		t.addBody("李四", "29");
		t.addBody("王二麻子", "37");
		t.print();

		t = new ConsoleTableUtil();
		t.addHeader("体温", "占比");
		t.addHeader("℃", "%");
		t.addBody("36.8", "10");
		t.addBody("37", "5");
		t.print();

		t = new ConsoleTableUtil();
		t.addHeader("标题1", "标题2");
		t.addBody("12345", "混合321654asdfcSDF");
		t.addBody("sd   e3ee  ff22", "ff值");
		t.print();
	}

}