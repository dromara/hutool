package cn.hutool.core.text.dfa;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IssueI5Q4HDTest {

	@Test
	public void matchAllTest(){
		final String content="站房建设面积较小，不符合规范要求。辅助设施_站房_面积";
		final String keywordStr="不符合规范要求,现场手工比对孔不规范,采样口,现场,辅助设施,比对孔未处于监测断面下游,站房,未设置,标识,给排水,面积较小,监控设备,灭火装置,排污口,设备操作维护不方便,不规范,采样平台,参比方法,直爬梯,单独,站房建设,不健全,没有配置";
		final List<String> keyWords = Arrays.asList(keywordStr.split(","));
		final Set<String> keyWordSet=new HashSet<>(keyWords);
		final WordTree wordTree=new WordTree();
		wordTree.addWords(keyWordSet);
		//DateUtil.beginOfHour()
		final List<String> strings = wordTree.matchAll(content, -1, true, true);
		Assertions.assertEquals("[站房, 站房建设, 面积较小, 不符合规范要求, 辅助设施, 站房]", strings.toString());
	}
}
