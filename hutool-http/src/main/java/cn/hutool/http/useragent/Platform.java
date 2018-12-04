package cn.hutool.http.useragent;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;

/**
 * 平台对象
 * 
 * @author looly
 * @since 4.2.1
 */
public class Platform extends UserAgentInfo {

	/** 未知 */
	public static final Platform Unknown = new Platform(NameUnknown, null);

	/**
	 * 支持的移动平台类型
	 */
	public static final List<Platform> mobilePlatforms = CollUtil.newArrayList(//
			new Platform("Windows Phone", "windows (ce|phone|mobile)( os)?"), //
			new Platform("iPad", "ipad"), //
			new Platform("iPod", "ipod"), //
			new Platform("iPhone", "iphone"), //
			new Platform("Android", "android"), //
			new Platform("Symbian", "symbian(os)?"), //
			new Platform("Blackberry", "blackberry") //
	);

	/**
	 * 支持的桌面平台类型
	 */
	public static final List<Platform> desktopPlatforms=CollUtil.newArrayList(//
			new Platform("Windows", "windows"), //
			new Platform("Mac", "(macintosh|darwin)"), //
			new Platform("Linux", "linux"), //
			new Platform("Wii", "wii"), //
			new Platform("Playstation", "playstation"), //
			new Platform("Java", "java") //
	);
	
	/**
	 * 支持的平台类型
	 */
	public static final List<Platform> platforms;
	static {
		platforms=new ArrayList<Platform>(13);
		platforms.addAll(mobilePlatforms);
		platforms.addAll(desktopPlatforms);
	}

	/**
	 * 构造
	 * 
	 * @param name 平台名称
	 * @param regex 关键字或表达式
	 */
	public Platform(String name, String regex) {
		super(name, regex);
	}

	/**
	 * 是否为移动平台
	 * @return 是否为移动平台
	 */
	public boolean isMobile() {
		return mobilePlatforms.contains(this);
	}
}
