package org.dromara.hutool.json.jmh;

import com.google.gson.JsonObject;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.json.JSONObject;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试JSON树结构转JSON字符串性能
 */
@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 1, time = 1) //预热5次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class JsonPutJmh {

	Map<String, String> testData;
	private JSONObject hutoolJSON;
	private JsonObject gson;
	private com.alibaba.fastjson2.JSONObject fastJSON;


	@Setup
	public void setup() {
		testData = new HashMap<>(100, 1);
		for (int i = 0; i < 100; i++) {
			testData.put(RandomUtil.randomString(10), RandomUtil.randomString(20));
		}

		hutoolJSON = new JSONObject();
		gson = new JsonObject();
		fastJSON = new com.alibaba.fastjson2.JSONObject();
	}

	@Benchmark
	public void gsonJmh() {
		testData.forEach(gson::addProperty);
	}

	@Benchmark
	public void hutoolJmh() {
		hutoolJSON.putAllObj(testData);
	}

	@Benchmark
	public void fastJSONJmh() {
		fastJSON.putAll(testData);
	}
}
