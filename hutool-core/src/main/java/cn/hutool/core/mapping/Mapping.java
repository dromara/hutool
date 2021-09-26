package cn.hutool.core.mapping;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author XueRi
 * @since 2021/9/26
 */
public class Mapping {

	protected static <T, R> R createInstance(T source, Class<R> target) {
		R instance;
		try {
			instance = target.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		Set<Field> declaredFields = new HashSet<>();
		getSourceFields(source.getClass(), declaredFields);
		HashMap<String, Field> fieldMap = new HashMap<>();
		getInstanceFields(target, fieldMap);

		for (Field field : declaredFields) {
			if (fieldMap.containsKey(field.getName())) {
				Field instanceField = fieldMap.get(field.getName());
				if (field.getType().equals(instanceField.getType())) {
					field.setAccessible(true);
					instanceField.setAccessible(true);
					try {
						Object val = field.get(source);
						instanceField.set(instance, val);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}

	private static <T> void getSourceFields(Class<T> sourceCla, Set<Field> declaredFields) {
		boolean serialize = false;
		// Verify that a parent class exists
		if (!Object.class.equals(sourceCla.getSuperclass())) {
			getSourceFields(sourceCla.getSuperclass(), declaredFields);
		}
		Class<?>[] interfaces = sourceCla.getInterfaces();
		for (Class<?> inter : interfaces) {
			if (Serializable.class.equals(inter)) {
				serialize = true;
				break;
			}
		}
		Field[] fields = sourceCla.getDeclaredFields();
		// Static property does not map
		for (Field field : fields) {
			if (serialize) {
				if (!Modifier.isStatic(field.getModifiers()) || !field.getName().contains("serial")) {
					declaredFields.add(field);
				}
			} else {
				if (!Modifier.isStatic(field.getModifiers())) {
					declaredFields.add(field);
				}
			}
		}
	}

	private static <T> void getInstanceFields(Class<?> instanceCla, HashMap<String, Field> declaredFields) {
		if (!Object.class.equals(instanceCla.getSuperclass())) {
			getInstanceFields(instanceCla.getSuperclass(), declaredFields);
		}
		Field[] fields = instanceCla.getDeclaredFields();
		for (Field field : fields) {
			// Static property does not map
			if (!Modifier.isStatic(field.getModifiers())) {
				declaredFields.put(field.getName(), field);
			}
		}
	}

	protected static <T, R> void getInstanceCollection(Collection<R> targetCollection, Collection<T> source, Class<R> target, BiConsumer<T, R> biConsumer) {
		for (T t : source) {
			R instance = createInstance(t, target);
			if (!Objects.isNull(biConsumer)) {
				biConsumer.accept(t, instance);
			}
			targetCollection.add(instance);
		}
	}
}
