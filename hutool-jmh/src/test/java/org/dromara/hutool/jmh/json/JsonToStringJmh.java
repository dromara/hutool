package org.dromara.hutool.jmh.json;

import com.alibaba.fastjson2.JSON;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.openjdk.jmh.annotations.*;

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
public class JsonToStringJmh {

	private JSONObject hutoolJSON;
	private JsonElement gson;
	private com.alibaba.fastjson2.JSONObject fastJSON;


	@Setup
	public void setup() {
		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"birthday\":\"2020-01-01\"}";
		hutoolJSON = JSONUtil.parseObj(jsonStr);
		gson = JsonParser.parseString(jsonStr);
		fastJSON = JSON.parseObject(jsonStr);
	}

	@Benchmark
	public void gsonJmh() {
		final String jsonStr = gson.toString();
		Assertions.assertNotNull(jsonStr);
	}

	@Benchmark
	public void hutoolJmh() {
		final String jsonStr = hutoolJSON.toString();
		Assertions.assertNotNull(jsonStr);
	}

	@Benchmark
	public void fastJSONJmh() {
		final String jsonStr = fastJSON.toString();
		Assertions.assertNotNull(jsonStr);
	}
}
