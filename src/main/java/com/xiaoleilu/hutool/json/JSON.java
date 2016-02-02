package com.xiaoleilu.hutool.json;

import java.io.Writer;

/**
 * JSON接口
 * @author Looly
 *
 */
public interface JSON{
	
	/**
	 * 将JSON内容写入Writer，无缩进<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 * 
	 * @param writer Writer
	 * @return Writer
	 * @throws JSONException
	 */
	public Writer write(Writer writer) throws JSONException;
	
	/**
	 * 将JSON内容写入Writer<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 * 
	 * @param writer writer
	 * @param indentFactor 每一级别的缩进量
	 * @param indent 顶级别缩进量
	 * @return Writer
	 * @throws JSONException
	 */
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException;
	
	/**
	 * 转换为JSON字符串
	 * @param indentFactor 每一级别的缩进
	 * @return JSON字符串
	 * @throws JSONException
	 */
	public String toJSONString(int indentFactor) throws JSONException;
}
