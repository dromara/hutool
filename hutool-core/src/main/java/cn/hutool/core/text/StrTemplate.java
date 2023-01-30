package cn.hutool.core.text;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 常用参数替换与解析方法
 *
 * @author ZhuBobi
 * @since 5.8.12
 */
public class StrTemplate {

	/**
	 * 解析或替换变量的开始标志
	 **/
	private static String DEFAULT_PREFIX = "{";

	/**
	 * 解析或替换变量的结束标志
	 **/
	private static String DEFAULT_SUFFIX = "}";

	/**
	 * 默认构造方式：使用 { } 来标记变量：如/{param1}/hello
	 **/
	private static StrTemplate template = getInstance();

	/**
	 * 私有构造方法，只能通过 getInstance 方法来获取实例
	 **/
	private StrTemplate() {
	}

	/**
	 * 私有构造方法，只能通过 getInstance 方法来获取实例
	 *
	 * @param prefix 参数起始标志前缀
	 * @param suffix 参数结束标志后缀
	 */
	private StrTemplate(String prefix, String suffix) {
		DEFAULT_PREFIX = prefix;
		DEFAULT_SUFFIX = suffix;
	}

	/**
	 * 通过 getInstance 方法来获取实例
	 *
	 * @param prefix 参数起始标志前缀
	 * @param suffix 参数结束标志后缀
	 */
	public synchronized static StrTemplate getInstance(String prefix, String suffix) {
		if (null == template) {
			template = new StrTemplate(prefix, suffix);
		} else if (!DEFAULT_PREFIX.equals(prefix)) {
			DEFAULT_PREFIX = prefix;
			DEFAULT_SUFFIX = suffix;
		}
		return template;
	}


	/**
	 * 通过 getInstance 方法来获取实例，不带参数即默认使用 {  } 作为参数标志
	 */
	public synchronized static StrTemplate getInstance() {
		if (null == template) {
			template = new StrTemplate();
		}
		return template;
	}

	/**
	 * 格式化文本，使用 {index} 占位, 按照下标进行替换<br>
	 * 例如：<br>
	 * template: I say {0} {1} to the {1} 或 I say {} {} to the {}<br>
	 * args: ["hello","world"]<br>
	 *
	 * @param template   文本模板，被替换的部分用 {index} 表示
	 * @param args       参数数组
	 * @return 格式化后的文本
	 * @since 5.8.12
	 */
	public String format(CharSequence template, Object... args) {
		if (null == template || template.length() == 0) {
			return null;
		}

		String templateStr = template.toString();
		int tE = 0;
		int index = 0;
		while (true) {
			int tS = templateStr.indexOf(DEFAULT_PREFIX, tE);
			if (tS == -1) {
				break;
			}
			tE = templateStr.indexOf(DEFAULT_SUFFIX, tS);
			String substring = templateStr.substring(tS + DEFAULT_PREFIX.length(), tE);
			if(StrUtil.isNotEmpty(substring)){
				index = Integer.parseInt(substring);
			}

			String patton = templateStr.substring(tS, tE + DEFAULT_SUFFIX.length());
			templateStr = StrUtil.replace(templateStr, patton, args[index].toString());
			tE = tS + args[index].toString().length();
			index++;

		}
		return templateStr;
	}

	/**
	 * 格式化文本，使用 {varName} 占位<br>
	 * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
	 *
	 * @param template   文本模板，被替换的部分用 {key} 表示
	 * @param map        参数值对
	 * @param ignoreNull 是否忽略 {@code null} 值，忽略则 {@code null} 值对应的变量不被替换，否则替换为""
	 * @return 格式化后的文本
	 * @since 5.8.12
	 */
	public String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {

		if (null == template || template.length() == 0) {
			return null;
		}

		String templateStr = template.toString();
		int tE = 0;
		while (true) {
			int tS = templateStr.indexOf(DEFAULT_PREFIX, tE);
			if (tS == -1) {
				break;
			}
			tE = templateStr.indexOf(DEFAULT_SUFFIX, tS);
			String name = templateStr.substring(tS + DEFAULT_PREFIX.length(), tE);
			String patton = templateStr.substring(tS, tE + DEFAULT_SUFFIX.length());
			Object value = map.get(name);
			if(ignoreNull && null == value ){
				value = "";
			}
			if(!ignoreNull && null == value){
				tE = tS + templateStr.length();
				continue;
			}
			templateStr = StrUtil.replace(templateStr, patton, value.toString());
			tE = tS + value.toString().length();

		}
		return templateStr;
	}


	/**
	 * 解析格式化后的参数，使用 {@link StrTemplate#DEFAULT_PREFIX} 与 {@link StrTemplate#DEFAULT_SUFFIX} 内字符串 作为参数名<br>
	 * 将格式化后的字符串按照文本模板解析对应的参数<br>
	 * 例：<br>
	 * 通常使用：<br>
	 * template: siot/sys/{productKey}/{deviceKey}/property/{get}<br>
	 * formatStr: siot/sys/11/22/property/33<br>
	 *
	 * @param template  文本模板，参数名为 {@link StrTemplate#DEFAULT_PREFIX} 与 {@link StrTemplate#DEFAULT_SUFFIX} 内字符串
	 * @param formatStr 已经完成了字符串参数替换后的文本
	 * @return 解析对应的参数Map
	 * @since 5.8.12
	 */
	public Map<String, String> parse(String template, String formatStr) {

		Map<String, String> result = new HashMap<>();
		int indexT = 0;
		int indexF = 0;
		int fS;
		while (true) {
			int tS = template.indexOf(DEFAULT_PREFIX, indexT);
			if (tS == -1) {
				break;
			}
			int tE = template.indexOf(DEFAULT_SUFFIX, tS);
			if (tE == template.length() - DEFAULT_SUFFIX.length()) {
				int i = formatStr.lastIndexOf(template.charAt(tS - 1));
				result.put(template.substring(tS + DEFAULT_PREFIX.length(), tE), formatStr.substring(i + 1));
				break;
			}

			if (indexF == 0) {
				fS = tS;
			} else {
				fS = indexF + tS - indexT - DEFAULT_SUFFIX.length();
			}
			int fE = formatStr.indexOf(template.charAt(tE + DEFAULT_SUFFIX.length()), fS);
			result.put(template.substring(tS + DEFAULT_PREFIX.length(), tE), formatStr.substring(fS, fE));
			indexF = fE;
			indexT = tE;
		}
		return result;
	}


}
