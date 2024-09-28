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

package org.dromara.hutool.json.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openjdk.jmh.annotations.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 5, time = 1) //预热5次调用
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class ToJsonStrJmh {

	private JSONEngine jacksonEngine;
	private JSONEngine gsonEngine;
	private JSONEngine fastJSONEngine;
	private JSONEngine moshiEngine;
	private JSONEngine hutoolEngine;

	private TestBean testBean;

	@Setup
	public void setup() {
		jacksonEngine = JSONEngineFactory.createEngine("jackson");
		gsonEngine = JSONEngineFactory.createEngine("gson");
		fastJSONEngine = JSONEngineFactory.createEngine("fastjson");
		moshiEngine = JSONEngineFactory.createEngine("moshi");
		hutoolEngine = JSONEngineFactory.createEngine("hutool");

		testBean = new TestBean("张三", 18, true, new Date(), null);
	}

	@Benchmark
	public void jacksonJmh() {
		jacksonEngine.toJsonString(testBean);
	}

	@Benchmark
	public void gsonJmh() {
		gsonEngine.toJsonString(testBean);
	}

	@Benchmark
	public void fastJSONJmh() {
		fastJSONEngine.toJsonString(testBean);
	}

	@Benchmark
	public void moshiJSONJmh() {
		moshiEngine.toJsonString(testBean);
	}

	@Benchmark
	public void hutoolJSONJmh() {
		hutoolEngine.toJsonString(testBean);
	}

	@Data
	@AllArgsConstructor
	static class TestBean {
		private String name;
		private int age;
		private boolean gender;
		private Date createDate;
		private Object nullObj;
	}
}
