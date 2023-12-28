/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.xml;

import jakarta.xml.bind.annotation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * {@link JAXBUtil} 工具类
 * 测试 xml 和 bean 互转工具类
 *
 * @author dazer
 */
public class JAXBUtilTest {

	private final String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
			"<school>\n" +
			"    <school_name>西安市第一中学</school_name>\n" +
			"    <school_address>西安市雁塔区长安堡一号</school_address>\n" +
			"    <room>\n" +
			"        <room_no>101</room_no>\n" +
			"        <room_name>101教室</room_name>\n" +
			"    </room>\n" +
			"</school>\n";

	@Test
	public void beanToXmlTest() {
		final SchoolVo schoolVo = new SchoolVo();
		schoolVo.setSchoolName("西安市第一中学");
		schoolVo.setSchoolAddress("西安市雁塔区长安堡一号");

		final SchoolVo.RoomVo roomVo = new SchoolVo.RoomVo();
		roomVo.setRoomName("101教室");
		roomVo.setRoomNo("101");
		schoolVo.setRoom(roomVo);

		Assertions.assertEquals(xmlStr, JAXBUtil.beanToXml(schoolVo));
	}

	@Test
	public void xmlToBeanTest() {
		final SchoolVo schoolVo = JAXBUtil.xmlToBean(xmlStr, SchoolVo.class);
		Assertions.assertNotNull(schoolVo);
		Assertions.assertEquals("西安市第一中学", schoolVo.getSchoolName());
		Assertions.assertEquals("西安市雁塔区长安堡一号", schoolVo.getSchoolAddress());

		Assertions.assertEquals("101教室", schoolVo.getRoom().getRoomName());
		Assertions.assertEquals("101", schoolVo.getRoom().getRoomNo());
	}

	@XmlRootElement(name = "school")
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(propOrder={"schoolName", "schoolAddress", "room"})
	public static class SchoolVo {
		@XmlElement(name = "school_name", required = true)
		private String schoolName;
		@XmlElement(name = "school_address", required = true)
		private String schoolAddress;
		@XmlElement(name = "room", required = true)
		private RoomVo room;

		@XmlTransient
		public String getSchoolName() {
			return schoolName;
		}

		public void setSchoolName(final String schoolName) {
			this.schoolName = schoolName;
		}

		@XmlTransient
		public String getSchoolAddress() {
			return schoolAddress;
		}

		public void setSchoolAddress(final String schoolAddress) {
			this.schoolAddress = schoolAddress;
		}

		@XmlTransient
		public RoomVo getRoom() {
			return room;
		}

		public void setRoom(final RoomVo room) {
			this.room = room;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(propOrder={"roomNo", "roomName"})
		public static final class RoomVo {
			@XmlElement(name = "room_no", required = true)
			private String roomNo;
			@XmlElement(name = "room_name", required = true)
			private String roomName;

			@XmlTransient
			public String getRoomNo() {
				return roomNo;
			}

			public void setRoomNo(final String roomNo) {
				this.roomNo = roomNo;
			}

			@XmlTransient
			public String getRoomName() {
				return roomName;
			}

			public void setRoomName(final String roomName) {
				this.roomName = roomName;
			}
		}
	}
}
