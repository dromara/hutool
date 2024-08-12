/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.extra.validation;

import org.dromara.hutool.core.collection.CollUtil;

import jakarta.validation.*;

import java.util.Set;


/**
 * java bean 校验工具类，此工具类基于validation-api（jakarta.validation-api）封装
 *
 * <p>在实际使用中，用户需引入validation-api的实现，如：hibernate-validator</p>
 * <p>注意：hibernate-validator还依赖了javax.el，需自行引入。</p>
 *
 * @author chengqiang
 * @since 5.5.0
 */
public class ValidationUtil {
	/**
	 * 默认{@link Validator} 对象
	 */
	private static final Validator validator;

	static {
		try (final ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			validator = factory.getValidator();
		}
	}

	/**
	 * 获取原生{@link Validator} 对象
	 *
	 * @return {@link Validator} 对象
	 */
	public static Validator getValidator() {
		return validator;
	}

	/**
	 * 校验对象
	 *
	 * @param <T>    Bean类型
	 * @param bean   bean
	 * @param groups 校验组
	 * @return {@link Set}
	 */
	public static <T> Set<ConstraintViolation<T>> validate(final T bean, final Class<?>... groups) {
		return validator.validate(bean, groups);
	}

	/**
	 * 校验对象,校验不通过，直接抛出给调用者
	 * 说明：如果Bean对象内部有非基本类型对象，需要把内部对象取出，进行手动多次调用,本方法
	 *
	 * @param object 待校验对象
	 * @param groups 待校验的组
	 * @throws ValidationException 校验不通过，则报 ValidationException 异常，调用者进行捕获，直接响应给前端用户
	 */
	public static void validateAndThrowFirst(final Object object, final Class<?>... groups)
			throws ValidationException {
		final Set<ConstraintViolation<Object>> constraintViolations = validate(object, groups);
		if (CollUtil.isNotEmpty(constraintViolations)) {
			final ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
			throw new ValidationException(constraint.getMessage());
		}
	}

	/**
	 * 校验bean的某一个属性
	 *
	 * @param <T>          Bean类型
	 * @param bean         bean
	 * @param propertyName 属性名称
	 * @param groups       验证分组
	 * @return {@link Set}
	 */
	public static <T> Set<ConstraintViolation<T>> validateProperty(final T bean, final String propertyName, final Class<?>... groups) {
		return validator.validateProperty(bean, propertyName, groups);
	}

	/**
	 * 校验对象
	 *
	 * @param <T>    Bean类型
	 * @param bean   bean
	 * @param groups 校验组
	 * @return {@link BeanValidationResult}
	 */
	public static <T> BeanValidationResult warpValidate(final T bean, final Class<?>... groups) {
		return warpBeanValidationResult(validate(bean, groups));
	}

	/**
	 * 校验bean的某一个属性
	 *
	 * @param <T>          bean类型
	 * @param bean         bean
	 * @param propertyName 属性名称
	 * @param groups       验证分组
	 * @return {@link BeanValidationResult}
	 */
	public static <T> BeanValidationResult warpValidateProperty(final T bean, final String propertyName, final Class<?>... groups) {
		return warpBeanValidationResult(validateProperty(bean, propertyName, groups));
	}

	/**
	 * 包装校验结果
	 *
	 * @param constraintViolations 校验结果集
	 * @return {@link BeanValidationResult}
	 */
	private static <T> BeanValidationResult warpBeanValidationResult(final Set<ConstraintViolation<T>> constraintViolations) {
		final BeanValidationResult result = new BeanValidationResult(constraintViolations.isEmpty());
		for (final ConstraintViolation<T> constraintViolation : constraintViolations) {
			final BeanValidationResult.ErrorMessage errorMessage = new BeanValidationResult.ErrorMessage();
			errorMessage.setPropertyName(constraintViolation.getPropertyPath().toString());
			errorMessage.setMessage(constraintViolation.getMessage());
			errorMessage.setValue(constraintViolation.getInvalidValue());
			result.addErrorMessage(errorMessage);
		}
		return result;
	}

}
