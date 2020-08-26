package cn.hutool.core.exceptions;

/**
 * 叶子容器未找到异常
 *
 * @author shadow
 * @version 5.4.1
 * @since 5.4.1
 */
public class LeafCollectionNotFoundException extends RuntimeException {

    /**
     * @param message message
     */
    public LeafCollectionNotFoundException(String message) {
        super(message);
    }
}
