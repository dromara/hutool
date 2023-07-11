package cn.hutool.extra.spring.requestjson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * RequestJson解析器
 * Created by useheart on 2023/7/11
 * @author useheart
 */
public class RequestJsonMethodArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {
	protected static final String REQUEST_JSON_NAME = RequestJsonMethodArgumentResolver.class.getName();
	protected static final Object NO_VALUE = new Object();
	private final ObjectMapper objectMapper;

	public RequestJsonMethodArgumentResolver(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice, ObjectMapper objectMapper) {
		super(converters, requestResponseBodyAdvice);
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestJson.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object arg = webRequest.getAttribute(REQUEST_JSON_NAME, RequestAttributes.SCOPE_REQUEST);
		if (arg != null) {
			return arg;
		}
		parameter = parameter.nestedIfOptional();
		String name = Conventions.getVariableNameForParameter(parameter);
		Type nestedGenericParameterType = parameter.getNestedGenericParameterType();
		if (parameter.hasParameterAnnotation(RequestJson.class) && !nestedGenericParameterType.equals(JsonNode.class)) {
			throw new MethodArgumentTypeMismatchException(null, JsonNode.class, name, parameter,
					new Throwable("param class not equals: current class: " + nestedGenericParameterType + ",except class: " + JsonNode.class.getName()));
		}
		arg = readWithMessageConverters(webRequest, parameter, Map.class);
		arg = objectMapper.valueToTree(arg);

		handleDataBinder(name, arg, parameter, mavContainer, webRequest, binderFactory);

		Object adaptArg = adaptArgumentIfNecessary(arg, parameter);
		if (adaptArg == null) {
			adaptArg = NO_VALUE;
		}
		webRequest.setAttribute(REQUEST_JSON_NAME, adaptArg, RequestAttributes.SCOPE_REQUEST);
		return adaptArg;
	}

	protected void handleDataBinder(String name, Object value, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if (binderFactory != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, value, name);
			if (value != null) {
				validateIfApplicable(binder, parameter);
				if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
					throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
				}
			}
			if (mavContainer != null) {
				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
			}
		}
	}
}
