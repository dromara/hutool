/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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
