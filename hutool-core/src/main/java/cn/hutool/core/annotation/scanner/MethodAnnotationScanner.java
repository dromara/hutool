package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 扫描{@link Method}上的注解
 *
 * @author huangchengxing
 */
public class MethodAnnotationScanner implements AnnotationScanner {

	@Override
	public boolean support(AnnotatedElement annotatedElement) {
		return annotatedElement instanceof Method;
	}

	@Override
	public List<Annotation> getAnnotations(AnnotatedElement annotatedElement) {
		return CollUtil.newArrayList(annotatedElement.getAnnotations());
	}

}
