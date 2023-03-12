package cn.hutool.core.io;

import cn.hutool.core.util.ArrayUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * 文件类型魔数封装
 *
 * @author CherryRum
 * @since 5.8.12
 */
public enum FileMagicNumber {
	UNKNOWN(null, null) {
		@Override
		public boolean match(final byte[] bytes) {
			return false;
		}
	},
	//image start---------------------------------------------
	JPEG("image/jpeg", "jpg") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 2
					&& Objects.equals(bytes[0], (byte) 0xff)
					&& Objects.equals(bytes[1], (byte) 0xd8)
					&& Objects.equals(bytes[2], (byte) 0xff);
		}
	},
	JXR("image/vnd.ms-photo", "jxr") {
		@Override
		public boolean match(final byte[] bytes) {
			//file magic number https://www.iana.org/assignments/media-types/image/jxr
			return bytes.length > 2
					&& Objects.equals(bytes[0], (byte) 0x49)
					&& Objects.equals(bytes[1], (byte) 0x49)
					&& Objects.equals(bytes[2], (byte) 0xbc);
		}
	},
	APNG("image/apng", "apng") {
		@Override
		public boolean match(final byte[] bytes) {
			final boolean b = bytes.length > 8
					&& Objects.equals(bytes[0], (byte) 0x89)
					&& Objects.equals(bytes[1], (byte) 0x50)
					&& Objects.equals(bytes[2], (byte) 0x4e)
					&& Objects.equals(bytes[3], (byte) 0x47)
					&& Objects.equals(bytes[4], (byte) 0x0d)
					&& Objects.equals(bytes[5], (byte) 0x0a)
					&& Objects.equals(bytes[6], (byte) 0x1a)
					&& Objects.equals(bytes[7], (byte) 0x0a);

			if (b) {
				int i = 8;
				while (i < bytes.length) {
					try {
						final int dataLength = new BigInteger(1, Arrays.copyOfRange(bytes, i, i + 4)).intValue();
						i += 4;
						final byte[] bytes1 = Arrays.copyOfRange(bytes, i, i + 4);
						final String chunkType = new String(bytes1);
						i += 4;
						if (Objects.equals(chunkType, "IDAT") || Objects.equals(chunkType, "IEND")) {
							return false;
						} else if (Objects.equals(chunkType, "acTL")) {
							return true;
						}
						i += dataLength + 4;
					} catch (final Exception e) {
						return false;
					}
				}
			}
			return false;
		}
	},
	PNG("image/png", "png") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x89)
					&& Objects.equals(bytes[1], (byte) 0x50)
					&& Objects.equals(bytes[2], (byte) 0x4e)
					&& Objects.equals(bytes[3], (byte) 0x47);
		}
	},
	GIF("image/gif", "gif") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 2
					&& Objects.equals(bytes[0], (byte) 0x47)
					&& Objects.equals(bytes[1], (byte) 0x49)
					&& Objects.equals(bytes[2], (byte) 0x46);
		}
	},
	BMP("image/bmp", "bmp") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 1
					&& Objects.equals(bytes[0], (byte) 0x42)
					&& Objects.equals(bytes[1], (byte) 0x4d);
		}
	},
	TIFF("image/tiff", "tiff") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 4) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0x49)
					&& Objects.equals(bytes[1], (byte) 0x49)
					&& Objects.equals(bytes[2], (byte) 0x2a)
					&& Objects.equals(bytes[3], (byte) 0x00);
			final boolean flag2 = (Objects.equals(bytes[0], (byte) 0x4d)
					&& Objects.equals(bytes[1], (byte) 0x4d)
					&& Objects.equals(bytes[2], (byte) 0x00)
					&& Objects.equals(bytes[3], (byte) 0x2a));
			return flag1 || flag2;

		}
	},

	DWG("image/vnd.dwg", "dwg") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 10
					&& Objects.equals(bytes[0], (byte) 0x41)
					&& Objects.equals(bytes[1], (byte) 0x43)
					&& Objects.equals(bytes[2], (byte) 0x31)
					&& Objects.equals(bytes[3], (byte) 0x30);
		}
	},

	WEBP("image/webp", "webp") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 11
					&& Objects.equals(bytes[8], (byte) 0x57)
					&& Objects.equals(bytes[9], (byte) 0x45)
					&& Objects.equals(bytes[10], (byte) 0x42)
					&& Objects.equals(bytes[11], (byte) 0x50);
		}
	},
	PSD("image/vnd.adobe.photoshop", "psd") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x38)
					&& Objects.equals(bytes[1], (byte) 0x42)
					&& Objects.equals(bytes[2], (byte) 0x50)
					&& Objects.equals(bytes[3], (byte) 0x53);
		}
	},
	ICO("image/x-icon", "ico") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x00)
					&& Objects.equals(bytes[1], (byte) 0x00)
					&& Objects.equals(bytes[2], (byte) 0x01)
					&& Objects.equals(bytes[3], (byte) 0x00);
		}
	},
	XCF("image/x-xcf", "xcf") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 9
					&& Objects.equals(bytes[0], (byte) 0x67)
					&& Objects.equals(bytes[1], (byte) 0x69)
					&& Objects.equals(bytes[2], (byte) 0x6d)
					&& Objects.equals(bytes[3], (byte) 0x70)
					&& Objects.equals(bytes[4], (byte) 0x20)
					&& Objects.equals(bytes[5], (byte) 0x78)
					&& Objects.equals(bytes[6], (byte) 0x63)
					&& Objects.equals(bytes[7], (byte) 0x66)
					&& Objects.equals(bytes[8], (byte) 0x20)
					&& Objects.equals(bytes[9], (byte) 0x76);
		}
	},
	//image end-----------------------------------------------

	//audio start---------------------------------------------

	WAV("audio/x-wav", "wav") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 11
					&& Objects.equals(bytes[0], (byte) 0x52)
					&& Objects.equals(bytes[1], (byte) 0x49)
					&& Objects.equals(bytes[2], (byte) 0x46)
					&& Objects.equals(bytes[3], (byte) 0x46)
					&& Objects.equals(bytes[8], (byte) 0x57)
					&& Objects.equals(bytes[9], (byte) 0x41)
					&& Objects.equals(bytes[10], (byte) 0x56)
					&& Objects.equals(bytes[11], (byte) 0x45);
		}
	},
	MIDI("audio/midi", "midi") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x4d)
					&& Objects.equals(bytes[1], (byte) 0x54)
					&& Objects.equals(bytes[2], (byte) 0x68)
					&& Objects.equals(bytes[3], (byte) 0x64);
		}
	},
	MP3("audio/mpeg", "mp3") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 2) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0x49) && Objects.equals(bytes[1], (byte) 0x44) && Objects.equals(bytes[2], (byte) 0x33);
			final boolean flag2 = Objects.equals(bytes[0], (byte) 0xFF) && Objects.equals(bytes[1], (byte) 0xFB);
			final boolean flag3 = Objects.equals(bytes[0], (byte) 0xFF) && Objects.equals(bytes[1], (byte) 0xF3);
			final boolean flag4 = Objects.equals(bytes[0], (byte) 0xFF) && Objects.equals(bytes[1], (byte) 0xF2);
			return flag1 || flag2 || flag3 || flag4;
		}
	},
	OGG("audio/ogg", "ogg") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x4f)
					&& Objects.equals(bytes[1], (byte) 0x67)
					&& Objects.equals(bytes[2], (byte) 0x67)
					&& Objects.equals(bytes[3], (byte) 0x53);
		}
	},
	FLAC("audio/x-flac", "flac") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x66)
					&& Objects.equals(bytes[1], (byte) 0x4c)
					&& Objects.equals(bytes[2], (byte) 0x61)
					&& Objects.equals(bytes[3], (byte) 0x43);
		}
	},
	M4A("audio/mp4", "m4a") {
		@Override
		public boolean match(final byte[] bytes) {
			return (bytes.length > 10
					&& Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x74)
					&& Objects.equals(bytes[6], (byte) 0x79)
					&& Objects.equals(bytes[7], (byte) 0x70)
					&& Objects.equals(bytes[8], (byte) 0x4d)
					&& Objects.equals(bytes[9], (byte) 0x34)
					&& Objects.equals(bytes[10], (byte) 0x41))
					|| (Objects.equals(bytes[0], (byte) 0x4d)
					&& Objects.equals(bytes[1], (byte) 0x34)
					&& Objects.equals(bytes[2], (byte) 0x41)
					&& Objects.equals(bytes[3], (byte) 0x20));
		}
	},
	AAC("audio/aac", "aac") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 1) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0xFF) && Objects.equals(bytes[1], (byte) 0xF1);
			final boolean flag2 = Objects.equals(bytes[0], (byte) 0xFF) && Objects.equals(bytes[1], (byte) 0xF9);
			return flag1 || flag2;
		}
	},
	AMR("audio/amr", "amr") {
		@Override
		public boolean match(final byte[] bytes) {
			//single-channel
			if (bytes.length < 11) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0x23)
					&& Objects.equals(bytes[1], (byte) 0x21)
					&& Objects.equals(bytes[2], (byte) 0x41)
					&& Objects.equals(bytes[3], (byte) 0x4d)
					&& Objects.equals(bytes[4], (byte) 0x52)
					&& Objects.equals(bytes[5], (byte) 0x0A);
			//multi-channel:
			final boolean flag2 = Objects.equals(bytes[0], (byte) 0x23)
					&& Objects.equals(bytes[1], (byte) 0x21)
					&& Objects.equals(bytes[2], (byte) 0x41)
					&& Objects.equals(bytes[3], (byte) 0x4d)
					&& Objects.equals(bytes[4], (byte) 0x52)
					&& Objects.equals(bytes[5], (byte) 0x5F)
					&& Objects.equals(bytes[6], (byte) 0x4d)
					&& Objects.equals(bytes[7], (byte) 0x43)
					&& Objects.equals(bytes[8], (byte) 0x31)
					&& Objects.equals(bytes[9], (byte) 0x2e)
					&& Objects.equals(bytes[10], (byte) 0x30)
					&& Objects.equals(bytes[11], (byte) 0x0a);
			return flag1 || flag2;
		}
	},
	AC3("audio/ac3", "ac3") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 2
					&& Objects.equals(bytes[0], (byte) 0x0b)
					&& Objects.equals(bytes[1], (byte) 0x77);
		}
	},
	AIFF("audio/x-aiff", "aiff") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 11
					&& Objects.equals(bytes[0], (byte) 0x46)
					&& Objects.equals(bytes[1], (byte) 0x4f)
					&& Objects.equals(bytes[2], (byte) 0x52)
					&& Objects.equals(bytes[3], (byte) 0x4d)
					&& Objects.equals(bytes[8], (byte) 0x41)
					&& Objects.equals(bytes[9], (byte) 0x49)
					&& Objects.equals(bytes[10], (byte) 0x46)
					&& Objects.equals(bytes[11], (byte) 0x46);
		}
	},
	//audio end-----------------------------------------------

	//font start---------------------------------------------
	// The existing registration "application/font-woff" is deprecated in favor of "font/woff".
	WOFF("font/woff", "woff") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 8) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0x77)
					&& Objects.equals(bytes[1], (byte) 0x4f)
					&& Objects.equals(bytes[2], (byte) 0x46)
					&& Objects.equals(bytes[3], (byte) 0x46);
			final boolean flag2 = Objects.equals(bytes[4], (byte) 0x00)
					&& Objects.equals(bytes[5], (byte) 0x01)
					&& Objects.equals(bytes[6], (byte) 0x00)
					&& Objects.equals(bytes[7], (byte) 0x00);
			final boolean flag3 = Objects.equals(bytes[4], (byte) 0x4f)
					&& Objects.equals(bytes[5], (byte) 0x54)
					&& Objects.equals(bytes[6], (byte) 0x54)
					&& Objects.equals(bytes[7], (byte) 0x4f);
			final boolean flag4 = Objects.equals(bytes[4], (byte) 0x74)
					&& Objects.equals(bytes[5], (byte) 0x72)
					&& Objects.equals(bytes[6], (byte) 0x75)
					&& Objects.equals(bytes[7], (byte) 0x65);
			return flag1 && (flag2 || flag3 || flag4);
		}
	},
	WOFF2("font/woff2", "woff2") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 8) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0x77)
					&& Objects.equals(bytes[1], (byte) 0x4f)
					&& Objects.equals(bytes[2], (byte) 0x46)
					&& Objects.equals(bytes[3], (byte) 0x32);
			final boolean flag2 = Objects.equals(bytes[4], (byte) 0x00)
					&& Objects.equals(bytes[5], (byte) 0x01)
					&& Objects.equals(bytes[6], (byte) 0x00)
					&& Objects.equals(bytes[7], (byte) 0x00);
			final boolean flag3 = Objects.equals(bytes[4], (byte) 0x4f)
					&& Objects.equals(bytes[5], (byte) 0x54)
					&& Objects.equals(bytes[6], (byte) 0x54)
					&& Objects.equals(bytes[7], (byte) 0x4f);
			final boolean flag4 = Objects.equals(bytes[4], (byte) 0x74)
					&& Objects.equals(bytes[5], (byte) 0x72)
					&& Objects.equals(bytes[6], (byte) 0x75)
					&& Objects.equals(bytes[7], (byte) 0x65);
			return flag1 && (flag2 || flag3 || flag4);
		}
	},
	TTF("font/ttf", "ttf") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 4
					&& Objects.equals(bytes[0], (byte) 0x00)
					&& Objects.equals(bytes[1], (byte) 0x01)
					&& Objects.equals(bytes[2], (byte) 0x00)
					&& Objects.equals(bytes[3], (byte) 0x00)
					&& Objects.equals(bytes[4], (byte) 0x00);
		}
	},
	OTF("font/otf", "otf") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 4
					&& Objects.equals(bytes[0], (byte) 0x4f)
					&& Objects.equals(bytes[1], (byte) 0x54)
					&& Objects.equals(bytes[2], (byte) 0x54)
					&& Objects.equals(bytes[3], (byte) 0x4f)
					&& Objects.equals(bytes[4], (byte) 0x00);
		}
	},

	//font end-----------------------------------------------

	//archive start-----------------------------------------
	EPUB("application/epub+zip", "epub") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 58
					&& Objects.equals(bytes[0], (byte) 0x50) && Objects.equals(bytes[1], (byte) 0x4b)
					&& Objects.equals(bytes[2], (byte) 0x03) && Objects.equals(bytes[3], (byte) 0x04)
					&& Objects.equals(bytes[30], (byte) 0x6d) && Objects.equals(bytes[31], (byte) 0x69)
					&& Objects.equals(bytes[32], (byte) 0x6d) && Objects.equals(bytes[33], (byte) 0x65)
					&& Objects.equals(bytes[34], (byte) 0x74) && Objects.equals(bytes[35], (byte) 0x79)
					&& Objects.equals(bytes[36], (byte) 0x70) && Objects.equals(bytes[37], (byte) 0x65)
					&& Objects.equals(bytes[38], (byte) 0x61) && Objects.equals(bytes[39], (byte) 0x70)
					&& Objects.equals(bytes[40], (byte) 0x70) && Objects.equals(bytes[41], (byte) 0x6c)
					&& Objects.equals(bytes[42], (byte) 0x69) && Objects.equals(bytes[43], (byte) 0x63)
					&& Objects.equals(bytes[44], (byte) 0x61) && Objects.equals(bytes[45], (byte) 0x74)
					&& Objects.equals(bytes[46], (byte) 0x69) && Objects.equals(bytes[47], (byte) 0x6f)
					&& Objects.equals(bytes[48], (byte) 0x6e) && Objects.equals(bytes[49], (byte) 0x2f)
					&& Objects.equals(bytes[50], (byte) 0x65) && Objects.equals(bytes[51], (byte) 0x70)
					&& Objects.equals(bytes[52], (byte) 0x75) && Objects.equals(bytes[53], (byte) 0x62)
					&& Objects.equals(bytes[54], (byte) 0x2b) && Objects.equals(bytes[55], (byte) 0x7a)
					&& Objects.equals(bytes[56], (byte) 0x69) && Objects.equals(bytes[57], (byte) 0x70);
		}
	},
	ZIP("application/zip", "zip") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 4) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0x50) && Objects.equals(bytes[1], (byte) 0x4b);
			final boolean flag2 = Objects.equals(bytes[2], (byte) 0x03) || Objects.equals(bytes[2], (byte) 0x05) || Objects.equals(bytes[2], (byte) 0x07);
			final boolean flag3 = Objects.equals(bytes[3], (byte) 0x04) || Objects.equals(bytes[3], (byte) 0x06) || Objects.equals(bytes[3], (byte) 0x08);
			return flag1 && flag2 && flag3;
		}
	},
	TAR("application/x-tar", "tar") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 261
					&& Objects.equals(bytes[257], (byte) 0x75)
					&& Objects.equals(bytes[258], (byte) 0x73)
					&& Objects.equals(bytes[259], (byte) 0x74)
					&& Objects.equals(bytes[260], (byte) 0x61)
					&& Objects.equals(bytes[261], (byte) 0x72);
		}
	},
	RAR("application/x-rar-compressed", "rar") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 6
					&& Objects.equals(bytes[0], (byte) 0x52)
					&& Objects.equals(bytes[1], (byte) 0x61)
					&& Objects.equals(bytes[2], (byte) 0x72)
					&& Objects.equals(bytes[3], (byte) 0x21)
					&& Objects.equals(bytes[4], (byte) 0x1a)
					&& Objects.equals(bytes[5], (byte) 0x07)
					&& (Objects.equals(bytes[6], (byte) 0x00) || Objects.equals(bytes[6], (byte) 0x01));
		}
	},
	GZ("application/gzip", "gz") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 2
					&& Objects.equals(bytes[0], (byte) 0x1f)
					&& Objects.equals(bytes[1], (byte) 0x8b)
					&& Objects.equals(bytes[2], (byte) 0x08);
		}
	},
	BZ2("application/x-bzip2", "bz2") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 2
					&& Objects.equals(bytes[0], (byte) 0x42)
					&& Objects.equals(bytes[1], (byte) 0x5a)
					&& Objects.equals(bytes[2], (byte) 0x68);
		}
	},
	SevenZ("application/x-7z-compressed", "7z") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 6
					&& Objects.equals(bytes[0], (byte) 0x37)
					&& Objects.equals(bytes[1], (byte) 0x7a)
					&& Objects.equals(bytes[2], (byte) 0xbc)
					&& Objects.equals(bytes[3], (byte) 0xaf)
					&& Objects.equals(bytes[4], (byte) 0x27)
					&& Objects.equals(bytes[5], (byte) 0x1c)
					&& Objects.equals(bytes[6], (byte) 0x00);
		}
	},
	PDF("application/pdf", "pdf") {
		@Override
		public boolean match(byte[] bytes) {
			//去除bom头并且跳过三个字节
			if (bytes.length > 3 && Objects.equals(bytes[0], (byte) 0xEF)
					&& Objects.equals(bytes[1], (byte) 0xBB) && Objects.equals(bytes[2], (byte) 0xBF)) {
				bytes = Arrays.copyOfRange(bytes, 3, bytes.length);
			}
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x25)
					&& Objects.equals(bytes[1], (byte) 0x50)
					&& Objects.equals(bytes[2], (byte) 0x44)
					&& Objects.equals(bytes[3], (byte) 0x46);
		}
	},
	EXE("application/x-msdownload", "exe") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 1
					&& Objects.equals(bytes[0], (byte) 0x4d)
					&& Objects.equals(bytes[1], (byte) 0x5a);
		}
	},
	SWF("application/x-shockwave-flash", "swf") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 2
					&& (Objects.equals(bytes[0], 0x43) || Objects.equals(bytes[0], (byte) 0x46))
					&& Objects.equals(bytes[1], (byte) 0x57)
					&& Objects.equals(bytes[2], (byte) 0x53);
		}
	},
	RTF("application/rtf", "rtf") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 4
					&& Objects.equals(bytes[0], (byte) 0x7b)
					&& Objects.equals(bytes[1], (byte) 0x5c)
					&& Objects.equals(bytes[2], (byte) 0x72)
					&& Objects.equals(bytes[3], (byte) 0x74)
					&& Objects.equals(bytes[4], (byte) 0x66);
		}
	},
	NES("application/x-nintendo-nes-rom", "nes") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x4e)
					&& Objects.equals(bytes[1], (byte) 0x45)
					&& Objects.equals(bytes[2], (byte) 0x53)
					&& Objects.equals(bytes[3], (byte) 0x1a);
		}
	},
	CRX("application/x-google-chrome-extension", "crx") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x43)
					&& Objects.equals(bytes[1], (byte) 0x72)
					&& Objects.equals(bytes[2], (byte) 0x32)
					&& Objects.equals(bytes[3], (byte) 0x34);
		}
	},
	CAB("application/vnd.ms-cab-compressed", "cab") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 4) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[0], (byte) 0x4d) && Objects.equals(bytes[1], (byte) 0x53)
					&& Objects.equals(bytes[2], (byte) 0x43) && Objects.equals(bytes[3], (byte) 0x46);
			final boolean flag2 = Objects.equals(bytes[0], (byte) 0x49) && Objects.equals(bytes[1], (byte) 0x53)
					&& Objects.equals(bytes[2], (byte) 0x63) && Objects.equals(bytes[3], (byte) 0x28);
			return flag1 || flag2;
		}
	},
	PS("application/postscript", "ps") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 1
					&& Objects.equals(bytes[0], (byte) 0x25)
					&& Objects.equals(bytes[1], (byte) 0x21);
		}
	},
	XZ("application/x-xz", "xz") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 5
					&& Objects.equals(bytes[0], (byte) 0xFD)
					&& Objects.equals(bytes[1], (byte) 0x37)
					&& Objects.equals(bytes[2], (byte) 0x7A)
					&& Objects.equals(bytes[3], (byte) 0x58)
					&& Objects.equals(bytes[4], (byte) 0x5A)
					&& Objects.equals(bytes[5], (byte) 0x00);
		}
	},
	SQLITE("application/x-sqlite3", "sqlite") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 15
					&& Objects.equals(bytes[0], (byte) 0x53) && Objects.equals(bytes[1], (byte) 0x51)
					&& Objects.equals(bytes[2], (byte) 0x4c) && Objects.equals(bytes[3], (byte) 0x69)
					&& Objects.equals(bytes[4], (byte) 0x74) && Objects.equals(bytes[5], (byte) 0x65)
					&& Objects.equals(bytes[6], (byte) 0x20) && Objects.equals(bytes[7], (byte) 0x66)
					&& Objects.equals(bytes[8], (byte) 0x6f) && Objects.equals(bytes[9], (byte) 0x72)
					&& Objects.equals(bytes[10], (byte) 0x6d) && Objects.equals(bytes[11], (byte) 0x61)
					&& Objects.equals(bytes[12], (byte) 0x74) && Objects.equals(bytes[13], (byte) 0x20)
					&& Objects.equals(bytes[14], (byte) 0x33) && Objects.equals(bytes[15], (byte) 0x00);
		}
	},
	DEB("application/x-deb", "deb") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 20
					&& Objects.equals(bytes[0], (byte) 0x21) && Objects.equals(bytes[1], (byte) 0x3c)
					&& Objects.equals(bytes[2], (byte) 0x61) && Objects.equals(bytes[3], (byte) 0x72)
					&& Objects.equals(bytes[4], (byte) 0x63) && Objects.equals(bytes[5], (byte) 0x68)
					&& Objects.equals(bytes[6], (byte) 0x3e) && Objects.equals(bytes[7], (byte) 0x0a)
					&& Objects.equals(bytes[8], (byte) 0x64) && Objects.equals(bytes[9], (byte) 0x65)
					&& Objects.equals(bytes[10], (byte) 0x62) && Objects.equals(bytes[11], (byte) 0x69)
					&& Objects.equals(bytes[12], (byte) 0x61) && Objects.equals(bytes[13], (byte) 0x6e)
					&& Objects.equals(bytes[14], (byte) 0x2d) && Objects.equals(bytes[15], (byte) 0x62)
					&& Objects.equals(bytes[16], (byte) 0x69) && Objects.equals(bytes[17], (byte) 0x6e)
					&& Objects.equals(bytes[18], (byte) 0x61) && Objects.equals(bytes[19], (byte) 0x72)
					&& Objects.equals(bytes[20], (byte) 0x79);
		}
	},
	AR("application/x-unix-archive", "ar") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 6
					&& Objects.equals(bytes[0], (byte) 0x21)
					&& Objects.equals(bytes[1], (byte) 0x3c)
					&& Objects.equals(bytes[2], (byte) 0x61)
					&& Objects.equals(bytes[3], (byte) 0x72)
					&& Objects.equals(bytes[4], (byte) 0x63)
					&& Objects.equals(bytes[5], (byte) 0x68)
					&& Objects.equals(bytes[6], (byte) 0x3e);
		}
	},
	LZOP("application/x-lzop", "lzo") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 7
					&& Objects.equals(bytes[0], (byte) 0x89)
					&& Objects.equals(bytes[1], (byte) 0x4c)
					&& Objects.equals(bytes[2], (byte) 0x5a)
					&& Objects.equals(bytes[3], (byte) 0x4f)
					&& Objects.equals(bytes[4], (byte) 0x00)
					&& Objects.equals(bytes[5], (byte) 0x0d)
					&& Objects.equals(bytes[6], (byte) 0x0a)
					&& Objects.equals(bytes[7], (byte) 0x1a);
		}
	},
	LZ("application/x-lzip", "lz") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x4c)
					&& Objects.equals(bytes[1], (byte) 0x5a)
					&& Objects.equals(bytes[2], (byte) 0x49)
					&& Objects.equals(bytes[3], (byte) 0x50);
		}
	},
	ELF("application/x-executable", "elf") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 52
					&& Objects.equals(bytes[0], (byte) 0x7f)
					&& Objects.equals(bytes[1], (byte) 0x45)
					&& Objects.equals(bytes[2], (byte) 0x4c)
					&& Objects.equals(bytes[3], (byte) 0x46);
		}
	},
	LZ4("application/x-lz4", "lz4") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 4
					&& Objects.equals(bytes[0], (byte) 0x04)
					&& Objects.equals(bytes[1], (byte) 0x22)
					&& Objects.equals(bytes[2], (byte) 0x4d)
					&& Objects.equals(bytes[3], (byte) 0x18);
		}
	},
	//https://github.com/madler/brotli/blob/master/br-format-v3.txt,brotli 没有固定的file magic number,所以此处只是参考
	BR("application/x-brotli", "br") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0xce)
					&& Objects.equals(bytes[1], (byte) 0xb2)
					&& Objects.equals(bytes[2], (byte) 0xcf)
					&& Objects.equals(bytes[3], (byte) 0x81);
		}
	},
	DCM("application/x-dicom", "dcm") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 128
					&& Objects.equals(bytes[128], (byte) 0x44)
					&& Objects.equals(bytes[129], (byte) 0x49)
					&& Objects.equals(bytes[130], (byte) 0x43)
					&& Objects.equals(bytes[131], (byte) 0x4d);
		}
	},
	RPM("application/x-rpm", "rpm") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 4
					&& Objects.equals(bytes[0], (byte) 0xed)
					&& Objects.equals(bytes[1], (byte) 0xab)
					&& Objects.equals(bytes[2], (byte) 0xee)
					&& Objects.equals(bytes[3], (byte) 0xdb);
		}
	},
	ZSTD("application/x-zstd", "zst") {
		@Override
		public boolean match(final byte[] bytes) {
			final int length = bytes.length;
			if (length < 5) {
				return false;
			}
			final byte[] buf1 = new byte[]{(byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27, (byte) 0x28};
			final boolean flag1 = ArrayUtil.contains(buf1, bytes[0])
					&& Objects.equals(bytes[1], (byte) 0xb5)
					&& Objects.equals(bytes[2], (byte) 0x2f)
					&& Objects.equals(bytes[3], (byte) 0xfd);
			if (flag1) {
				return true;
			}
			if ((bytes[0] & 0xF0) == 0x50) {
				return bytes[1] == 0x2A && bytes[2] == 0x4D && bytes[3] == 0x18;
			}
			return false;
		}
	},
	//archive end------------------------------------------------------------
	//video start------------------------------------------------------------
	MP4("video/mp4", "mp4") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 13) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x74)
					&& Objects.equals(bytes[6], (byte) 0x79)
					&& Objects.equals(bytes[7], (byte) 0x70)
					&& Objects.equals(bytes[8], (byte) 0x4d)
					&& Objects.equals(bytes[9], (byte) 0x53)
					&& Objects.equals(bytes[10], (byte) 0x4e)
					&& Objects.equals(bytes[11], (byte) 0x56);
			final boolean flag2 = Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x74)
					&& Objects.equals(bytes[6], (byte) 0x79)
					&& Objects.equals(bytes[7], (byte) 0x70)
					&& Objects.equals(bytes[8], (byte) 0x69)
					&& Objects.equals(bytes[9], (byte) 0x73)
					&& Objects.equals(bytes[10], (byte) 0x6f)
					&& Objects.equals(bytes[11], (byte) 0x6d);
			return flag1 || flag2;
		}
	},
	AVI("video/x-msvideo", "avi") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 11
					&& Objects.equals(bytes[0], (byte) 0x52)
					&& Objects.equals(bytes[1], (byte) 0x49)
					&& Objects.equals(bytes[2], (byte) 0x46)
					&& Objects.equals(bytes[3], (byte) 0x46)
					&& Objects.equals(bytes[8], (byte) 0x41)
					&& Objects.equals(bytes[9], (byte) 0x56)
					&& Objects.equals(bytes[10], (byte) 0x49)
					&& Objects.equals(bytes[11], (byte) 0x20);
		}
	},
	WMV("video/x-ms-wmv", "wmv") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 9
					&& Objects.equals(bytes[0], (byte) 0x30)
					&& Objects.equals(bytes[1], (byte) 0x26)
					&& Objects.equals(bytes[2], (byte) 0xb2)
					&& Objects.equals(bytes[3], (byte) 0x75)
					&& Objects.equals(bytes[4], (byte) 0x8e)
					&& Objects.equals(bytes[5], (byte) 0x66)
					&& Objects.equals(bytes[6], (byte) 0xcf)
					&& Objects.equals(bytes[7], (byte) 0x11)
					&& Objects.equals(bytes[8], (byte) 0xa6)
					&& Objects.equals(bytes[9], (byte) 0xd9);
		}
	},
	M4V("video/x-m4v", "m4v") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 12) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x74)
					&& Objects.equals(bytes[6], (byte) 0x79)
					&& Objects.equals(bytes[7], (byte) 0x70)
					&& Objects.equals(bytes[8], (byte) 0x4d)
					&& Objects.equals(bytes[9], (byte) 0x34)
					&& Objects.equals(bytes[10], (byte) 0x56)
					&& Objects.equals(bytes[11], (byte) 0x20);
			final boolean flag2 = Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x74)
					&& Objects.equals(bytes[6], (byte) 0x79)
					&& Objects.equals(bytes[7], (byte) 0x70)
					&& Objects.equals(bytes[8], (byte) 0x6d)
					&& Objects.equals(bytes[9], (byte) 0x70)
					&& Objects.equals(bytes[10], (byte) 0x34)
					&& Objects.equals(bytes[11], (byte) 0x32);
			return flag1 || flag2;
		}
	},
	FLV("video/x-flv", "flv") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x46)
					&& Objects.equals(bytes[1], (byte) 0x4c)
					&& Objects.equals(bytes[2], (byte) 0x56)
					&& Objects.equals(bytes[3], (byte) 0x01);
		}
	},
	MKV("video/x-matroska", "mkv") {
		@Override
		public boolean match(final byte[] bytes) {
			//0x42 0x82 0x88 0x6d 0x61 0x74 0x72 0x6f 0x73 0x6b 0x61
			final boolean flag1 = bytes.length > 11
					&& Objects.equals(bytes[0], (byte) 0x1a)
					&& Objects.equals(bytes[1], (byte) 0x45)
					&& Objects.equals(bytes[2], (byte) 0xdf)
					&& Objects.equals(bytes[3], (byte) 0xa3);

			if (flag1) {
				//此处需要判断是否是'\x42\x82\x88matroska'，算法类似kmp判断
				final byte[] bytes1 = {(byte) 0x42, (byte) 0x82, (byte) 0x88, (byte) 0x6d, (byte) 0x61, (byte) 0x74, (byte) 0x72, (byte) 0x6f, (byte) 0x73, (byte) 0x6b, (byte) 0x61};
				final int index = FileMagicNumber.indexOf(bytes, bytes1);
				return index > 0;
			}
			return false;
		}
	},

	WEBM("video/webm", "webm") {
		@Override
		public boolean match(final byte[] bytes) {
			final boolean flag1 = bytes.length > 8
					&& Objects.equals(bytes[0], (byte) 0x1a)
					&& Objects.equals(bytes[1], (byte) 0x45)
					&& Objects.equals(bytes[2], (byte) 0xdf)
					&& Objects.equals(bytes[3], (byte) 0xa3);
			if (flag1) {
				//此处需要判断是否是'\x42\x82\x88webm'，算法类似kmp判断
				final byte[] bytes1 = {(byte) 0x42, (byte) 0x82, (byte) 0x88, (byte) 0x77, (byte) 0x65, (byte) 0x62, (byte) 0x6d};
				final int index = FileMagicNumber.indexOf(bytes, bytes1);
				return index > 0;
			}
			return false;
		}
	},
	//此文件签名非常复杂，只判断常见的几种
	MOV("video/quicktime", "mov") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 12) {
				return false;
			}
			final boolean flag1 = Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x74)
					&& Objects.equals(bytes[6], (byte) 0x79)
					&& Objects.equals(bytes[7], (byte) 0x70)
					&& Objects.equals(bytes[8], (byte) 0x71)
					&& Objects.equals(bytes[9], (byte) 0x74)
					&& Objects.equals(bytes[10], (byte) 0x20)
					&& Objects.equals(bytes[11], (byte) 0x20);
			final boolean flag2 = Objects.equals(bytes[4], (byte) 0x6D)
					&& Objects.equals(bytes[5], (byte) 0x6F)
					&& Objects.equals(bytes[6], (byte) 0x6F)
					&& Objects.equals(bytes[7], (byte) 0x76);
			final boolean flag3 = Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x72)
					&& Objects.equals(bytes[6], (byte) 0x65)
					&& Objects.equals(bytes[7], (byte) 0x65);
			final boolean flag4 = Objects.equals(bytes[4], (byte) 0x6D)
					&& Objects.equals(bytes[5], (byte) 0x64)
					&& Objects.equals(bytes[6], (byte) 0x61)
					&& Objects.equals(bytes[7], (byte) 0x74);
			final boolean flag5 = Objects.equals(bytes[4], (byte) 0x77)
					&& Objects.equals(bytes[5], (byte) 0x69)
					&& Objects.equals(bytes[6], (byte) 0x64)
					&& Objects.equals(bytes[7], (byte) 0x65);
			final boolean flag6 = Objects.equals(bytes[4], (byte) 0x70)
					&& Objects.equals(bytes[5], (byte) 0x6E)
					&& Objects.equals(bytes[6], (byte) 0x6F)
					&& Objects.equals(bytes[7], (byte) 0x74);
			final boolean flag7 = Objects.equals(bytes[4], (byte) 0x73)
					&& Objects.equals(bytes[5], (byte) 0x6B)
					&& Objects.equals(bytes[6], (byte) 0x69)
					&& Objects.equals(bytes[7], (byte) 0x70);
			return flag1 || flag2 || flag3 || flag4 || flag5 || flag6 || flag7;
		}
	},
	MPEG("video/mpeg", "mpg") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 3
					&& Objects.equals(bytes[0], (byte) 0x00)
					&& Objects.equals(bytes[1], (byte) 0x00)
					&& Objects.equals(bytes[2], (byte) 0x01)
					&& (bytes[3] >= (byte) 0xb0 && bytes[3] <= (byte) 0xbf);
		}
	},
	RMVB("video/vnd.rn-realvideo", "rmvb") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 4
					&& Objects.equals(bytes[0], (byte) 0x2E)
					&& Objects.equals(bytes[1], (byte) 0x52)
					&& Objects.equals(bytes[2], (byte) 0x4D)
					&& Objects.equals(bytes[3], (byte) 0x46);
		}
	},
	M3GP("video/3gpp", "3gp") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 10
					&& Objects.equals(bytes[4], (byte) 0x66)
					&& Objects.equals(bytes[5], (byte) 0x74)
					&& Objects.equals(bytes[6], (byte) 0x79)
					&& Objects.equals(bytes[7], (byte) 0x70)
					&& Objects.equals(bytes[8], (byte) 0x33)
					&& Objects.equals(bytes[9], (byte) 0x67)
					&& Objects.equals(bytes[10], (byte) 0x70);
		}
	},
	//video end ---------------------------------------------------------------
	//document start ----------------------------------------------------------
	DOC("application/msword", "doc") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0xd0, (byte) 0xcf, (byte) 0x11, (byte) 0xe0, (byte) 0xa1, (byte) 0xb1, (byte) 0x1a, (byte) 0xe1};
			final boolean flag1 = bytes.length > 515 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 8), byte1);
			if (flag1) {
				final byte[] byte2 = new byte[]{(byte) 0xec, (byte) 0xa5, (byte) 0xc1, (byte) 0x00};
				final boolean flag2 = Arrays.equals(Arrays.copyOfRange(bytes, 512, 516), byte2);
				final byte[] byte3 = new byte[]{(byte) 0x00, (byte) 0x0a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4d, (byte) 0x53, (byte) 0x57, (byte) 0x6f, (byte) 0x72, (byte) 0x64
						, (byte) 0x44, (byte) 0x6f, (byte) 0x63, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x57, (byte) 0x6f, (byte) 0x72, (byte) 0x64,
						(byte) 0x2e, (byte) 0x44, (byte) 0x6f, (byte) 0x63, (byte) 0x75, (byte) 0x6d, (byte) 0x65, (byte) 0x6e, (byte) 0x74, (byte) 0x2e, (byte) 0x38, (byte) 0x00,
						(byte) 0xf4, (byte) 0x39, (byte) 0xb2, (byte) 0x71};
				final byte[] range = Arrays.copyOfRange(bytes, 2075, 2142);
				final boolean flag3 = bytes.length > 2142 && FileMagicNumber.indexOf(range, byte3) > 0;
				return flag2 || flag3;
			}
			return false;
		}
	},

	XLS("application/vnd.ms-excel", "xls") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0xd0, (byte) 0xcf, (byte) 0x11, (byte) 0xe0, (byte) 0xa1, (byte) 0xb1, (byte) 0x1a, (byte) 0xe1};
			final boolean flag1 = bytes.length > 520 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 8), byte1);
			if (flag1) {
				final byte[] byte2 = new byte[]{(byte) 0xfd, (byte) 0xff, (byte) 0xff, (byte) 0xff};
				final boolean flag2 = Arrays.equals(Arrays.copyOfRange(bytes, 512, 516), byte2) && (bytes[518] == 0x00 || bytes[518] == 0x02);
				final byte[] byte3 = new byte[]{(byte) 0x09, (byte) 0x08, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x05, (byte) 0x00};
				final boolean flag3 = Arrays.equals(Arrays.copyOfRange(bytes, 512, 520), byte3);
				final byte[] byte4 = new byte[]{(byte) 0xe2, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5c, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x43, (byte) 0x61, (byte) 0x6c, (byte) 0x63};
				final boolean flag4 = bytes.length > 2095 && Arrays.equals(Arrays.copyOfRange(bytes, 1568, 2095), byte4);
				return flag2 || flag3 || flag4;
			}
			return false;
		}

	},
	PPT("application/vnd.ms-powerpoint", "ppt") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0xd0, (byte) 0xcf, (byte) 0x11, (byte) 0xe0, (byte) 0xa1, (byte) 0xb1, (byte) 0x1a, (byte) 0xe1};
			final boolean flag1 = bytes.length > 524 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 8), byte1);
			if (flag1) {
				final byte[] byte2 = new byte[]{(byte) 0xa0, (byte) 0x46, (byte) 0x1d, (byte) 0xf0};
				final byte[] byteRange = Arrays.copyOfRange(bytes, 512, 516);
				final boolean flag2 = Arrays.equals(byteRange, byte2);
				final byte[] byte3 = new byte[]{(byte) 0x00, (byte) 0x6e, (byte) 0x1e, (byte) 0xf0};
				final boolean flag3 = Arrays.equals(byteRange, byte3);
				final byte[] byte4 = new byte[]{(byte) 0x0f, (byte) 0x00, (byte) 0xe8, (byte) 0x03};
				final boolean flag4 = Arrays.equals(byteRange, byte4);
				final byte[] byte5 = new byte[]{(byte) 0xfd, (byte) 0xff, (byte) 0xff, (byte) 0xff};
				final boolean flag5 = Arrays.equals(byteRange, byte5) && bytes[522] == 0x00 && bytes[523] == 0x00;
				final byte[] byte6 = new byte[]{(byte) 0x00, (byte) 0xb9, (byte) 0x29, (byte) 0xe8, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00,
						(byte) 0x4d, (byte) 0x53, (byte) 0x20, (byte) 0x50, (byte) 0x6f, (byte) 0x77, (byte) 0x65, (byte) 0x72, (byte) 0x50, (byte)
						0x6f, (byte) 0x69, (byte) 0x6e, (byte) 0x74, (byte) 0x20, (byte) 0x39, (byte) 0x37};
				final boolean flag6 = bytes.length > 2096 && Arrays.equals(Arrays.copyOfRange(bytes, 2072, 2096), byte6);
				return flag2 || flag3 || flag4 || flag5 || flag6;
			}
			return false;
		}
	},
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx") {
		@Override
		public boolean match(final byte[] bytes) {
			return Objects.equals(FileMagicNumber.matchDocument(bytes), DOCX);
		}
	},
	PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx") {
		@Override
		public boolean match(final byte[] bytes) {
			return Objects.equals(FileMagicNumber.matchDocument(bytes), PPTX);
		}
	},
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx") {
		@Override
		public boolean match(final byte[] bytes) {
			return Objects.equals(FileMagicNumber.matchDocument(bytes), XLSX);
		}
	},

	//document end ------------------------------------------------------------
	//other start -------------------------------------------------------------
	WASM("application/wasm", "wasm") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 7
					&& Objects.equals(bytes[0], (byte) 0x00)
					&& Objects.equals(bytes[1], (byte) 0x61)
					&& Objects.equals(bytes[2], (byte) 0x73)
					&& Objects.equals(bytes[3], (byte) 0x6D)
					&& Objects.equals(bytes[4], (byte) 0x01)
					&& Objects.equals(bytes[5], (byte) 0x00)
					&& Objects.equals(bytes[6], (byte) 0x00)
					&& Objects.equals(bytes[7], (byte) 0x00);
		}
	},
	// https://source.android.com/devices/tech/dalvik/dex-format#dex-file-magic
	DEX("application/vnd.android.dex", "dex") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 36
					&& Objects.equals(bytes[0], (byte) 0x64)
					&& Objects.equals(bytes[1], (byte) 0x65)
					&& Objects.equals(bytes[2], (byte) 0x78)
					&& Objects.equals(bytes[3], (byte) 0x0A)
					&& Objects.equals(bytes[36], (byte) 0x70);
		}
	},
	DEY("application/vnd.android.dey", "dey") {
		@Override
		public boolean match(final byte[] bytes) {
			return bytes.length > 100
					&& Objects.equals(bytes[0], (byte) 0x64)
					&& Objects.equals(bytes[1], (byte) 0x65)
					&& Objects.equals(bytes[2], (byte) 0x79)
					&& Objects.equals(bytes[3], (byte) 0x0A) &&
					DEX.match(Arrays.copyOfRange(bytes, 40, 100));
		}
	},
	EML("message/rfc822", "eml") {
		@Override
		public boolean match(final byte[] bytes) {
			if (bytes.length < 8) {
				return false;
			}
			final byte[] byte1 = new byte[]{(byte) 0x46, (byte) 0x72, (byte) 0x6F, (byte) 0x6D, (byte) 0x20, (byte) 0x20, (byte) 0x20};
			final byte[] byte2 = new byte[]{(byte) 0x46, (byte) 0x72, (byte) 0x6F, (byte) 0x6D, (byte) 0x20, (byte) 0x3F, (byte) 0x3F, (byte) 0x3F};
			final byte[] byte3 = new byte[]{(byte) 0x46, (byte) 0x72, (byte) 0x6F, (byte) 0x6D, (byte) 0x3A, (byte) 0x20};
			final byte[] byte4 = new byte[]{(byte) 0x52, (byte) 0x65, (byte) 0x74, (byte) 0x75, (byte) 0x72, (byte) 0x6E, (byte) 0x2D, (byte) 0x50, (byte) 0x61, (byte) 0x74, (byte) 0x68, (byte) 0x3A, (byte) 0x20};
			return Arrays.equals(Arrays.copyOfRange(bytes, 0, 7), byte1)
					|| Arrays.equals(Arrays.copyOfRange(bytes, 0, 8), byte2)
					|| Arrays.equals(Arrays.copyOfRange(bytes, 0, 6), byte3)
					|| bytes.length > 13 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 13), byte4);
		}
	},
	MDB("application/vnd.ms-access", "mdb") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x53, (byte) 0x74, (byte) 0x61, (byte) 0x6E, (byte) 0x64,
					(byte) 0x61, (byte) 0x72, (byte) 0x64, (byte) 0x20, (byte) 0x4A, (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x44, (byte) 0x42};
			return bytes.length > 18 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 18), byte1);
		}
	},
	//CHM  49 54 53 46
	CHM("application/vnd.ms-htmlhelp", "chm") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0x49, (byte) 0x54, (byte) 0x53, (byte) 0x46};
			return bytes.length > 4 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 4), byte1);
		}
	},
	//class CA FE BA BE
	CLASS("application/java-vm", "class") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
			return bytes.length > 4 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 4), byte1);
		}
	},
	//torrent 64 38 3A 61 6E 6E 6F 75 6E 63 65
	TORRENT("application/x-bittorrent", "torrent") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0x64, (byte) 0x38, (byte) 0x3A, (byte) 0x61, (byte) 0x6E, (byte) 0x6E, (byte) 0x6F, (byte) 0x75, (byte) 0x6E, (byte) 0x63, (byte) 0x65};
			return bytes.length > 11 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 11), byte1);
		}
	},
	WPD("application/vnd.wordperfect", "wpd") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0xFF, (byte) 0x57, (byte) 0x50, (byte) 0x43};
			return bytes.length > 4 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 4), byte1);
		}
	},
	DBX("", "dbx") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0xCF, (byte) 0xAD, (byte) 0x12, (byte) 0xFE};
			return bytes.length > 4 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 4), byte1);
		}
	},
	PST("application/vnd.ms-outlook-pst", "pst") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0x21, (byte) 0x42, (byte) 0x44, (byte) 0x4E};
			return bytes.length > 4 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 4), byte1);
		}
	},
	RAM("audio/x-pn-realaudio", "ram") {
		@Override
		public boolean match(final byte[] bytes) {
			final byte[] byte1 = new byte[]{(byte) 0x2E, (byte) 0x72, (byte) 0x61, (byte) 0xFD, (byte) 0x00};
			return bytes.length > 5 && Arrays.equals(Arrays.copyOfRange(bytes, 0, 5), byte1);
		}
	}
	//other end ---------------------------------------------------------------
	;
	private final String mimeType;
	private final String extension;

	FileMagicNumber(final String mimeType, final String extension) {
		this.mimeType = mimeType;
		this.extension = extension;
	}

	public static FileMagicNumber getMagicNumber(final byte[] bytes) {
		final FileMagicNumber number = Arrays.stream(values())
				.filter(fileMagicNumber -> fileMagicNumber.match(bytes))
				.findFirst()
				.orElse(UNKNOWN);
		if (number.equals(FileMagicNumber.ZIP)) {
			final FileMagicNumber fn = FileMagicNumber.matchDocument(bytes);
			return fn == UNKNOWN ? ZIP : fn;
		}
		return number;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getExtension() {
		return extension;
	}

	private static int indexOf(final byte[] array, final byte[] target) {
		if (array == null || target == null || array.length < target.length) {
			return -1;
		}
		if (target.length == 0) {
			return 0;
		} else {
			label1:
			for (int i = 0; i < array.length - target.length + 1; ++i) {
				for (int j = 0; j < target.length; ++j) {
					if (array[i + j] != target[j]) {
						continue label1;
					}
				}
				return i;
			}
			return -1;
		}
	}

	//处理 Open XML 类型的文件
	private static boolean compareBytes(final byte[] buf, final byte[] slice, final int startOffset) {
		final int sl = slice.length;
		if (startOffset + sl > buf.length) {
			return false;
		}
		final byte[] sub = Arrays.copyOfRange(buf, startOffset, startOffset + sl);
		return Arrays.equals(sub, slice);
	}

	private static FileMagicNumber matchOpenXmlMime(final byte[] bytes, final int offset) {
		final byte[] word = new byte[]{'w', 'o', 'r', 'd', '/'};
		final byte[] ppt = new byte[]{'p', 'p', 't', '/'};
		final byte[] xl = new byte[]{'x', 'l', '/'};
		if (FileMagicNumber.compareBytes(bytes, word, offset)) {
			return FileMagicNumber.DOCX;
		}
		if (FileMagicNumber.compareBytes(bytes, ppt, offset)) {
			return FileMagicNumber.PPTX;
		}
		if (FileMagicNumber.compareBytes(bytes, xl, offset)) {
			return FileMagicNumber.XLSX;
		}
		return FileMagicNumber.UNKNOWN;
	}

	private static FileMagicNumber matchDocument(final byte[] bytes) {
		final FileMagicNumber fileMagicNumber = FileMagicNumber.matchOpenXmlMime(bytes, (byte) 0x1e);
		if (false == fileMagicNumber.equals(UNKNOWN)) {
			return fileMagicNumber;
		}
		final byte[] bytes1 = new byte[]{0x5B, 0x43, 0x6F, 0x6E, 0x74, 0x65, 0x6E, 0x74, 0x5F, 0x54, 0x79, 0x70, 0x65, 0x73, 0x5D, 0x2E, 0x78, 0x6D, 0x6C};
		final byte[] bytes2 = new byte[]{0x5F, 0x72, 0x65, 0x6C, 0x73, 0x2F, 0x2E, 0x72, 0x65, 0x6C, 0x73};
		final byte[] bytes3 = new byte[]{0x64, 0x6F, 0x63, 0x50, 0x72, 0x6F, 0x70, 0x73};
		final boolean flag1 = FileMagicNumber.compareBytes(bytes, bytes1, (byte) 0x1e);
		final boolean flag2 = FileMagicNumber.compareBytes(bytes, bytes2, (byte) 0x1e);
		final boolean flag3 = FileMagicNumber.compareBytes(bytes, bytes3, (byte) 0x1e);
		if (false == (flag1 || flag2 || flag3)) {
			return UNKNOWN;
		}
		int index = 0;
		for (int i = 0; i < 4; i++) {
			index = searchSignature(bytes, index + 4, 6000);
			if (index == -1) {
				continue;
			}
			final FileMagicNumber fn = FileMagicNumber.matchOpenXmlMime(bytes, index + 30);
			if (false == fn.equals(UNKNOWN)) {
				return fn;
			}
		}
		return UNKNOWN;
	}

	private static int searchSignature(final byte[] bytes, final int start, final int rangeNum) {
		final byte[] signature = new byte[]{0x50, 0x4B, 0x03, 0x04};
		final int length = bytes.length;
		int end = start + rangeNum;
		if (end > length) {
			end = length;
		}
		final int index = FileMagicNumber.indexOf(Arrays.copyOfRange(bytes, start, end), signature);
		return (index == -1)
				? -1
				: (start + index);
	}

	public abstract boolean match(byte[] bytes);

}
