package cn.hutool.core.bean.copier;

import java.util.Map;

import cn.hutool.core.map.MapUtil;

/**
 * 属性拷贝选项<br>
 * 包括：<br>
 * 1、限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类<br>
 * 2、是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null<br>
 * 3、忽略的属性列表，设置一个属性列表，不拷贝这些属性值<br>
 * 
 * @author Looly
 */
public class CopyOptions {
	/** 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类 */
	protected Class<?> editable;
	/** 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null */
	protected boolean ignoreNullValue;
	/** 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值 */
	protected String[] ignoreProperties;
	/** 是否忽略字段注入错误 */
	protected boolean ignoreError;
	/** 是否忽略字段大小写 */
	protected boolean ignoreCase;
	/** 拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用 */
	protected Map<String, String> fieldMapping;

	/**
	 * 创建拷贝选项
	 * 
	 * @return 拷贝选项
	 */
	public static CopyOptions create() {
		return new CopyOptions();
	}

	/**
	 * 创建拷贝选项
	 * 
	 * @param editable 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
	 * @param ignoreNullValue 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 * @param ignoreProperties 忽略的属性列表，设置一个属性列表，不拷贝这些属性值
	 * @return 拷贝选项
	 */
	public static CopyOptions create(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
		return new CopyOptions(editable, ignoreNullValue, ignoreProperties);
	}

	/**
	 * 构造拷贝选项
	 */
	public CopyOptions() {
	}

	/**
	 * 构造拷贝选项
	 * 
	 * @param editable 限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
	 * @param ignoreNullValue 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 * @param ignoreProperties 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 */
	public CopyOptions(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
		this.editable = editable;
		this.ignoreNullValue = ignoreNullValue;
		this.ignoreProperties = ignoreProperties;
	}

	/**
	 * 设置限制的类或接口，必须为目标对象的实现接口或父类，用于限制拷贝的属性
	 * 
	 * @param editable 限制的类或接口
	 * @return CopyOptions
	 */
	public CopyOptions setEditable(Class<?> editable) {
		this.editable = editable;
		return this;
	}

	/**
	 * 设置是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 * 
	 * @param ignoreNullVall 是否忽略空值，当源对象的值为null时，true: 忽略而不注入此值，false: 注入null
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreNullValue(boolean ignoreNullVall) {
		this.ignoreNullValue = ignoreNullVall;
		return this;
	}

	/**
	 * 设置忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 * 
	 * @param ignoreProperties 忽略的目标对象中属性列表，设置一个属性列表，不拷贝这些属性值
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreProperties(String... ignoreProperties) {
		this.ignoreProperties = ignoreProperties;
		return this;
	}

	/**
	 * 设置是否忽略字段的注入错误
	 * 
	 * @param ignoreError 是否忽略注入错误
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
		return this;
	}
	
	/**
	 * 设置是否忽略字段的注入错误
	 * 
	 * @param ignoreCase 是否忽略大小写
	 * @return CopyOptions
	 */
	public CopyOptions setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	/**
	 * 设置拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用
	 * 
	 * @param fieldMapping 拷贝属性的字段映射，用于不同的属性之前拷贝做对应表用
	 * @return CopyOptions
	 */
	public CopyOptions setFieldMapping(Map<String, String> fieldMapping) {
		this.fieldMapping = fieldMapping;
		return this;
	}
	
	/**
	 * 获取反转之后的映射
	 * @return 反转映射
	 * @since 4.1.10
	 */
	protected Map<String, String> getReversedMapping() {
		 return (null != this.fieldMapping) ? MapUtil.reverse(this.fieldMapping) : null;
	}
}
