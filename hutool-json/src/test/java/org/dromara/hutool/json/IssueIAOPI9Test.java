/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAOPI9Test {
	@Test
	void toBeanTest() {
		final String jsonStr = "{\"chatCommandType\":\"CHAT_MSG\"}";
		final ChatPublicAppReceiveMq mqChatRequest = JSONUtil.toBean(jsonStr, ChatPublicAppReceiveMq.class);
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
		ChatAppMqCommandTypeEnum(final String type) {
			this.type = type;
		}

		private final String type;
		public String getType() {
			return type;
		}
	}
}
