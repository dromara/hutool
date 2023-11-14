/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.test.bean.ExamInfoDict;
import org.dromara.hutool.core.lang.test.bean.UserInfoDict;
import org.dromara.hutool.core.map.Dict;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link BeanPathOld} 单元测试
 *
 * @author looly
 *
 */
public class BeanPathTest {

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
	public void beanPathTest1() {
		final BeanPathOld pattern = new BeanPathOld("userInfo.examInfoDict[0].id");
		Assertions.assertEquals("userInfo", pattern.patternParts.get(0));
		Assertions.assertEquals("examInfoDict", pattern.patternParts.get(1));
		Assertions.assertEquals("0", pattern.patternParts.get(2));
		Assertions.assertEquals("id", pattern.patternParts.get(3));

	}

	@Test
	public void beanPathTest2() {
		final BeanPathOld pattern = new BeanPathOld("[userInfo][examInfoDict][0][id]");
		Assertions.assertEquals("userInfo", pattern.patternParts.get(0));
		Assertions.assertEquals("examInfoDict", pattern.patternParts.get(1));
		Assertions.assertEquals("0", pattern.patternParts.get(2));
		Assertions.assertEquals("id", pattern.patternParts.get(3));
	}

	@Test
	public void beanPathTest3() {
		final BeanPathOld pattern = new BeanPathOld("['userInfo']['examInfoDict'][0]['id']");
		Assertions.assertEquals("userInfo", pattern.patternParts.get(0));
		Assertions.assertEquals("examInfoDict", pattern.patternParts.get(1));
		Assertions.assertEquals("0", pattern.patternParts.get(2));
		Assertions.assertEquals("id", pattern.patternParts.get(3));
	}

	@Test
	public void getTest() {
		final BeanPathOld pattern = BeanPathOld.of("userInfo.examInfoDict[0].id");
		final Object result = pattern.get(tempMap);
		Assertions.assertEquals(1, result);
	}

	@Test
	public void setTest() {
		final BeanPathOld pattern = BeanPathOld.of("userInfo.examInfoDict[0].id");
		pattern.set(tempMap, 2);
		final Object result = pattern.get(tempMap);
		Assertions.assertEquals(2, result);
	}

	@Test
	public void getMapTest () {
		final BeanPathOld pattern = BeanPathOld.of("userInfo[id, photoPath]");
		@SuppressWarnings("unchecked")
		final Map<String, Object> result = (Map<String, Object>)pattern.get(tempMap);
		Assertions.assertEquals(1, result.get("id"));
		Assertions.assertEquals("yx.mm.com", result.get("photoPath"));
	}

	@Test
	public void getKeyWithDotTest () {
		final Map<String, Object> dataMap = new HashMap<>(16);
		dataMap.put("aa", "value0");
		dataMap.put("aa.bb.cc", "value111111");//     key   是类名 格式 带 ' . '

		final BeanPathOld pattern = BeanPathOld.of("'aa.bb.cc'");
		Assertions.assertEquals("value111111", pattern.get(dataMap));
	}

	@Test
	public void compileTest(){
		final BeanPathOld of = BeanPathOld.of("'abc.dd'.ee.ff'.'");
		Assertions.assertEquals("abc.dd", of.getPatternParts().get(0));
		Assertions.assertEquals("ee", of.getPatternParts().get(1));
		Assertions.assertEquals("ff.", of.getPatternParts().get(2));
	}

	@Test
	public void issue2362Test() {
		final Map<String, Object> map = new HashMap<>();

		BeanPathOld beanPath = BeanPathOld.of("list[0].name");
		beanPath.set(map, "张三");
		Assertions.assertEquals("{list=[{name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPathOld.of("list[1].name");
		beanPath.set(map, "张三");
		Assertions.assertEquals("{list=[null, {name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPathOld.of("list[0].1.name");
		beanPath.set(map, "张三");
		Assertions.assertEquals("{list=[[null, {name=张三}]]}", map.toString());
	}

	@Test
	public void putTest() {
		final Map<String, Object> map = new HashMap<>();

		final BeanPathOld beanPath = BeanPathOld.of("list[1].name");
		beanPath.set(map, "张三");
		Assertions.assertEquals("{list=[null, {name=张三}]}", map.toString());
	}

	@Test
	public void putByPathTest() {
		final Dict dict = new Dict();
		BeanPathOld.of("aa.bb").set(dict, "BB");
		Assertions.assertEquals("{aa={bb=BB}}", dict.toString());
	}

	@Test
	public void appendArrayTest(){
		// issue#3008@Github
		final MyUser myUser = new MyUser();
		BeanPathOld.of("hobby[0]").set(myUser, "LOL");
		BeanPathOld.of("hobby[1]").set(myUser, "KFC");
		BeanPathOld.of("hobby[2]").set(myUser, "COFFE");

		Assertions.assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(myUser.getHobby()));
	}

	@Test
	public void appendArrayTest2(){
		// issue#3008@Github
		final MyUser2 myUser = new MyUser2();
		BeanPathOld.of("myUser.hobby[0]").set(myUser, "LOL");
		BeanPathOld.of("myUser.hobby[1]").set(myUser, "KFC");
		BeanPathOld.of("myUser.hobby[2]").set(myUser, "COFFE");

		Assertions.assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(myUser.getMyUser().getHobby()));
	}

	@Data
	static class MyUser {
		private String[] hobby;
	}

	@Data
	static class MyUser2 {
		private MyUser myUser;
	}
}
