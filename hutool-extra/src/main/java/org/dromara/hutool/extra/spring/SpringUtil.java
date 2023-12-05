/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.spring;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.reflect.TypeReference;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

/**
 * Spring(Spring boot)工具封装，包括：
 *
 * <ol>
 *     <li>Spring IOC容器中的bean对象获取</li>
 *     <li>注册和注销Bean</li>
 * </ol>
 *
 * @author loolly
 * @since 5.1.0
 */
public class SpringUtil implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	/**
	 * Spring应用上下文环境
	 */
	private static ConfigurableApplicationContext applicationContext;

	@Override
	public void initialize(final ConfigurableApplicationContext applicationContext) {
		SpringUtil.applicationContext = applicationContext;
	}

	/**
	 * 获取{@link ApplicationContext}
	 *
	 * @return {@link ApplicationContext}
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取{@link ListableBeanFactory}，可能为{@link ConfigurableListableBeanFactory} 或 {@link ApplicationContextAware}
	 *
	 * @return {@link ListableBeanFactory}
	 * @since 5.7.0
	 */
	public static ListableBeanFactory getBeanFactory() {
		final ListableBeanFactory factory = applicationContext;
		if (null == factory) {
			throw new HutoolException("No ConfigurableListableBeanFactory or ApplicationContext injected, maybe not in the Spring environment?");
		}
		return factory;
	}

	/**
	 * 获取{@link ConfigurableListableBeanFactory}
	 *
	 * @return {@link ConfigurableListableBeanFactory}
	 * @throws HutoolException 当上下文非ConfigurableListableBeanFactory抛出异常
	 * @since 5.7.7
	 */
	public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws HutoolException {
		return applicationContext.getBeanFactory();
	}

	//通过name获取 Bean.

	/**
	 * 通过name获取 Bean
	 *
	 * @param <T>  Bean类型
	 * @param name Bean名称
	 * @return Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(final String name) {
		return (T) getBeanFactory().getBean(name);
	}

	/**
	 * 通过class获取Bean
	 *
	 * @param <T>   Bean类型
	 * @param clazz Bean类
	 * @return Bean对象
	 */
	public static <T> T getBean(final Class<T> clazz) {
		return getBeanFactory().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param <T>   bean类型
	 * @param name  Bean名称
	 * @param clazz bean类型
	 * @return Bean对象
	 */
	public static <T> T getBean(final String name, final Class<T> clazz) {
		return getBeanFactory().getBean(name, clazz);
	}

	/**
	 * 通过类型参考返回带泛型参数的Bean
	 *
	 * @param reference 类型参考，用于持有转换后的泛型类型
	 * @param <T>       Bean类型
	 * @return 带泛型参数的Bean
	 * @since 5.4.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(final TypeReference<T> reference) {
		final ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
		final Class<T> rawType = (Class<T>) parameterizedType.getRawType();
		final Class<?>[] genericTypes = Arrays.stream(parameterizedType.getActualTypeArguments()).map(type -> (Class<?>) type).toArray(Class[]::new);
		final String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
		return getBean(beanNames[0], rawType);
	}

	/**
	 * 获取指定类型对应的所有Bean，包括子类
	 *
	 * @param <T>  Bean类型
	 * @param type 类、接口，null表示获取所有bean
	 * @return 类型对应的bean，key是bean注册的name，value是Bean
	 * @since 5.3.3
	 */
	public static <T> Map<String, T> getBeansOfType(final Class<T> type) {
		return getBeanFactory().getBeansOfType(type);
	}

	/**
	 * 获取指定类型对应的Bean名称，包括子类
	 *
	 * @param type 类、接口，null表示获取所有bean名称
	 * @return bean名称
	 * @since 5.3.3
	 */
	public static String[] getBeanNamesForType(final Class<?> type) {
		return getBeanFactory().getBeanNamesForType(type);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param key 配置项key
	 * @return 属性值
	 * @since 5.3.3
	 */
	public static String getProperty(final String key) {
		final ConfigurableEnvironment environment = getEnvironment();
		return null == environment ? null : environment.getProperty(key);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param key          配置项key
	 * @param defaultValue 默认值
	 * @return 属性值
	 * @since 5.8.24
	 */
	public static String getProperty(final String key, final String defaultValue) {
		final ConfigurableEnvironment environment = getEnvironment();
		return null == environment ? null : environment.getProperty(key, defaultValue);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param <T>          属性值类型
	 * @param key          配置项key
	 * @param targetType   配置项类型
	 * @param defaultValue 默认值
	 * @return 属性值
	 * @since 5.8.24
	 */
	public static <T> T getProperty(final String key, final Class<T> targetType, final T defaultValue) {
		final ConfigurableEnvironment environment = getEnvironment();
		return null == environment ? null : environment.getProperty(key, targetType, defaultValue);
	}

	/**
	 * 获取环境属性
	 *
	 * @return {@link ConfigurableEnvironment}
	 */
	public static ConfigurableEnvironment getEnvironment() {
		return null == applicationContext ? null : applicationContext.getEnvironment();
	}

	/**
	 * 获取应用程序名称
	 *
	 * @return 应用程序名称
	 * @since 5.7.12
	 */
	public static String getApplicationName() {
		return getProperty("spring.application.name");
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 *
	 * @return 当前的环境配置
	 * @since 5.3.3
	 */
	public static String[] getActiveProfiles() {
		if (null == applicationContext) {
			return null;
		}
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取当前的环境配置，当有多个环境配置时，只获取第一个
	 *
	 * @return 当前的环境配置
	 * @since 5.3.3
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 动态向Spring注册Bean
	 * <p>
	 * 由{@link org.springframework.beans.factory.BeanFactory} 实现，通过工具开放API
	 * <p>
	 * 更新: shadow 2021-07-29 17:20:44 增加自动注入，修复注册bean无法反向注入的问题
	 *
	 * @param <T>      Bean类型
	 * @param beanName 名称
	 * @param bean     bean
	 * @author shadow
	 * @since 5.4.2
	 */
	public static <T> void registerBean(final String beanName, final T bean) {
		final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		factory.autowireBean(bean);
		factory.registerSingleton(beanName, bean);
	}

	/**
	 * 注销bean
	 * <p>
	 * 将Spring中的bean注销，请谨慎使用
	 *
	 * @param beanName bean名称
	 * @author shadow
	 * @since 5.7.7
	 */
	public static void unregisterBean(final String beanName) {
		final ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		if (factory instanceof DefaultSingletonBeanRegistry) {
			final DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) factory;
			registry.destroySingleton(beanName);
		} else {
			throw new HutoolException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
		}
	}

	/**
	 * 发布事件
	 *
	 * @param event 待发布的事件，事件必须是{@link ApplicationEvent}的子类
	 * @since 5.7.12
	 */
	public static void publishEvent(final ApplicationEvent event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

	/**
	 * 发布事件
	 * Spring 4.2+ 版本事件可以不再是{@link ApplicationEvent}的子类
	 *
	 * @param event 待发布的事件
	 * @since 5.7.21
	 */
	public static void publishEvent(final Object event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}
}




