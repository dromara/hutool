package cn.hutool.core.eventbus;

/**
 * @author unknowIfGuestInDream
 */
public class ApplicationPreparedEvent {
	String value;

	public ApplicationPreparedEvent(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
