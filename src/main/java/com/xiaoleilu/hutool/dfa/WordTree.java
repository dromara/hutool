package com.xiaoleilu.hutool.dfa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * DFA（Deterministic Finite Automaton 确定有穷自动机）
 * DFA单词树（以下简称单词树），常用于在某大段文字中快速查找某几个关键词是否存在。<br>
 * 单词树使用group区分不同的关键字集合，不同的分组可以共享树枝，避免重复建树。<br>
 * 单词树使用树状结构表示一组单词。<br>
 * 例如：红领巾，红河构建树后为：<br>
 *                 红                    <br>
 *              /      \                 <br>
 *           领         河             <br>
 *          /                            <br>
 *        巾                            <br>
 *其中每个节点都是一个WordTree对象，查找时从上向下查找。<br>
 * @author Looly
 *
 */
public class WordTree extends HashMap<Character, WordTree>{
	private static final long serialVersionUID = -4646423269465809276L;
	
	/** 默认的类型 */
	public final static int DEFAULT_GROUP = 0;
	
	/** 
	 * 关键词类型用于区分不同的
	 * 关键字类型，是否最后一个字符
	 */
	public Map<Integer, Boolean> groups = new HashMap<>();
	
	//--------------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public WordTree() {
	}
	
	/**
	 * 构造方法
	 * @param group 分组
	 */
	public WordTree(int group) {
		this.groups.put(group, false);
	}
	//--------------------------------------------------------------------------------------- Constructor start
	
	//------------------------------------------------------------------------------- add word
	
	/**
	 * 增加一组单词，默认分组
	 * @param words 单词
	 */
	public void addWords(Collection<String> words){
		addWords(words, DEFAULT_GROUP);
	}
	
	/**
	 * 增加一组单词
	 * @param words 单词
	 * @param group 分组
	 */
	public void addWords(Collection<String> words, int group){
		if(false == (words instanceof Set)){
			words = new HashSet<>(words);
		}
		for (String word : words) {
			addWord(word, group);
		}
	}
	
	/**
	 * 添加单词，使用默认类型
	 * @param word 单词
	 */
	public void addWord(String word) {
		this.addWord(word, DEFAULT_GROUP);
	}
	
	/**
	 * 添加单词，使用默认类型
	 * @param word 单词
	 * @param group 分组
	 */
	public void addWord(String word, int group) {
		WordTree current = this;
		WordTree child;
		char currentChar;
		int length = word.length();
		for(int i = 0; i < length; i++){
			currentChar = word.charAt(i);
			if(false == StopChar.isStopChar(currentChar)){//只处理合法字符
				child = current.get(currentChar);
				if(child == null){
					//无子类，新建一个子节点后存放下一个字符
					child = new WordTree(group);
					current.put(currentChar, child);
				}
				current = child;
			}
			current.setEnd(group, true);
		}
	}
	
	//------------------------------------------------------------------------------- match
	/**
	 * 指定文本是否包含树中的词，默认类型
	 * @param text 被检查的文本
	 * @return 是否包含
	 */
	public boolean isMatch(String text) {
		return isMatch(text, DEFAULT_GROUP);
	}
	
	/**
	 * 指定文本是否包含树中的词
	 * @param text 被检查的文本
	 * @param group 类型
	 * @return 是否包含
	 */
	public boolean isMatch(String text, int group){
		if(null == text){
			return false;
		}
		
		WordTree current = this;
		WordTree child;
		char currentChar;
		int length = text.length();
		for (int i = 0; i < length; i++) {
			for (int j = i; j < length; j++) {
				currentChar = text.charAt(j);
				if(false == StopChar.isStopChar(currentChar)){
					if(false == current.containsKey(currentChar)){
						//本级关键字不含有则进行下一轮
						break;
					}
					if(current.isEnd(group)){
						//当所分组达到末尾说明词存在
						return true;
					}
					
					child = current.get(currentChar);
					if(null != child){
						current = child;
					}
				}
			}
			current = this;
		}
		return false;
	}
	
	/**
	 * 获得第一个匹配的关键字，默认类型
	 * @param text 被检查的文本
	 * @return 匹配到的关键字
	 */
	public String match(String text) {
		return match(text, DEFAULT_GROUP);
	}
	
	/**
	 * 获得第一个匹配的关键字
	 * @param text 被检查的文本
	 * @param group 分组
	 * @return 匹配到的关键字
	 */
	public String match(String text, int group){
		if(null == text){
			return null;
		}
		WordTree current = this;
		WordTree child;
		char currentChar;
		int length = text.length();
		StringBuilder sb;
		for (int i = 0; i < length; i++) {
			sb = StrUtil.builder();
			for (int j = i; j < length; j++) {
				currentChar = text.charAt(j);
				if(false == StopChar.isStopChar(currentChar)){
					if(false == current.containsKey(currentChar)){
						//本级关键字不含有则进行下一轮
						break;
					}
					sb.append(currentChar);
					if(current.isEnd(group)){
						//当所分组达到末尾说明词存在
						return sb.toString();
					}
					child = current.get(currentChar);
					if(null != child){
						current = child;
					}
				}
			}
			current = this;
		}
		return null;
	}
	
	//------------------------------------------------------------------------------- match all
	/**
	 * 找出所有匹配的关键字，默认类型
	 * @param text 被检查的文本
	 * @return 匹配的词列表
	 */
	public List<String> matchAll(String text) {
		return matchAll(text, DEFAULT_GROUP);
	}
	
	/**
	 * 找出所有匹配的关键字
	 * @param text 被检查的文本
	 * @param group 分组
	 * @return 匹配的词列表
	 */
	public List<String> matchAll(String text, int group) {
		if(null == text){
			return null;
		}
		
		List<String> findedWords = new ArrayList<String>();
		WordTree current = this;
		WordTree child;
		char currentChar;
		int length = text.length();
		StringBuilder sb;
		for (int i = 0; i < length; i++) {
			sb = StrUtil.builder();
			for (int j = i; j < length; j++) {
				currentChar = text.charAt(j);
				if(false == StopChar.isStopChar(currentChar)){
					if(false == current.containsKey(currentChar)){
						//本级关键字不含有则进行下一轮
						break;
					}
					sb.append(currentChar);
					if(current.isEnd(group)){
						//到达单词末尾，关键词成立，从此词的下一个位置开始查找
						findedWords.add(sb.toString());
						i = j + 1;
						break;
					}
					child = current.get(currentChar);
					if(null != child){
						current = child;
					}
				}
			}
			current = this;
		}
		return findedWords;
	}
	
	
	//--------------------------------------------------------------------------------------- Private method start
	/**
	 * 是否末尾
	 * @param group 分组
	 * @return 是否末尾
	 */
	private boolean isEnd(int group){
		//已经是关键字的末尾，则结束（即只保留最短字符串）
		Boolean isEnd = this.groups.get(group);
		return null != isEnd && isEnd;
	}
	
	/**
	 * 设置是否到达末尾
	 * @param group 分组
	 * @param isEnd 是否末尾
	 */
	private void setEnd(int group, boolean isEnd){
		this.groups.put(group, isEnd);
	}
	//--------------------------------------------------------------------------------------- Private method end
}
