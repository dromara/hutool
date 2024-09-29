/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json.jmh;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.engine.JSONEngine;
import org.dromara.hutool.json.engine.JSONEngineFactory;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 5, time = 1) //预热5次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class FromJsonStringStrJmh {

	private JSONEngine jacksonEngine;
	private JSONEngine gsonEngine;
	private JSONEngine fastJSONEngine;
	private JSONEngine hutoolEngine;

	private String jsonStr;

	@Setup
	public void setup() {
		jsonStr = "{\"name\":\"张三\",\"age\":18,\"birthday\":\"2020-01-01\"}";

		jacksonEngine = JSONEngineFactory.createEngine("jackson");
		gsonEngine = JSONEngineFactory.createEngine("gson");
		fastJSONEngine = JSONEngineFactory.createEngine("fastjson");
		hutoolEngine = JSONEngineFactory.createEngine("hutool");
	}

	@Benchmark
	public void jacksonJmh() {
		jacksonEngine.fromJsonString(jsonStr, JsonNode.class);
	}

	@Benchmark
	public void gsonJmh() {
		gsonEngine.fromJsonString(jsonStr, JsonElement.class);
	}

	@Benchmark
	public void fastJSONJmh() {
		fastJSONEngine.fromJsonString(jsonStr, com.alibaba.fastjson2.JSON.class);
	}

	@Benchmark
	public void hutoolJSONJmh() {
		hutoolEngine.fromJsonString(jsonStr, JSON.class);
	}
}
