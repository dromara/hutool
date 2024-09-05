/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.text.dfa;

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
		Assertions.assertEquals("[站房建设, 面积较小, 不符合规范要求, 辅助设施, 站房]", strings.toString());
	}
}
