package cn.hutool.core.bean;

import cn.hutool.core.lang.test.bean.ExamInfoDict;
import cn.hutool.core.lang.test.bean.UserInfoDict;
import cn.hutool.core.map.Dict;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link BeanPath} 单元测试
 *
 * @author looly
 *
 */
public class BeanPathTest {

	Map<String, Object> tempMap;

	@Before
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
		final BeanPath pattern = new BeanPath("userInfo.examInfoDict[0].id");
		Assert.assertEquals("userInfo", pattern.patternParts.get(0));
		Assert.assertEquals("examInfoDict", pattern.patternParts.get(1));
		Assert.assertEquals("0", pattern.patternParts.get(2));
		Assert.assertEquals("id", pattern.patternParts.get(3));

	}

	@Test
	public void beanPathTest2() {
		final BeanPath pattern = new BeanPath("[userInfo][examInfoDict][0][id]");
		Assert.assertEquals("userInfo", pattern.patternParts.get(0));
		Assert.assertEquals("examInfoDict", pattern.patternParts.get(1));
		Assert.assertEquals("0", pattern.patternParts.get(2));
		Assert.assertEquals("id", pattern.patternParts.get(3));
	}

	@Test
	public void beanPathTest3() {
		final BeanPath pattern = new BeanPath("['userInfo']['examInfoDict'][0]['id']");
		Assert.assertEquals("userInfo", pattern.patternParts.get(0));
		Assert.assertEquals("examInfoDict", pattern.patternParts.get(1));
		Assert.assertEquals("0", pattern.patternParts.get(2));
		Assert.assertEquals("id", pattern.patternParts.get(3));
	}

	@Test
	public void getTest() {
		final BeanPath pattern = BeanPath.of("userInfo.examInfoDict[0].id");
		final Object result = pattern.get(tempMap);
		Assert.assertEquals(1, result);
	}

	@Test
	public void setTest() {
		final BeanPath pattern = BeanPath.of("userInfo.examInfoDict[0].id");
		pattern.set(tempMap, 2);
		final Object result = pattern.get(tempMap);
		Assert.assertEquals(2, result);
	}

	@Test
	public void getMapTest () {
		final BeanPath pattern = BeanPath.of("userInfo[id, photoPath]");
		@SuppressWarnings("unchecked")
		final Map<String, Object> result = (Map<String, Object>)pattern.get(tempMap);
		Assert.assertEquals(1, result.get("id"));
		Assert.assertEquals("yx.mm.com", result.get("photoPath"));
	}

	@Test
	public void getKeyWithDotTest () {
		final Map<String, Object> dataMap = new HashMap<>(16);
		dataMap.put("aa", "value0");
		dataMap.put("aa.bb.cc", "value111111");//     key   是类名 格式 带 ' . '

		final BeanPath pattern = BeanPath.of("'aa.bb.cc'");
		Assert.assertEquals("value111111", pattern.get(dataMap));
	}

	@Test
	public void compileTest(){
		final BeanPath of = BeanPath.of("'abc.dd'.ee.ff'.'");
		Assert.assertEquals("abc.dd", of.getPatternParts().get(0));
		Assert.assertEquals("ee", of.getPatternParts().get(1));
		Assert.assertEquals("ff.", of.getPatternParts().get(2));
	}

	@Test
	public void issue2362Test() {
		final Map<String, Object> map = new HashMap<>();

		BeanPath beanPath = BeanPath.of("list[0].name");
		beanPath.set(map, "张三");
		Assert.assertEquals("{list=[{name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.of("list[1].name");
		beanPath.set(map, "张三");
		Assert.assertEquals("{list=[null, {name=张三}]}", map.toString());

		map.clear();
		beanPath = BeanPath.of("list[0].1.name");
		beanPath.set(map, "张三");
		Assert.assertEquals("{list=[[null, {name=张三}]]}", map.toString());
	}

	@Test
	public void putByPathTest() {
		final Dict dict = new Dict();
		BeanPath.of("aa.bb").set(dict, "BB");
		Assert.assertEquals("{aa={bb=BB}}", dict.toString());
	}
}
