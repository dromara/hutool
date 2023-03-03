package cn.hutool.swing.img;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.IORuntimeException;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片元信息工具类<br>
 * 借助metadata-extractor完成图片元信息的读取，如旋转角度等问题
 *
 * @author wdz
 * @since 5.8.13
 */
public class ImgMetaUtil {

	/**
	 * 纠正图片旋转<br>
	 * 通过读取图片元数据信息获取旋转角度，然后根据旋转角度修正图片的角度
	 *
	 * @param file 图片文件
	 * @return {@link BufferedImage}
	 * @throws IOException IO异常
	 */
	public static BufferedImage correctBufferImg(final File file) throws IOException {
		// 获取偏转角度
		final int orientation = getOrientation(file);


		// 原始图片缓存
		final BufferedImage srcImg = ImageIO.read(file);
		//如果不偏转，直接返回即可
		if (orientation != 90 && orientation != 270) {
			return srcImg;
		}


		// 宽高互换
		// 原始宽度
		final int imgWidth = srcImg.getHeight();
		// 原始高度
		final int imgHeight = srcImg.getWidth();

		// 中心点位置
		final double centerWidth = ((double) imgWidth) / 2;
		final double centerHeight = ((double) imgHeight) / 2;

		// 图片缓存
		final BufferedImage targetImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

		// 旋转对应角度
		final Graphics2D g = targetImg.createGraphics();
		g.rotate(Math.toRadians(orientation), centerWidth, centerHeight);
		g.drawImage(srcImg, (imgWidth - srcImg.getWidth()) / 2, (imgHeight - srcImg.getHeight()) / 2, null);
		g.rotate(Math.toRadians(-orientation), centerWidth, centerHeight);
		g.dispose();
		return targetImg;
	}

	/**
	 * 获取图片文件旋转角度
	 *
	 * @param file 上传图片
	 * @return 旋转角度
	 * @throws IORuntimeException IO异常
	 */
	public static int getOrientation(final File file) throws IORuntimeException {
		final Metadata metadata;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (final ImageProcessingException e) {
			throw new UtilException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return getOrientation(metadata);
	}

	/**
	 * 获取图片旋转角度
	 *
	 * @param in 图片流
	 * @return 旋转角度
	 * @throws IORuntimeException IO异常
	 */
	public static int getOrientation(final InputStream in) throws IORuntimeException {
		final Metadata metadata;
		try {
			metadata = ImageMetadataReader.readMetadata(in);
		} catch (final ImageProcessingException e) {
			throw new UtilException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return getOrientation(metadata);
	}

	private static int getOrientation(final Metadata metadata) {
		for (final Directory directory : metadata.getDirectories()) {
			for (final Tag tag : directory.getTags()) {
				if ("Orientation".equals(tag.getTagName())) {
					final String orientation = tag.getDescription();
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
