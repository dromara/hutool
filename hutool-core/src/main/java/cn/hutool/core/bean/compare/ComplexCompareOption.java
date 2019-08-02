package cn.hutool.core.bean.compare;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 复杂bean比较选项类,可针对不同类型的bean做单独的配置
 * @author dawn
 */
public class ComplexCompareOption implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 比较选项配置类，根据类型从bean中取出选项
	 */
	protected Map<Class, CompareOption> compareOptionMap = new HashMap<>();

	public ComplexCompareOption() {
	}

	public ComplexCompareOption(Map<Class, CompareOption> compareOptionMap) {
		this.compareOptionMap = compareOptionMap;
	}

	public ComplexCompareOption setSimpleCompareOption(Class clazz, CompareOption compareOption){
		compareOptionMap.put(clazz, compareOption);
		return this;
	}

	public Map<Class, CompareOption> getCompareOptionMap() {
		return compareOptionMap;
	}

	public ComplexCompareOption setCompareOptionMap(Map<Class, CompareOption> compareOptionMap) {
		this.compareOptionMap = compareOptionMap;
		return this;
	}
}
