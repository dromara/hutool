package cn.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAOPI9Test {
	@Test
	void toBeanTest() {
		String jsonStr = "{\"chatCommandType\":\"CHAT_MSG\"}";
		ChatPublicAppReceiveMq mqChatRequest = JSONUtil.toBean(jsonStr, ChatPublicAppReceiveMq.class);
		Assertions.assertEquals("CHAT_MSG", mqChatRequest.getChatCommandType().name());
	}

	@Data
	static class ChatPublicAppReceiveMq {
		private ChatAppMqCommandTypeEnum chatCommandType;
	}

	public enum ChatAppMqCommandTypeEnum {
		/**
		 * 对话消息
		 */
		CHAT_MSG("chat_msg"),

		/**
		 * 对话消息(批量-群发)
		 */
		CHAT_MSG_BATCH("chat_msg_batch"),
		/**
		 * 命令消息
		 */
		COMMAND_MSG("command_msg")
		;
		ChatAppMqCommandTypeEnum(String type) {
			this.type = type;
		}

		private String type;
		public String getType() {
			return type;
		}
	}
}
