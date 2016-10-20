package com.xiaoleilu.hutool.demo;

import java.util.List;

import com.xiaoleilu.hutool.dfa.SensitiveUtil;
import com.xiaoleilu.hutool.dfa.WordTree;

/**
 * DFA关键词查找Demo
 * @author Looly
 *
 */
public class DfaDemo {
	public static void main(String[] args) {
		//方式1，自定义初始化查找树 
		WordTree tree = new WordTree();
		tree.addWord("关键");
		tree.addWord("你好");
		tree.addWord("你好么");
		tree.addWord("好像");
		tree.addWord("你妹");
		tree.addWord("你大爷");
		tree.addWord("关    闭");
		
		String match = tree.match("是对你好像方的关    键身份是的你大爷方式");
		System.out.println(match);
		List<String> matchAll = tree.matchAll("是对你好像方的关键身份是你大爷的方式");
		System.out.println(matchAll);
		
		//方式2，封装查找树为敏感词工具
		SensitiveUtil.init("你大爷,你妹,你大大,你大舅", false);
		System.out.println(SensitiveUtil.getFindedAllSensitive("去你大爷的"));
	}
}
