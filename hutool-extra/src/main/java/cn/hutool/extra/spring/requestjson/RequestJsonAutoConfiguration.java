package cn.hutool.extra.spring.requestjson;

import cn.hutool.core.exceptions.InvocationTargetRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @RequestJson 和 @RequestJsonParam 注解使用
 * 对于body content-type:application/x-www-form-urlencoded 可以通过 @RequestParam来获取对应值。
 * 但是对于body content-type:application/json 目前需要声明对应的类和@ReqeustBody来获取对应值。
 * 对于不同接口参数需要声明不同类来解决，且粒度类很大。
 * 因此RequestJsonAutoConfiguration旨在解决对应问题，可以通过使用@RequestJsonParam获取指定key-value，粒度更小，类似RequestParam获取对应值。
 * example:
 * json内容：{"a":"a", "b":"{c:1, "d":2}", "f":"{"g":2, "h":"1"}"}
 * public void test(@RequestJson JsonNode jsonNode, @RequestJsonParam(value="a", required=true) String a, @RequestJsonParam(value="b.c", required=false) Integer b, @RequestJsonParam(value="f") Object f1, @RequestJsonParam(value="f") Map<String, Object> f2, @RequestJsonParam(value="f") F f3) {
 *     return;
 * }
 * Created by useheart on 2023/7/11
 * @author useheart
 */
public class RequestJsonAutoConfiguration implements WebMvcConfigurer, ApplicationContextAware {
	public static final String OBJECT_MAPPER_BEAN_NAME = "RequestJsonAutoConfiguration.ObjectMapper";

	private ApplicationContext applicationContext;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 如果需要提高ObjectMapper反序列化的能力，可以在外部配置类重新定义
	 * */
	@Bean(name = RequestJsonAutoConfiguration.OBJECT_MAPPER_BEAN_NAME)
	@ConditionalOnMissingBean(name = RequestJsonAutoConfiguration.OBJECT_MAPPER_BEAN_NAME)
	public ObjectMapper objectMapper() {
		return new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 属性：messageConverters requestResponseAdvice
	 * RequestMappingHandlerAdapter中requestResponseAdvice为私有变量无法获取对应值，目前采用反射获取的。
	 * 其次RequestMappingHandlerAdapter在应用IOC中存在bean但是因为resolvers设置是实例化中的一个步骤，会导致循环问题。
	 * 因为目前采用WebMvcAutoConfiguration.EnableWebMvcConfiguration手工生成一个临时实例，是非标准实例化流程，
	 * 但是满足requestResponseAdvice和messageConverts两个变量的所有Bean自动加载。
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		WebMvcAutoConfiguration.EnableWebMvcConfiguration enableWebMvcConfiguration = applicationContext.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class);
		RequestMappingHandlerAdapter requestMappingHandlerAdapter = enableWebMvcConfiguration.requestMappingHandlerAdapter(new ContentNegotiationManager(), null, null);
		requestMappingHandlerAdapter.setApplicationContext(applicationContext);
		requestMappingHandlerAdapter.afterPropertiesSet();
		try {
			Field requestResponseBodyAdviceField = RequestMappingHandlerAdapter.class.getDeclaredField("requestResponseBodyAdvice");
			ReflectionUtils.makeAccessible(requestResponseBodyAdviceField);
			List<Object> requestResponseBodyAdvice = (List<Object>) requestResponseBodyAdviceField.get(requestMappingHandlerAdapter);
			ObjectMapper objectMapper = (ObjectMapper) applicationContext.getBean(RequestJsonAutoConfiguration.OBJECT_MAPPER_BEAN_NAME);
			resolvers.add(new RequestJsonMethodArgumentResolver(requestMappingHandlerAdapter.getMessageConverters(), requestResponseBodyAdvice, objectMapper));
			resolvers.add(new RequestJsonParamMethodArgumentResolver(requestMappingHandlerAdapter.getMessageConverters(), requestResponseBodyAdvice, objectMapper));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new InvocationTargetRuntimeException("Reflection inaccessible RequestMappingHandlerAdapter");
		}
	}
}
