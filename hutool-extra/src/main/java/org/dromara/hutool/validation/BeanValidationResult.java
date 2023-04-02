/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * bean 校验结果
 *
 * @author chengqiang
 * @since 5.5.0
 */
public class BeanValidationResult {
	/**
	 * 校验是否成功
	 */
	private boolean success;
	/**
	 * 错误消息
	 */
	private List<ErrorMessage> errorMessages = new ArrayList<>();

	/**
	 * 构造
	 *
	 * @param success 是否验证成功
	 */
	public BeanValidationResult(final boolean success) {
		this.success = success;
	}

	/**
	 * 是否验证通过
	 *
	 * @return 是否验证通过
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置是否通过
	 *
	 * @param success 是否通过
	 * @return this
	 */
	public BeanValidationResult setSuccess(final boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * 获取错误信息列表
	 *
	 * @return 错误信息列表
	 */
	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * 设置错误信息列表
	 *
	 * @param errorMessages 错误信息列表
	 * @return this
	 */
	public BeanValidationResult setErrorMessages(final List<ErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
		return this;
	}

	/**
	 * 增加错误信息
	 *
	 * @param errorMessage 错误信息
	 * @return this
	 */
	public BeanValidationResult addErrorMessage(final ErrorMessage errorMessage) {
		this.errorMessages.add(errorMessage);
		return this;
	}

	/**
	 * 错误消息，包括字段名（字段路径）、消息内容和字段值
	 */
	public static class ErrorMessage {
		/**
		 * 属性字段名称
		 */
		private String propertyName;
		/**
		 * 错误信息
		 */
		private String message;
		/**
		 * 错误值
		 */
		private Object value;

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(final String propertyName) {
			this.propertyName = propertyName;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(final String message) {
			this.message = message;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(final Object value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "ErrorMessage{" +
					"propertyName='" + propertyName + '\'' +
					", message='" + message + '\'' +
					", value=" + value +
					'}';
		}
	}
}
