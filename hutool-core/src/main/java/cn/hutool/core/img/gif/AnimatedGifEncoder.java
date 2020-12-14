package cn.hutool.core.img.gif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 动态GIF动画生成器，可生成一个或多个帧的GIF。
 *
 * <pre>
 * Example:
 *    AnimatedGifEncoder e = new AnimatedGifEncoder();
 *    e.start(outputFileName);
 *    e.setDelay(1000);   // 1 frame per sec
 *    e.addFrame(image1);
 *    e.addFrame(image2);
 *    e.finish();
 * </pre>
 * <p>
 * 来自：https://github.com/rtyley/animated-gif-lib-for-java
 *
 * @author Kevin Weiner, FM Software
 * @version 1.03 November 2003
 * @since 5.3.8
 */
public class AnimatedGifEncoder {

	protected int width; // image size
	protected int height;
	protected Color transparent = null; // transparent color if given
	protected boolean transparentExactMatch = false; // transparent color will be found by looking for the closest color
	// or for the exact color if transparentExactMatch == true
	protected Color background = null;  // background color if given
	protected int transIndex; // transparent index in color table
	protected int repeat = -1; // no repeat
	protected int delay = 0; // frame delay (hundredths)
	protected boolean started = false; // ready to output frames
	protected OutputStream out;
	protected BufferedImage image; // current frame
	protected byte[] pixels; // BGR byte array from frame
	protected byte[] indexedPixels; // converted frame indexed to palette
	protected int colorDepth; // number of bit planes
	protected byte[] colorTab; // RGB palette
	protected boolean[] usedEntry = new boolean[256]; // active palette entries
	protected int palSize = 7; // color table size (bits-1)
	protected int dispose = -1; // disposal code (-1 = use default)
	protected boolean closeStream = false; // close stream when finished
	protected boolean firstFrame = true;
	protected boolean sizeSet = false; // if false, get size from first frame
	protected int sample = 10; // default sample interval for quantizer

	/**
	 * 设置每一帧的间隔时间
	 * Sets the delay time between each frame, or changes it
	 * for subsequent frames (applies to last frame added).
	 *
	 * @param ms 间隔时间，单位毫秒
	 */
	public void setDelay(int ms) {
		delay = Math.round(ms / 10.0f);
	}

	/**
	 * Sets the GIF frame disposal code for the last added frame
	 * and any subsequent frames.  Default is 0 if no transparent
	 * color has been set, otherwise 2.
	 *
	 * @param code int disposal code.
	 */
	public void setDispose(int code) {
		if (code >= 0) {
			dispose = code;
		}
	}

	/**
	 * Sets the number of times the set of GIF frames
	 * should be played.  Default is 1; 0 means play
	 * indefinitely.  Must be invoked before the first
	 * image is added.
	 *
	 * @param iter int number of iterations.
	 */
	public void setRepeat(int iter) {
		if (iter >= 0) {
			repeat = iter;
		}
	}

	/**
	 * Sets the transparent color for the last added frame
	 * and any subsequent frames.
	 * Since all colors are subject to modification
	 * in the quantization process, the color in the final
	 * palette for each frame closest to the given color
	 * becomes the transparent color for that frame.
	 * May be set to null to indicate no transparent color.
	 *
	 * @param c Color to be treated as transparent on display.
	 */
	public void setTransparent(Color c) {
		setTransparent(c, false);
	}

	/**
	 * Sets the transparent color for the last added frame
	 * and any subsequent frames.
	 * Since all colors are subject to modification
	 * in the quantization process, the color in the final
	 * palette for each frame closest to the given color
	 * becomes the transparent color for that frame.
	 * If exactMatch is set to true, transparent color index
	 * is search with exact match, and not looking for the
	 * closest one.
	 * May be set to null to indicate no transparent color.
	 *
	 * @param c          Color to be treated as transparent on display.
	 * @param exactMatch If exactMatch is set to true, transparent color index is search with exact match
	 */
	public void setTransparent(Color c, boolean exactMatch) {
		transparent = c;
		transparentExactMatch = exactMatch;
	}


