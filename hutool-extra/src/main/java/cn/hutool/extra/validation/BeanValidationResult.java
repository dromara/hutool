package cn.hutool.extra.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * bean 校验结果
 *
 * @author chengqiang
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
	public BeanValidationResult(boolean success){
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public BeanValidationResult setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}

	public BeanValidationResult setErrorMessages(List<ErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
		return this;
	}

	public BeanValidationResult addErrorMessage(ErrorMessage errorMessage){
		this.errorMessages.add(errorMessage);
		return this;
	}

	/**
	 * 错误消息，包括字段名（字段路径）和消息内容
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

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return "ErrorMessage{" +
					"propertyName='" + propertyName + '\'' +
					", message='" + message + '\'' +
					'}';
		}
	}
}
