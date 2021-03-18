package cn.hutool.extra.flowcontrol;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author WangChen
 **/
public class FlowControlException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public FlowControlException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public FlowControlException(String message) {
        super(message);
    }

    public FlowControlException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public FlowControlException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FlowControlException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}
