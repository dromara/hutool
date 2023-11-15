/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean.path;

import org.dromara.hutool.core.lang.test.bean.ExamInfoDict;
import org.dromara.hutool.core.lang.test.bean.UserInfoDict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanPathGetOrSetValueTest {
	Map<String, Object> tempMap;

	@BeforeEach
	public void init() {
		// ------------------------------------------------- 考试信息列表
		final ExamInfoDict examInfoDict = new ExamInfoDict();
		examInfoDict.setId(1);
		examInfoDict.setExamType(0);
		examInfoDict.setAnswerIs(1);

		final ExamInfoDict examInfoDict1 = new ExamInfoDict();
		examInfoDict1.setId(2);
		examInfoDict1.setExamType(0);
		examInfoDict1.setAnswerIs(0);

		final ExamInfoDict examInfoDict2 = new ExamInfoDict();
		examInfoDict2.setId(3);
		examInfoDict2.setExamType(1);
		examInfoDict2.setAnswerIs(0);

		final List<ExamInfoDict> examInfoDicts = new ArrayList<>();
		examInfoDicts.add(examInfoDict);
		examInfoDicts.add(examInfoDict1);
		examInfoDicts.add(examInfoDict2);

		// ------------------------------------------------- 用户信息
		final UserInfoDict userInfoDict = new UserInfoDict();
		userInfoDict.setId(1);
		userInfoDict.setPhotoPath("yx.mm.com");
		userInfoDict.setRealName("张三");
		userInfoDict.setExamInfoDict(examInfoDicts);

		tempMap = new HashMap<>();
		tempMap.put("userInfo", userInfoDict);
		tempMap.put("flag", 1);
	}

	@Test
	public void getValueTest() {
		final BeanPath pattern = new BeanPath("$.userInfo.examInfoDict[0].id");
		final Object result = pattern.getValue(tempMap);
		Assertions.assertEquals(1, result);
	}

	@Test
	public void setValueTest() {
		final BeanPath pattern = new BeanPath("userInfo.examInfoDict[0].id");
		pattern.setValue(tempMap, 2);
		final Object result = pattern.getValue(tempMap);
		Assertions.assertEquals(2, result);
	}

	@Test
	public void getMapTest () {
		final BeanPath pattern = new BeanPath("userInfo[id, photoPath]");
		@SuppressWarnings("unchecked")
		final Map<String, Object> result = (Map<String, Object>)pattern.getValue(tempMap);
		Assertions.assertEquals(1, result.get("id"));
		Assertions.assertEquals("yx.mm.com", result.get("photoPath"));
	}

	@Test
	public void getKeyWithDotTest () {
		final Map<String, Object> dataMap = new HashMap<>(16);
		dataMap.put("aa", "value0");
		dataMap.put("aa.bb.cc", "value111111");//     key   是类名 格式 带 ' . '

		final BeanPath pattern = new BeanPath("'aa.bb.cc'");
		Assertions.assertEquals("value111111", pattern.getValue(dataMap));
	}

	@Test
	public void issue2362Test() {
		final Map<String, Object> map = new HashMap<>();

		BeanPath beanPath = BeanPath.of("list[0].name");
		beanPath.setValue(map, "张三");
		Assertions.assertEquals("{list=[{name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.of("list[1].name");
		beanPath.setValue(map, "张三");
		Assertions.assertEquals("{list=[null, {name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.of("list[0].1.name");
		beanPath.setValue(map, "张三");
		Assertions.assertEquals("{list=[[null, {name=张三}]]}", map.toString());
	}
}
