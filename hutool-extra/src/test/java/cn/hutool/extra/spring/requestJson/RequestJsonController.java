package cn.hutool.extra.spring.requestJson;

import cn.hutool.extra.spring.requestjson.RequestJson;
import cn.hutool.extra.spring.requestjson.RequestJsonParam;
import com.fasterxml.jackson.databind.JsonNode;
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
					   @RequestJsonParam(value = "testBMap", required = false) Map<String, Object> testBMap,
					   @RequestJsonParam(value = "testBMap2", required = false) Map<String, Object> testBMap2) {
		System.out.println(other);
		System.out.println(other2);
		System.out.println(bool);
		System.out.println(inta);
		System.out.println(long1);
		System.out.println(str);
		System.out.println(object);
		System.out.println(jsonNode);
		System.out.println(testA);
		System.out.println(testB);
		System.out.println(list1);
		System.out.println("list2:" + list2);
		System.out.println(set1);
		System.out.println("set2:" +  set2);
		System.out.println(testBMap);
		System.out.println("testBMap2:" + testBMap2);
		return "success";
	}
}
