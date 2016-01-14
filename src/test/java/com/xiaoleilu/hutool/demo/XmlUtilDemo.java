package com.xiaoleilu.hutool.demo;

import org.w3c.dom.Document;

import com.xiaoleilu.hutool.util.XmlUtil;

public class XmlUtilDemo {
	public static void main(String[] args) {
		Document doc = XmlUtil.createXml("root");
		System.out.println(XmlUtil.toStr(doc, "gbk"));
	}
}
