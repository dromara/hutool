package cn.hutool.core.eventbus;

/**
 * @author unknowIfGuestInDream
 */
public class ApplicationReadyEvent {
	String value;

	public ApplicationReadyEvent(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
