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
    private Boolean success = Boolean.TRUE;

    /**
     * 错误消息
     */
    private List<ErrorMessage> errorMessages = new ArrayList<>();

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ErrorMessage> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<ErrorMessage> errorMessages) {
        this.errorMessages = errorMessages;
    }

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
    }
}
