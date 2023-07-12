package cn.hutool.extra.spring.requestJson;

import cn.hutool.extra.spring.requestjson.RequestJson;
import cn.hutool.extra.spring.requestjson.RequestJsonParam;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by useheart on 2023/7/11
 * @author useheart
 */
@RestController
@RequestMapping("/")
public class RequestJsonController {

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test(@RequestJsonParam(value = "other", required = false) String other,
					   @RequestJsonParam(value = "other2", required = false, defaultValue = "other") String other2,
					   @RequestJsonParam Boolean bool,
					   @RequestJsonParam(value = "int1", required = false) Integer inta,
					   @RequestJsonParam(value = "long1") long long1,
					   @RequestJsonParam String str,
					   @RequestJsonParam Object object,
					   @RequestJson JsonNode jsonNode,
					   @RequestJsonParam(value = "testA") EnableRequestJsonTest.TestA testA,
					   @RequestJsonParam(value = "testB.b") String testB,
					   @RequestJsonParam(value = "testAList") List<EnableRequestJsonTest.TestA> list1,
					   @RequestJsonParam(value = "testAList2", required = false) List<EnableRequestJsonTest.TestA> list2,
					   @RequestJsonParam(value = "objectSet") List<Object> set1,
					   @RequestJsonParam(value = "objectSet2", required = false) List<Object> set2,
					   @RequestJsonParam(value = "testMap", required = false) Map<String, Object> testMap,
					   @RequestJsonParam(value = "testMap2", required = false) Map<String, Object> testMap2) {
		System.out.println(other);
		Assert.assertNull(other);
		System.out.println(other2);
		Assert.assertEquals(other2, "other");
		System.out.println(bool);
		Assert.assertEquals(bool, true);
		System.out.println(inta);
		Assert.assertEquals(inta, Integer.valueOf(11));
		System.out.println(long1);
		Assert.assertEquals(long1, 1111L);
		System.out.println(str);
		Assert.assertEquals(str, "hello world");
		System.out.println(object);
		Assert.assertNotEquals(object, null);
		System.out.println(jsonNode);
		Assert.assertNotEquals(jsonNode, null);
		System.out.println(testA);
		Assert.assertEquals(testA, new EnableRequestJsonTest.TestA());
		System.out.println(testB);
		Assert.assertEquals(testB, testB);
		System.out.println(list1);
		Assert.assertNotEquals(list1, null);
		System.out.println("list2:" + list2);
		Assert.assertNull(list2);
		System.out.println(set1);
		Assert.assertNotEquals(set1, null);
		System.out.println("set2:" +  set2);
		Assert.assertNull(set2);
		System.out.println(testMap);
		Assert.assertNotEquals(testMap, null);
		System.out.println("testBMap2:" + testMap2);
		Assert.assertNull(testMap2);
		return "success";
	}
}
