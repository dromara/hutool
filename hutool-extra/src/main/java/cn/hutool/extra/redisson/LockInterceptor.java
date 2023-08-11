package cn.hutool.extra.redisson;

import cn.hutool.core.lang.Assert;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;

/**
 * Author: miracle
 * Date: 2023/8/10 16:46
 */
@Aspect
public class LockInterceptor {

	@Resource
	private RedissonClient client;

	@Pointcut("@annotation(cn.hutool.extra.redisson.RedissonLock)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object process(ProceedingJoinPoint point) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		Method method = methodSignature.getMethod();
		RedissonLock lock = method.getAnnotation(RedissonLock.class);
		boolean isLocked;
		String keyString;
		Lock underLock;
		try {
			Map<String, Object> params = _params(point);
			EvaluationContext context = new StandardEvaluationContext(String.class);
			if (!params.isEmpty()) {
				params.forEach(context::setVariable);
			}
			LockKey key = lock.key();
			keyString = getKey(key, context);
			//获取锁信息
			underLock = this.generate(keyString, key.isWrite());
			isLocked = underLock.tryLock(lock.waitTime(), lock.timeUnit());
		} catch (Exception e1) {
			throw new RuntimeException("分布式锁获取失败");
		}
		Assert.isTrue(isLocked, lock.errorCode());
		try {
			return point.proceed();
		} finally {
			try {
				underLock.unlock();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	private Lock generate(String keyString, Boolean isWrite) {
		RLock lock;
		RReadWriteLock readWriteLock = client.getReadWriteLock(keyString);
		if (isWrite) {
			lock = readWriteLock.writeLock();
		} else {
			lock = readWriteLock.readLock();
		}
		return client.getMultiLock(lock);
	}

	private String getKey(LockKey key, EvaluationContext context) {
		String[] keySpels = key.keys();
		List<Object> keyValues = new ArrayList<>();
		for (String keySpel : keySpels) {
			if (StringUtils.isNotBlank(keySpel)) {
				Expression expression = new SpelExpressionParser().parseExpression(keySpel);
				Object object = expression.getValue(context);
				keyValues.add(object);
			}
		}
		StringBuilder builder = new StringBuilder();
		StringBuilder append = builder.append(key.prefix());
		for (Object item : keyValues) {
			append.append(item.toString()).append(key.delimiter());
		}
		return append.toString();
	}

	private Map<String, Object> _params(ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		String[] parameterNames = new DefaultParameterNameDiscoverer().getParameterNames(method);
		Map<String, Object> paramMap = Maps.newHashMap();
		for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
			paramMap.put(parameterNames[i], args[i]);
		}
		return paramMap;
	}

}
