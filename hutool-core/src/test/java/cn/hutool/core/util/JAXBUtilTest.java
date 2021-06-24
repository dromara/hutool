package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.annotation.*;

/**
 * {@link JAXBUtil} 工具类
 * 测试 xml 和 bean 互转工具类
 *
 * @author dazer
 */
public class JAXBUtilTest {

	private String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
		SchoolVo schoolVo = new SchoolVo();
		schoolVo.setSchoolName("西安市第一中学");
		schoolVo.setSchoolAddress("西安市雁塔区长安堡一号");

		SchoolVo.RoomVo roomVo = new SchoolVo.RoomVo();
		roomVo.setRoomName("101教室");
		roomVo.setRoomNo("101");
		schoolVo.setRoom(roomVo);

		Assert.assertEquals(xmlStr, JAXBUtil.beanToXml(schoolVo));
	}

	@Test
	public void xmlToBeanTest() {
		final SchoolVo schoolVo = JAXBUtil.xmlToBean(xmlStr, SchoolVo.class);
		Assert.assertNotNull(schoolVo);
		Assert.assertEquals("西安市第一中学", schoolVo.getSchoolName());
		Assert.assertEquals("西安市雁塔区长安堡一号", schoolVo.getSchoolAddress());

		Assert.assertEquals("101教室", schoolVo.getRoom().getRoomName());
		Assert.assertEquals("101", schoolVo.getRoom().getRoomNo());
	}

	@XmlRootElement(name = "school")
	@XmlAccessorType(XmlAccessType.FIELD)
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

		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
		}

		@XmlTransient
		public String getSchoolAddress() {
			return schoolAddress;
		}

		public void setSchoolAddress(String schoolAddress) {
			this.schoolAddress = schoolAddress;
		}

		@XmlTransient
		public RoomVo getRoom() {
			return room;
		}

		public void setRoom(RoomVo room) {
			this.room = room;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		public static final class RoomVo {
			@XmlElement(name = "room_no", required = true)
			private String roomNo;
			@XmlElement(name = "room_name", required = true)
			private String roomName;

			@XmlTransient
			public String getRoomNo() {
				return roomNo;
			}

			public void setRoomNo(String roomNo) {
				this.roomNo = roomNo;
			}

			@XmlTransient
			public String getRoomName() {
				return roomName;
			}

			public void setRoomName(String roomName) {
				this.roomName = roomName;
			}
		}
	}
}
