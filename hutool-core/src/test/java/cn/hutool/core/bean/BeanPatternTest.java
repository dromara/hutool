package cn.hutool.core.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.test.bean.ExamInfoDict;
import cn.hutool.core.lang.test.bean.UserInfoDict;

public class BeanPatternTest {
	
	@Test
	public void beanPatternTest1() {
		BeanPattern pattern = new BeanPattern("userInfo.examInfoDict[0].id");
		Assert.assertEquals("userInfo", pattern.patternParts.get(0));
		Assert.assertEquals("examInfoDict", pattern.patternParts.get(1));
		Assert.assertEquals(0, pattern.patternParts.get(2));
		Assert.assertEquals("id", pattern.patternParts.get(3));
		Console.log(pattern.patternParts);
	}
	
	@Test
	public void getValueTest(){
		//------------------------------------------------- 考试信息列表
		ExamInfoDict examInfoDict = new ExamInfoDict();
		examInfoDict.setId(1);
		examInfoDict.setExamType(0);
		examInfoDict.setAnswerIs(1);

		ExamInfoDict examInfoDict1 = new ExamInfoDict();
		examInfoDict1.setId(2);
		examInfoDict1.setExamType(0);
		examInfoDict1.setAnswerIs(0);

		ExamInfoDict examInfoDict2 = new ExamInfoDict();
		examInfoDict2.setId(3);
		examInfoDict2.setExamType(1);
		examInfoDict2.setAnswerIs(0);

		List<ExamInfoDict> examInfoDicts = new ArrayList<ExamInfoDict>();
		examInfoDicts.add(examInfoDict);
		examInfoDicts.add(examInfoDict1);
		examInfoDicts.add(examInfoDict2);

		//------------------------------------------------- 用户信息
		UserInfoDict userInfoDict = new UserInfoDict();
		userInfoDict.setId(1);
		userInfoDict.setPhotoPath("yx.mm.com");
		userInfoDict.setRealName("张三");
		userInfoDict.setExamInfoDict(examInfoDicts);

		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("userInfo", userInfoDict);
		tempMap.put("flag", 1);

		BeanPattern pattern = BeanPattern.create("userInfo.examInfoDict[0].id");
		Object result = pattern.getValue(tempMap);
		Assert.assertEquals(1, result);
	}
}
