package com.xiaoleilu.hutool.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.json.test.bean.ExamInfoDict;
import com.xiaoleilu.hutool.json.test.bean.UserInfoDict;

/**
 * JSON转换单元测试
 * 
 * @author Looly，质量过关
 *
 */
public class JSONConvertTest {

	@Test
	public void testBean2Json() {

		Map<String, Object> tempMap = new HashMap<String, Object>();

		UserInfoDict userInfoDict = new UserInfoDict();
		userInfoDict.setId(1);
		userInfoDict.setPhotoPath("yx.mm.com");
		userInfoDict.setRealName("质量过关");

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

		userInfoDict.setExamInfoDict(examInfoDicts);

		tempMap.put("examInfoDicts", userInfoDict);
		tempMap.put("toSendManIdCard", 1);

		JSONObject obj = JSONUtil.parseObj(tempMap);
		Assert.assertEquals(new Integer(1), obj.getInt("toSendManIdCard"));
		Assert.assertEquals(new Integer(1), obj.getInt("toSendManIdCard"));

		JSONObject examInfoDictsJson = obj.getJSONObject("examInfoDicts");
		Assert.assertEquals(examInfoDictsJson.getInt("id"), new Integer(1));
		Assert.assertEquals(examInfoDictsJson.getStr("realName"), "质量过关");
	}

	@Test
	public void testJson2Bean() {
		// language=JSON
		String examJson = "{\n" + "  \"examInfoDicts\": {\n" + "    \"id\": 1,\n" + "    \"realName\": \"质量过关\",\n" //
				+ "    \"examInfoDict\": [\n" + "      {\n" + "        \"id\": 1,\n" + "        \"answerIs\": 1,\n" + "        \"examType\": 0\n" //
				+ "      },\n" + "      {\n" + "        \"id\": 2,\n" + "        \"answerIs\": 0,\n" + "        \"examType\": 0\n" + "      },\n" //
				+ "      {\n" + "        \"id\": 3,\n" + "        \"answerIs\": 0,\n" + "        \"examType\": 1\n" + "      }\n" + "    ],\n" //
				+ "    \"photoPath\": \"yx.mm.com\"\n" + "  },\n" + "  \"toSendManIdCard\": 1\n" + "}";

		JSONObject jsonObject = JSONUtil.parseObj(examJson).getJSONObject("examInfoDicts");
		UserInfoDict userInfoDict = jsonObject.toBean(UserInfoDict.class);
		
		Assert.assertEquals(userInfoDict.getId(), new Integer(1));
		Assert.assertEquals(userInfoDict.getRealName(), "质量过关");
	}

}