	/**
	 * Sets the background color for the last added frame
	 * and any subsequent frames.
	 * Since all colors are subject to modification
	 * in the quantization process, the color in the final
	 * palette for each frame closest to the given color
	 * becomes the background color for that frame.
	 * May be set to null to indicate no background color
	 * which will default to black.
	 *
	 * @param c Color to be treated as background on display.
	 */
	public void setBackground(Color c) {
		background = c;
	}

	/**
	 * Adds next GIF frame.  The frame is not written immediately, but is
	 * actually deferred until the next frame is received so that timing
	 * data can be inserted.  Invoking <code>finish()</code> flushes all
	 * frames.  If <code>setSize</code> was not invoked, the size of the
	 * first image is used for all subsequent frames.
	 *
	 * @param im BufferedImage containing frame to write.
	 * @return true if successful.
	 */
	public boolean addFrame(BufferedImage im) {
		if ((im == null) || !started) {
			return false;
		}
		boolean ok = true;
		try {
			if (!sizeSet) {
				// use first frame's size
				setSize(im.getWidth(), im.getHeight());
			}
			image = im;
			getImagePixels(); // convert to correct format if necessary
			analyzePixels(); // build color table & map pixels
			if (firstFrame) {
				writeLSD(); // logical screen descriptior
				writePalette(); // global color table
				if (repeat >= 0) {
					// use NS app extension to indicate reps
					writeNetscapeExt();
				}
			}
			writeGraphicCtrlExt(); // write graphic control extension
			writeImageDesc(); // image descriptor
			if (!firstFrame) {
				writePalette(); // local color table
			}
			writePixels(); // encode and write pixel data
			firstFrame = false;
		} catch (IOException e) {
			ok = false;
		}

		return ok;
	}

	/**
	 * Flushes any pending data and closes output file.
	 * If writing to an OutputStream, the stream is not
	 * closed.
	 *
	 * @return is ok
	 */
	public boolean finish() {
		if (!started) return false;
		boolean ok = true;
		started = false;
		try {
			out.write(0x3b); // gif trailer
			out.flush();
			if (closeStream) {
				out.close();
			}
		} catch (IOException e) {
			ok = false;
		}

		// reset for subsequent use
		transIndex = 0;
		out = null;
		image = null;
		pixels = null;
		indexedPixels = null;
		colorTab = null;
		closeStream = false;
		firstFrame = true;

		return ok;
	}

	/**
	 * Sets frame rate in frames per second.  Equivalent to
	 * <code>setDelay(1000/fps)</code>.
	 *
	 * @param fps float frame rate (frames per second)
	 */
	public void setFrameRate(float fps) {
		if (fps != 0f) {
			delay = Math.round(100f / fps);
		}
	}

	/**
	 * Sets quality of color quantization (conversion of images
	 * to the maximum 256 colors allowed by the GIF specification).
	 * Lower values (minimum = 1) produce better colors, but slow
	 * processing significantly.  10 is the default, and produces
	 * good color mapping at reasonable speeds.  Values greater
	 * than 20 do not yield significant improvements in speed.
	 *
	 * @param quality int greater than 0.
	 */
	public void setQuality(int quality) {
		if (quality < 1) quality = 1;
		sample = quality;
	}

	/**
	 * Sets the GIF frame size.  The default size is the
	 * size of the first frame added if this method is
	 * not invoked.
	 *
	 * @param w int frame width.
	 * @param h int frame width.
	 */
	public void setSize(int w, int h) {
		if (started && !firstFrame) return;
		width = w;
		height = h;
		if (width < 1) width = 320;
		if (height < 1) height = 240;
		sizeSet = true;
	}

	/**
	 * Initiates GIF file creation on the given stream.  The stream
	 * is not closed automatically.
	 *
	 * @param os OutputStream on which GIF images are written.
	 * @return false if initial write failed.
	 */
	public boolean start(OutputStream os) {
		if (os == null) return false;
		boolean ok = true;
		closeStream = false;
		out = os;
		try {
			writeString("GIF89a"); // header
		} catch (IOException e) {
			ok = false;
		}
		return started = ok;
	}

