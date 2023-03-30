package cn.hutool.json;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.test.bean.ExamInfoDict;
import cn.hutool.json.test.bean.PerfectEvaluationProductResVo;
import cn.hutool.json.test.bean.UserInfoDict;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON转换单元测试
 *
 * @author Looly，质量过关
 *
 */
public class JSONConvertTest {

	@Test
	public void testBean2Json() {

		final UserInfoDict userInfoDict = new UserInfoDict();
		userInfoDict.setId(1);
		userInfoDict.setPhotoPath("yx.mm.com");
		userInfoDict.setRealName("质量过关");

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

		userInfoDict.setExamInfoDict(examInfoDicts);

		final Map<String, Object> tempMap = new HashMap<>();
		tempMap.put("userInfoDict", userInfoDict);
		tempMap.put("toSendManIdCard", 1);

		final JSONObject obj = JSONUtil.parseObj(tempMap);
		Assertions.assertEquals(new Integer(1), obj.getInt("toSendManIdCard"));

		final JSONObject examInfoDictsJson = obj.getJSONObject("userInfoDict");
		Assertions.assertEquals(new Integer(1), examInfoDictsJson.getInt("id"));
		Assertions.assertEquals("质量过关", examInfoDictsJson.getStr("realName"));

		final Object id = JSONUtil.getByPath(obj, "userInfoDict.examInfoDict[0].id");
		Assertions.assertEquals(1, id);
	}

	@Test
	public void testJson2Bean() {
		// language=JSON
		final String examJson = "{\n" + "  \"examInfoDicts\": {\n" + "    \"id\": 1,\n" + "    \"realName\": \"质量过关\",\n" //
				+ "    \"examInfoDict\": [\n" + "      {\n" + "        \"id\": 1,\n" + "        \"answerIs\": 1,\n" + "        \"examType\": 0\n" //
				+ "      },\n" + "      {\n" + "        \"id\": 2,\n" + "        \"answerIs\": 0,\n" + "        \"examType\": 0\n" + "      },\n" //
				+ "      {\n" + "        \"id\": 3,\n" + "        \"answerIs\": 0,\n" + "        \"examType\": 1\n" + "      }\n" + "    ],\n" //
				+ "    \"photoPath\": \"yx.mm.com\"\n" + "  },\n" + "  \"toSendManIdCard\": 1\n" + "}";

		final JSONObject jsonObject = JSONUtil.parseObj(examJson).getJSONObject("examInfoDicts");
		final UserInfoDict userInfoDict = jsonObject.toBean(UserInfoDict.class);

		Assertions.assertEquals(userInfoDict.getId(), new Integer(1));
		Assertions.assertEquals(userInfoDict.getRealName(), "质量过关");

		//============

		final String jsonStr = "{\"id\":null,\"examInfoDict\":[{\"answerIs\":1, \"id\":null}]}";//JSONUtil.toJsonStr(userInfoDict1);
		final JSONObject jsonObject2 = JSONUtil.parseObj(jsonStr);//.getJSONObject("examInfoDicts");
		final UserInfoDict userInfoDict2 = jsonObject2.toBean(UserInfoDict.class);
		Assertions.assertNull(userInfoDict2.getId());
	}

	/**
	 * 针对Bean中Setter返回this测试是否可以成功调用Setter方法并注入
	 */
	@Test
	public void testJson2Bean2() {
		final String jsonStr = ResourceUtil.readUtf8Str("evaluation.json");
		final JSONObject obj = JSONUtil.parseObj(jsonStr);
		final PerfectEvaluationProductResVo vo = obj.toBean(PerfectEvaluationProductResVo.class);

		Assertions.assertEquals(obj.getStr("HA001"), vo.getHA001());
		Assertions.assertEquals(obj.getInt("costTotal"), vo.getCostTotal());
	}
}
