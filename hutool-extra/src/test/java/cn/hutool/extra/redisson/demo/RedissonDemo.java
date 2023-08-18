package cn.hutool.extra.redisson.demo;

import cn.hutool.extra.redisson.LockKey;
import cn.hutool.extra.redisson.RedissonLock;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: miracle
 * Date: 2023/8/12 10:37
 */
@Service
public class RedissonDemo {

	private final AtomicInteger init = new AtomicInteger(0);

	@RedissonLock(key = @LockKey(prefix = "demo", keys = "#name"))
	public Integer lock(String name) {
		int i = init.addAndGet(1);
		System.out.println(name + ":" + i);
		return i;
	}


}
