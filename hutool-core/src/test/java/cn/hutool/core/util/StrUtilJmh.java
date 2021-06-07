package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)//每次执行平均花费时间
@Warmup(iterations = 5) //预热5次调用
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // 执行5此，每次1秒
@Threads(1) //单线程
@Fork(1) //
@OutputTimeUnit(TimeUnit.NANOSECONDS) // 单位：纳秒
@State(Scope.Benchmark) // 共享域
public class StrUtilJmh {

	@Benchmark
	public void joinJmh() {
		CollUtil.join(initSize(20), ",", (org)-> String.valueOf(org.getProvinceId()));
	}

	@Benchmark
	public void joinJmh2() {
		final List<Org> orgs = initSize(20);
		final StringBuilder sb = new StringBuilder();
		final int size = orgs.size();
		boolean isFirst = true;
		for(int i = 0; i < size; i++){
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(",");
			}
			sb.append(orgs.get(i).getProvinceId());
		}
		sb.toString();
	}

	/**
	 * 来自：ibeetl
	 * 模拟测试数据,建议使用较大size以验证效果
	 * @param size 数量
	 * @return 对象List
	 */
	public static List<Org> initSize(int size){
		Org org = new Org();
		org.setProvinceId(21);
		org.setName("北京");
		ArrayList<Org> list = new ArrayList<>();
		for(int i=0;i<size;i++){
			list.add(org);
		}
		return list;
	}

	@Data
	public static class Org {
		private Integer id;
		private Integer provinceId;
		private String name;
	}
}
