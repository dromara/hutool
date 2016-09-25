package com.xiaoleilu.hutool.demo;

import java.util.List;

import com.xiaoleilu.hutool.dfa.WordTree;

public class DfaDemo {
	public static void main(String[] args) {
		WordTree tree = new WordTree();
		tree.addWord("关键");
		tree.addWord("你好");
		tree.addWord("你好么");
		tree.addWord("好像");
		tree.addWord("你妹");
		tree.addWord("关    闭");
		
		System.out.println(tree);
		
		String match = tree.match("是对你好像方的关    键身份是的方式");
		System.out.println(match);
		
		List<String> matchAll = tree.matchAll("是对你好像方的关键身份是的方式");
		System.out.println(matchAll);
	}
}
