package cn.hutool.core.img;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片避免旋转工具类
 *
 *@Date: 2023/2/28 19:21<br/>
 * @author wdz
 * @since 5.8.13
 */
public class ImgRevolveUtil {
	/**
	 * 纠正图片旋转
	 *
	 * @param srcFile
	 */
	public static BufferedImage correctBufferImg(File srcFile) throws IOException, ImageProcessingException {
		// 获取偏转角度
		int angle = getAngle(srcFile);
		//如果不偏转，直接返回即可
		if (angle != 90 && angle != 270) {
			return ImageIO.read(srcFile);
		}

		// 原始图片缓存
		BufferedImage srcImg = ImageIO.read(srcFile);

		// 宽高互换
		// 原始宽度
		int imgWidth = srcImg.getHeight();
		// 原始高度
		int imgHeight = srcImg.getWidth();

		// 中心点位置
		double centerWidth = ((double) imgWidth) / 2;
		double centerHeight = ((double) imgHeight) / 2;

		// 图片缓存
		BufferedImage targetImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

		// 旋转对应角度
		Graphics2D g = targetImg.createGraphics();
		g.rotate(Math.toRadians(angle), centerWidth, centerHeight);
		g.drawImage(srcImg, (imgWidth - srcImg.getWidth()) / 2, (imgHeight - srcImg.getHeight()) / 2, null);
		g.rotate(Math.toRadians(-angle), centerWidth, centerHeight);
		g.dispose();
		return targetImg;
	}


	/**
	 * 获取图片旋转角度
	 *
	 * @param file 上传图片
	 * @return
	 */
	public static int getAngle(File file) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				if ("Orientation".equals(tag.getTagName())) {
					String orientation = tag.getDescription();
					if (orientation.contains("90")) {
						return 90;
					} else if (orientation.contains("180")) {
						return 180;
					} else if (orientation.contains("270")) {
						return 270;
					}
				}
			}
		}
		return 0;
	}
}
