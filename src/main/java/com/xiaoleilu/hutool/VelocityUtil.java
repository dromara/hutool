package com.xiaoleilu.hutool;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.xiaoleilu.hutool.exceptions.NotInitedException;
import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * Velocity模板引擎工具类<br>
 * 使用前必须初始化工具类
 * @author xiaoleilu
 * 
 */
public class VelocityUtil {
	
	/** 是否初始化了默认引擎 */
	private static boolean isInited;
	
	/**
	 * 初始化Velocity全局属性
	 * @param templateDir 模板所在目录，绝对路径
	 */
	synchronized public static void init(String templateDir, String charset) {
		Velocity.init(_newInitedProp(templateDir, charset));
		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);	//使用缓存
		isInited = true;	//标记完成初始化
	}
	
	/**
	 * 新建Velocity模板引擎
	 * @param templateDir 模板所在目录，绝对路径
	 * @return VelocityEngine
	 */
	public static VelocityEngine newEngine(String templateDir, String charset) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);	//使用缓存
		
		ve.init(_newInitedProp(templateDir, charset));
		
		return ve;
	}

	/**
	 * 获得指定模板填充后的内容
	 * 
	 * @param templateDir 模板所在目录，绝对路径
	 * @param templateFileName 模板名称
	 * @param context 上下文（变量值的容器）
	 * @param charset 字符集
	 * @return 模板和内容匹配后的内容
	 */
	public static String getContent(String templateDir, String templateFileName, VelocityContext context, String charset) {
		// 初始化模板引擎
		final VelocityEngine ve = newEngine(templateDir, charset);

		return getContent(ve, templateFileName, context);
	}
	
	/**
	 * 获得指定模板填充后的内容
	 * 
	 * @param ve 模板引擎
	 * @param templateFileName 模板名称
	 * @param context 上下文（变量值的容器）
	 * @return 模板和内容匹配后的内容
	 */
	public static String getContent(VelocityEngine ve, String templateFileName, VelocityContext context) {
		final StringWriter writer = new StringWriter();	//StringWriter不需要关闭
		toWriter(ve, templateFileName, context, writer);
		return writer.toString();
	}
	
	/**
	 * 获得指定模板填充后的内容，使用默认引擎
	 * @param templateFileName 模板文件
	 * @param context 上下文（变量值的容器）
	 * @return 模板和内容匹配后的内容
	 */
	public static String getContent(String templateFileName, VelocityContext context) {
		final StringWriter writer = new StringWriter();	//StringWriter不需要关闭
		toWriter(templateFileName, context, writer);
		return writer.toString();
	}

	/**
	 * 生成文件
	 * @param ve 模板引擎
	 * @param templateFileName 模板文件名
	 * @param context 上下文
	 * @param destPath 目标路径（绝对）
	 */
	public static void toFile(VelocityEngine ve, String templateFileName, VelocityContext context, String destPath) {
		toFile(ve.getTemplate(templateFileName), context, destPath);
	}
	
	/**
	 * 生成文件，使用默认引擎
	 * @param templateFileName 模板文件名
	 * @param context 模板上下文
	 * @param destPath 目标路径（绝对）
	 */
	public static void toFile(String templateFileName, VelocityContext context, String destPath){
		assertInit();
		
		toFile(Velocity.getTemplate(templateFileName), context, destPath);
	}
	
	/**
	 * 生成文件
	 * @param template 模板
	 * @param context 模板上下文
	 * @param destPath 目标路径（绝对）
	 */
	public static void toFile(Template template, VelocityContext context, String destPath) {
		PrintWriter writer = null;
		try {
			writer = FileUtil.getPrintWriter(destPath, Velocity.getProperty(Velocity.OUTPUT_ENCODING).toString(), false);
			template.merge(context, writer);
		} catch (IOException e) {
			throw new UtilException(StrUtil.format("Write Velocity content to [{}] error!", destPath), e);
		}finally {
			FileUtil.close(writer);
		}
	}
	
	/**
	 * 生成内容写入流<br>
	 * 会自动关闭Writer
	 * @param ve 引擎
	 * @param templateFileName 模板文件名
	 * @param context 上下文
	 * @param writer 流
	 */
	public static void toWriter(VelocityEngine ve, String templateFileName, VelocityContext context, Writer writer) {
		final Template template = ve.getTemplate(templateFileName);
		template.merge(context, writer);
	}
	
	/**
	 * 生成内容写入流<br>
	 * 会自动关闭Writer
	 * @param templateFileName 模板文件名
	 * @param context 上下文
	 * @param writer 流
	 */
	public static void toWriter(String templateFileName, VelocityContext context, Writer writer) {
		assertInit();
		
		final Template template = Velocity.getTemplate(templateFileName);
		template.merge(context, writer);
	}
	
	/**
	 * 融合模板和内容
	 * @param templateContent 模板的内容字符串
	 * @param context 上下文
	 * @return 模板和内容匹配后的内容
	 */
	public static String merge(String templateContent, VelocityContext context) {
		final StringWriter writer = new StringWriter();
		try {
			Velocity.evaluate(context, writer, RandomUtil.randomUUID(), templateContent);
		} catch (Exception e) {
			throw new UtilException(e);
		}
		return writer.toString();
	}
	
	/**
	 * 生成内容写到响应内容中<br>
	 * 模板的变量来自于Request的Attribute对象
	 * @param templateFileName 模板文件
	 * @param request 请求对象，用于获取模板中的变量值
	 * @param response 响应对象
	 */
	public static void toWriter(String templateFileName, HttpServletRequest request, HttpServletResponse response) {
		final VelocityContext context = parseRequest(request);
		Writer writer = null;
		try {
			writer = response.getWriter();
			toWriter(templateFileName, context, writer);
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Write Velocity content template by [{}] to response error!", templateFileName), e);
		}finally {
			FileUtil.close(writer);
		}
	}
	
	/**
	 * 将Request中的数据转换为模板引擎<br>
	 * 取值包括Session和Request
	 * @param request 请求对象
	 * @return 模板引擎
	 */
	public static VelocityContext parseRequest(HttpServletRequest request) {
		VelocityContext context = new VelocityContext();
		
		//将Session中的值放入模板上下文
		final Enumeration<String> sessionAttrs = request.getSession().getAttributeNames();
		if(sessionAttrs != null) {
			String attrName = null;
			while (sessionAttrs.hasMoreElements()) {
				attrName = sessionAttrs.nextElement();
				context.put(attrName, request.getAttribute(attrName));
			}
		}
		
		//将Request中的值放入模板上下文，如果和Session冲突，则覆盖之
		final Enumeration<String> attrs = request.getAttributeNames();
		if(sessionAttrs != null) {
			String attrName = null;
			while (attrs.hasMoreElements()) {
				attrName = attrs.nextElement();
				context.put(attrName, request.getAttribute(attrName));
			}
		}
		
		return context;
	}
	
	//-------------------------------------------------------------------------- Private method start
	/**
	 * 新建一个初始化后的属性对象
	 * @param templateDir 模板所在目录
	 * @return 初始化后的属性对象
	 */
	private static Properties _newInitedProp(String templateDir, String charset){
		final Properties properties = new Properties();
		
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateDir);
		properties.setProperty(Velocity.ENCODING_DEFAULT, charset); 
		properties.setProperty(Velocity.INPUT_ENCODING, charset); 
		properties.setProperty(Velocity.OUTPUT_ENCODING, charset);
		
		return properties;
	}
	
	/**
	 * 断言是否初始化默认引擎，若未初始化抛出 异常
	 */
	private static void assertInit() {
		if(! isInited) throw new NotInitedException("Please use VelocityUtil.init() method to init Velocity default engine!");
	}
	//-------------------------------------------------------------------------- Private method end
}
