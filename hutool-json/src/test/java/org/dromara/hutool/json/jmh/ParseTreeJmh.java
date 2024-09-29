package org.dromara.hutool.json.jmh;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * JSON将字符串解析为树结构的性能对比测试
 *
 * @author looly
 */
@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 5, time = 1) //预热5次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class ParseTreeJmh {

	private String jsonStr;

	@Setup
	public void setup() {
		jsonStr = "{\"name\":\"张三\",\"age\":18,\"birthday\":\"2020-01-01\"}";
	}

	@Benchmark
	public void gsonJmh() {
		final JsonElement jsonElement = JsonParser.parseString(jsonStr);
		assertNotNull(jsonElement);
	}

	@Benchmark
	public void hutoolJmh() {
		final JSONObject parse = JSONUtil.parseObj(jsonStr);
		assertNotNull(parse);
	}

	@Benchmark
	public void fastJSONJmh() {
		final com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(jsonStr);
		assertNotNull(jsonObject);
	}

	@Benchmark
	public void jacksonJmh() throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode jsonNode = mapper.readTree(jsonStr);
		assertNotNull(jsonNode);
	}
}