	/**
	 * Initiates writing of a GIF file with the specified name.
	 *
	 * @param file String containing output file name.
	 * @return false if open or initial write failed.
	 */
	public boolean start(String file) {
		boolean ok;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			ok = start(out);
			closeStream = true;
		} catch (IOException e) {
			ok = false;
		}
		return started = ok;
	}

	public boolean isStarted() {
		return started;
	}

	/**
	 * Analyzes image colors and creates color map.
	 */
	protected void analyzePixels() {
		int len = pixels.length;
		int nPix = len / 3;
		indexedPixels = new byte[nPix];
		NeuQuant nq = new NeuQuant(pixels, len, sample);
		// initialize quantizer
		colorTab = nq.process(); // create reduced palette
		// convert map from BGR to RGB
		for (int i = 0; i < colorTab.length; i += 3) {
			byte temp = colorTab[i];
			colorTab[i] = colorTab[i + 2];
			colorTab[i + 2] = temp;
			usedEntry[i / 3] = false;
		}
		// map image pixels to new palette
		int k = 0;
		for (int i = 0; i < nPix; i++) {
			int index =
					nq.map(pixels[k++] & 0xff,
							pixels[k++] & 0xff,
							pixels[k++] & 0xff);
			usedEntry[index] = true;
			indexedPixels[i] = (byte) index;
		}
		pixels = null;
		colorDepth = 8;
		palSize = 7;
		// get closest match to transparent color if specified
		if (transparent != null) {
			transIndex = transparentExactMatch ? findExact(transparent) : findClosest(transparent);
		}
	}

	/**
	 * Returns index of palette color closest to c
	 *
	 * @param c Color
	 * @return index
	 */
	protected int findClosest(Color c) {
		if (colorTab == null) return -1;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int minpos = 0;
		int dmin = 256 * 256 * 256;
		int len = colorTab.length;
		for (int i = 0; i < len; ) {
			int dr = r - (colorTab[i++] & 0xff);
			int dg = g - (colorTab[i++] & 0xff);
			int db = b - (colorTab[i] & 0xff);
			int d = dr * dr + dg * dg + db * db;
			int index = i / 3;
			if (usedEntry[index] && (d < dmin)) {
				dmin = d;
				minpos = index;
			}
			i++;
		}
		return minpos;
	}

	/**
	 * Returns true if the exact matching color is existing, and used in the color palette, otherwise, return false.
	 * This method has to be called before finishing the image,
	 * because after finished the palette is destroyed and it will always return false.
	 *
	 * @param c 颜色
	 * @return 颜色是否存在
	 */
	boolean isColorUsed(Color c) {
		return findExact(c) != -1;
	}

	/**
	 * Returns index of palette exactly matching to color c or -1 if there is no exact matching.
	 *
	 * @param c Color
	 * @return index
	 */
	protected int findExact(Color c) {
		if (colorTab == null) {
			return -1;
		}

		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int len = colorTab.length / 3;
		for (int index = 0; index < len; ++index) {
			int i = index * 3;
			// If the entry is used in colorTab, then check if it is the same exact color we're looking for
			if (usedEntry[index] && r == (colorTab[i] & 0xff) && g == (colorTab[i + 1] & 0xff) && b == (colorTab[i + 2] & 0xff)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Extracts image pixels into byte array "pixels"
	 */
	protected void getImagePixels() {
		int w = image.getWidth();
		int h = image.getHeight();
		int type = image.getType();
		if ((w != width)
				|| (h != height)
				|| (type != BufferedImage.TYPE_3BYTE_BGR)) {
			// create new image with right size/format
			BufferedImage temp =
					new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = temp.createGraphics();
			g.setColor(background);
			g.fillRect(0, 0, width, height);
			g.drawImage(image, 0, 0, null);
			image = temp;
		}
		pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Writes Graphic Control Extension
	 *
	 * @throws IOException IO异常
	 */
	protected void writeGraphicCtrlExt() throws IOException {
		out.write(0x21); // extension introducer
		out.write(0xf9); // GCE label
		out.write(4); // data block size
		int transp, disp;
		if (transparent == null) {
			transp = 0;
			disp = 0; // dispose = no action
		} else {
			transp = 1;
			disp = 2; // force clear if using transparent color
		}
		if (dispose >= 0) {
			disp = dispose & 7; // user override
		}
		disp <<= 2;

		// packed fields
		//noinspection PointlessBitwiseExpression
		out.write(0 | // 1:3 reserved
				disp | // 4:6 disposal
				0 | // 7   user input - 0 = none
				transp); // 8   transparency flag

		writeShort(delay); // delay x 1/100 sec
		out.write(transIndex); // transparent color index
		out.write(0); // block terminator
	}

	/**
	 * Writes Image Descriptor
	 *
	 * @throws IOException IO异常
	 */
	protected void writeImageDesc() throws IOException {
		out.write(0x2c); // image separator
		writeShort(0); // image position x,y = 0,0
		writeShort(0);
		writeShort(width); // image size
		writeShort(height);
		// packed fields
		if (firstFrame) {
			// no LCT  - GCT is used for first (or only) frame
			out.write(0);
		} else {
			// specify normal LCT
			//noinspection PointlessBitwiseExpression
			out.write(0x80 | // 1 local color table  1=yes
					0 | // 2 interlace - 0=no
					0 | // 3 sorted - 0=no
					0 | // 4-5 reserved
					palSize); // 6-8 size of color table
		}
	}

	/**
	 * Writes Logical Screen Descriptor
	 *
	 * @throws IOException IO异常
	 */
	protected void writeLSD() throws IOException {
		// logical screen size
		writeShort(width);
		writeShort(height);
		// packed fields
		//noinspection PointlessBitwiseExpression
		out.write((0x80 | // 1   : global color table flag = 1 (gct used)
				0x70 | // 2-4 : color resolution = 7
				0x00 | // 5   : gct sort flag = 0
				palSize)); // 6-8 : gct size

		out.write(0); // background color index
		out.write(0); // pixel aspect ratio - assume 1:1
	}

	/**
	 * Writes Netscape application extension to define
	 * repeat count.
	 *
	 * @throws IOException IO异常
	 */
	protected void writeNetscapeExt() throws IOException {
		out.write(0x21); // extension introducer
		out.write(0xff); // app extension label
		out.write(11); // block size
		writeString("NETSCAPE" + "2.0"); // app id + auth code
		out.write(3); // sub-block size
		out.write(1); // loop sub-block id
		writeShort(repeat); // loop count (extra iterations, 0=repeat forever)
		out.write(0); // block terminator
	}

	/**
	 * Writes color table
	 *
	 * @throws IOException IO异常
	 */
	protected void writePalette() throws IOException {
		out.write(colorTab, 0, colorTab.length);
		int n = (3 * 256) - colorTab.length;
		for (int i = 0; i < n; i++) {
			out.write(0);
		}
	}

	/**
	 * Encodes and writes pixel data
	 *
	 * @throws IOException IO异常
	 */
	protected void writePixels() throws IOException {
		LZWEncoder encoder = new LZWEncoder(width, height, indexedPixels, colorDepth);
		encoder.encode(out);
	}

	/**
	 * Write 16-bit value to output stream, LSB first
	 *
	 * @param value 16-bit value
	 * @throws IOException IO异常
	 */
	protected void writeShort(int value) throws IOException {
		out.write(value & 0xff);
		out.write((value >> 8) & 0xff);
	}

	/**
	 * Writes string to output stream
	 *
	 * @param s String
	 * @throws IOException IO异常
	 */
	protected void writeString(String s) throws IOException {
		for (int i = 0; i < s.length(); i++) {
			out.write((byte) s.charAt(i));
		}
	}
}
