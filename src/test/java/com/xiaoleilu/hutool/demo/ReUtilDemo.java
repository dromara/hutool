package com.xiaoleilu.hutool.demo;

import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.ReUtil;
import com.xiaoleilu.hutool.Validator;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * 正则表达式工具类Demo
 * @author Looly
 *
 */
public class ReUtilDemo {
	private final static Log log = StaticLog.get();
	
	public static void main(String[] args) {
		String content = "ZZZaaabbbccc中文1234";
		
		//get demo 正则查找匹配的第一个字符串
		String resultGet = ReUtil.get("\\w{2}", content, 0);
		log.debug("get: {}", resultGet);
		
		log.debug("---------------------------------------------------------");
		
		//抽取多个分组然后把它们拼接起来
		String resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
		log.debug("extractMulti: {}", resultExtractMulti);
		
		log.debug("---------------------------------------------------------");
		
		//抽取多个分组然后把原文匹配到位置之前的内容都删除
		String[] contents = new String[]{content};
		String resultExtractMultiAndDelPre = ReUtil.extractMultiAndDelPre("(\\w)aa(\\w)", contents, "$1-$2");
		log.debug("extractMultiAndDelPre: content: {}, extract: {}", contents[0], resultExtractMultiAndDelPre);
		
		log.debug("---------------------------------------------------------");
		
		//删除第一个匹配到的内容
		String resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
		log.debug("delFirst: {}", resultDelFirst);
		
		log.debug("---------------------------------------------------------");
		
		//删除第一个匹配到的内容以及之前的文本
		String resultDelPre = ReUtil.delPre("(\\w)aa(\\w)", content);
		log.debug("delPre: {}", resultDelPre);
		
		log.debug("---------------------------------------------------------");
		
		//查找所有匹配文本
		List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<String>());
		log.debug("findAll: {}", resultFindAll);
		
		log.debug("---------------------------------------------------------");
		
		//找到匹配的第一个数字
		Integer resultGetFirstNumber= ReUtil.getFirstNumber(content);
		log.debug("getFirstNumber: {}", resultGetFirstNumber);
		
		log.debug("---------------------------------------------------------");
		
		//格式是否符合Ipv4格式
		log.debug("isIpv4: {}", Validator.isIpv4("127.0.0.1"));
		
		log.debug("---------------------------------------------------------");
		
		//给定字符串是否匹配给定正则
		log.debug("isMatch: {}", ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content));
		
		log.debug("---------------------------------------------------------");
		
		//通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
		log.debug("replaceAll: {}", ReUtil.replaceAll(content, "(\\d+)", "->$1<-"));
		
		log.debug("---------------------------------------------------------");
		
		//转义给定字符串，为正则相关的特殊符号转义
		log.debug("replaceAll: {}", ReUtil.escape("我有个$符号{}"));
		
		log.debug("---------------------------------------------------------");
	}
}
