package cn.hutool.extra.redisson.config;

import cn.hutool.extra.redisson.LockInterceptor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;

/**
 * Author: miracle
 * Date: 2023/8/11 13:40
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class})
@ConditionalOnClass(value = {RedisConfiguration.class, RedissonClient.class})
@AutoConfigureAfter(value = {RedisAutoConfiguration.class})
public class AutoRedissonLockConfiguration {

	private final RedisProperties redisProperties;

	public AutoRedissonLockConfiguration(RedisProperties redisProperties) {
		this.redisProperties = redisProperties;
	}

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
		//使用json序列化方式
		config.setCodec(new JsonJacksonCodec());
		config.useSingleServer().setAddress(address);
		SingleServerConfig singleServerConfig = config.useSingleServer();
		singleServerConfig.setPassword(redisProperties.getPassword());
		singleServerConfig.setDatabase(redisProperties.getDatabase());
		return Redisson.create(config);
	}

	@Bean
	public LockInterceptor lockInterceptor(RedissonClient client) {
		return new LockInterceptor(client);
	}

}
