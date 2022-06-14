package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 扫描{@link Field}上的注解
 *
 * @author huangchengxing
 */
public class FieldAnnotationScanner implements AnnotationScanner {

	@Override
	public boolean support(AnnotatedElement annotatedElement) {
		return annotatedElement instanceof Field;
	}

	@Override
	public List<Annotation> getAnnotations(AnnotatedElement annotatedElement) {
		return CollUtil.newArrayList(annotatedElement.getAnnotations());
	}

}
