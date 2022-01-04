package cn.hutool.db.nosql;

import cn.hutool.db.nosql.redis.RedisDS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

public class RedisDSTest {

	@Test
	@Disabled
	public void redisDSTest(){
		final Jedis jedis = RedisDS.create().getJedis();
	}
}
