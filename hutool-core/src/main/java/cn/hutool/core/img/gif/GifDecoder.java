package cn.hutool.core.img.gif;

import cn.hutool.core.io.IoUtil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * GIF文件解析
 * Class GifDecoder - Decodes a GIF file into one or more frames.
 * <p>
 * Example:
 *
 * <pre>
 * {@code
 *    GifDecoder d = new GifDecoder();
 *    d.read("sample.gif");
 *    int n = d.getFrameCount();
 *    for (int i = 0; i < n; i++) {
 *       BufferedImage frame = d.getFrame(i);  // frame i
 *       int t = d.getDelay(i);  // display duration of frame in milliseconds
 *       // do something with frame
 *    }
 * }
 * </pre>
 * <p>
 * 来自：https://github.com/rtyley/animated-gif-lib-for-java
 *
 * @author Kevin Weiner, FM Software; LZW decoder adapted from John Cristy's ImageMagick.
 * @version 1.03 November 2003
 */
public class GifDecoder {

	/**
	 * File read status: No errors.
	 */
	public static final int STATUS_OK = 0;

	/**
	 * File read status: Error decoding file (may be partially decoded)
	 */
	public static final int STATUS_FORMAT_ERROR = 1;

	/**
	 * File read status: Unable to open source.
	 */
	public static final int STATUS_OPEN_ERROR = 2;

	protected BufferedInputStream in;
	protected int status;

	protected int width; // full image width
	protected int height; // full image height
	protected boolean gctFlag; // global color table used
	protected int gctSize; // size of global color table
	protected int loopCount = 1; // iterations; 0 = repeat forever

	protected int[] gct; // global color table
	protected int[] lct; // local color table
	protected int[] act; // active color table

	protected int bgIndex; // background color index
	protected int bgColor; // background color
	protected int lastBgColor; // previous bg color
	protected int pixelAspect; // pixel aspect ratio

	protected boolean lctFlag; // local color table flag
	protected boolean interlace; // interlace flag
	protected int lctSize; // local color table size

	protected int ix, iy, iw, ih; // current image rectangle
	protected Rectangle lastRect; // last image rect
	protected BufferedImage image; // current frame
	protected BufferedImage lastImage; // previous frame

	protected byte[] block = new byte[256]; // current data block
	protected int blockSize = 0; // block size

	// last graphic control extension info
	protected int dispose = 0;
	// 0=no action; 1=leave in place; 2=restore to bg; 3=restore to prev
	protected int lastDispose = 0;
	protected boolean transparency = false; // use transparent color
	protected int delay = 0; // delay in milliseconds
	protected int transIndex; // transparent color index

	protected static final int MAX_STACK_SIZE = 4096;
	// max decoder pixel stack size

	// LZW decoder working arrays
	protected short[] prefix;
	protected byte[] suffix;
	protected byte[] pixelStack;
	protected byte[] pixels;

	protected ArrayList<GifFrame> frames; // frames read from current file
	protected int frameCount;

	static class GifFrame {
		public GifFrame(BufferedImage im, int del) {
			image = im;
			delay = del;
		}

		public BufferedImage image;
		public int delay;
	}

	/**
	 * Gets display duration for specified frame.
	 *
	 * @param n int index of frame
	 * @return delay in milliseconds
	 */
	public int getDelay(int n) {
		//
		delay = -1;
		if ((n >= 0) && (n < frameCount)) {
			delay = frames.get(n).delay;
		}
		return delay;
	}

	/**
	 * Gets the number of frames read from file.
	 *
	 * @return frame count
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * Gets the first (or only) image read.
	 *
	 * @return BufferedImage containing first frame, or null if none.
	 */
	public BufferedImage getImage() {
		return getFrame(0);
	}

	/**
	 * Gets the "Netscape" iteration count, if any.
	 * A count of 0 means repeat indefinitiely.
	 *
	 * @return iteration count if one was specified, else 1.
	 */
	public int getLoopCount() {
		return loopCount;
	}

	/**
	 * Creates new frame image from current data (and previous
	 * frames as specified by their disposition codes).
	 */
	protected void setPixels() {
		// expose destination image's pixels as int array
		int[] dest =
				((DataBufferInt) image.getRaster().getDataBuffer()).getData();

		// fill in starting image contents based on last image's dispose code
		if (lastDispose > 0) {
			if (lastDispose == 3) {
				// use image before last
				int n = frameCount - 2;
				if (n > 0) {
					lastImage = getFrame(n - 1);
				} else {
					lastImage = null;
				}
			}

			if (lastImage != null) {
				int[] prev =
						((DataBufferInt) lastImage.getRaster().getDataBuffer()).getData();
				System.arraycopy(prev, 0, dest, 0, width * height);
				// copy pixels

				if (lastDispose == 2) {
					// fill last image rect area with background color
					Graphics2D g = image.createGraphics();
					Color c;
					if (transparency) {
						c = new Color(0, 0, 0, 0);    // assume background is transparent
					} else {
						c = new Color(lastBgColor); // use given background color
					}
					g.setColor(c);
					g.setComposite(AlphaComposite.Src); // replace area
					g.fill(lastRect);
					g.dispose();
				}
			}
		}

		// copy each source line to the appropriate place in the destination
		int pass = 1;
		int inc = 8;
		int iline = 0;
		for (int i = 0; i < ih; i++) {
			int line = i;
			if (interlace) {
				if (iline >= ih) {
					pass++;
					switch (pass) {
						case 2:
							iline = 4;
							break;
						case 3:
							iline = 2;
							inc = 4;
							break;
						case 4:
							iline = 1;
							inc = 2;
					}
				}
				line = iline;
				iline += inc;
			}
			line += iy;
			if (line < height) {
				int k = line * width;
				int dx = k + ix; // start of line in dest
				int dlim = dx + iw; // end of dest line
				if ((k + width) < dlim) {
					dlim = k + width; // past dest edge
				}
				int sx = i * iw; // start of line in source
				while (dx < dlim) {
					// map color and insert in destination
					int index = ((int) pixels[sx++]) & 0xff;
					int c = act[index];
					if (c != 0) {
						dest[dx] = c;
					}
					dx++;
				}
			}
		}
	}

	/**
	 * Gets the image contents of frame n.
	 *
	 * @param n frame
	 * @return BufferedImage
	 */
	public BufferedImage getFrame(int n) {
		BufferedImage im = null;
		if ((n >= 0) && (n < frameCount)) {
			im = frames.get(n).image;
		}
		return im;
	}

	/**
	 * Gets image size.
	 *
	 * @return GIF image dimensions
	 */
	public Dimension getFrameSize() {
		return new Dimension(width, height);
	}

	/**
	 * Reads GIF image from stream
	 *
	 * @param is BufferedInputStream containing GIF file.
	 * @return read status code (0 = no errors)
	 */
	public int read(BufferedInputStream is) {
		init();
		if (is != null) {
			in = is;
			readHeader();
			if (false == err()) {
				readContents();
				if (frameCount < 0) {
					status = STATUS_FORMAT_ERROR;
				}
			}
		} else {
			status = STATUS_OPEN_ERROR;
		}
		IoUtil.close(is);
		return status;
	}

	/**
	 * Reads GIF image from stream
	 *
	 * @param is InputStream containing GIF file.
	 * @return read status code (0 = no errors)
	 */
	public int read(InputStream is) {
		init();
		if (is != null) {
			if (!(is instanceof BufferedInputStream))
				is = new BufferedInputStream(is);
			in = (BufferedInputStream) is;
			readHeader();
			if (!err()) {
				readContents();
				if (frameCount < 0) {
					status = STATUS_FORMAT_ERROR;
				}
			}
		} else {
			status = STATUS_OPEN_ERROR;
		}
		IoUtil.close(is);
		return status;
	}

	/**
	 * Reads GIF file from specified file/URL source
	 * (URL assumed if name contains ":/" or "file:")
	 *
	 * @param name String containing source
	 * @return read status code (0 = no errors)
	 */
	public int read(String name) {
		status = STATUS_OK;
		try {
			name = name.trim().toLowerCase();
			if ((name.contains("file:")) ||
					(name.indexOf(":/") > 0)) {
				URL url = new URL(name);
				in = new BufferedInputStream(url.openStream());
			} else {
				in = new BufferedInputStream(new FileInputStream(name));
			}
			status = read(in);
		} catch (IOException e) {
			status = STATUS_OPEN_ERROR;
		}

		return status;
	}

	/**
	 * Decodes LZW image data into pixel array.
	 * Adapted from John Cristy's ImageMagick.
	 */
	protected void decodeImageData() {
		int NullCode = -1;
		int npix = iw * ih;
		int available,
				clear,
				code_mask,
				code_size,
				end_of_information,
				in_code,
				old_code,
				bits,
				code,
				count,
				i,
				datum,
				data_size,
				first,
				top,
				bi,
				pi;

		if ((pixels == null) || (pixels.length < npix)) {
			pixels = new byte[npix]; // allocate new pixel array
		}
		if (prefix == null) prefix = new short[MAX_STACK_SIZE];
		if (suffix == null) suffix = new byte[MAX_STACK_SIZE];
		if (pixelStack == null) pixelStack = new byte[MAX_STACK_SIZE + 1];

		//  Initialize GIF data stream decoder.

		data_size = read();
		clear = 1 << data_size;
		end_of_information = clear + 1;
		available = clear + 2;
		old_code = NullCode;
		code_size = data_size + 1;
		code_mask = (1 << code_size) - 1;
		for (code = 0; code < clear; code++) {
			prefix[code] = 0;
			suffix[code] = (byte) code;
		}

		//  Decode GIF pixel stream.

		datum = bits = count = first = top = pi = bi = 0;

		for (i = 0; i < npix; ) {
			if (top == 0) {
				if (bits < code_size) {
					//  Load bytes until there are enough bits for a code.
					if (count == 0) {
						// Read a new data block.
						count = readBlock();
						if (count <= 0)
							break;
						bi = 0;
					}
					datum += (((int) block[bi]) & 0xff) << bits;
					bits += 8;
					bi++;
					count--;
					continue;
				}

				//  Get the next code.

				code = datum & code_mask;
				datum >>= code_size;
				bits -= code_size;

				//  Interpret the code

				if ((code > available) || (code == end_of_information))
					break;
				if (code == clear) {
					//  Reset decoder.
					code_size = data_size + 1;
					code_mask = (1 << code_size) - 1;
					available = clear + 2;
					old_code = NullCode;
					continue;
				}
				if (old_code == NullCode) {
					pixelStack[top++] = suffix[code];
					old_code = code;
					first = code;
					continue;
				}
				in_code = code;
				if (code == available) {
					pixelStack[top++] = (byte) first;
					code = old_code;
				}
				while (code > clear) {
					pixelStack[top++] = suffix[code];
					code = prefix[code];
				}
				first = ((int) suffix[code]) & 0xff;

				//  Add a new string to the string table,

				if (available >= MAX_STACK_SIZE) {
					pixelStack[top++] = (byte) first;
					continue;
				}
				pixelStack[top++] = (byte) first;
				prefix[available] = (short) old_code;
				suffix[available] = (byte) first;
				available++;
				if (((available & code_mask) == 0)
						&& (available < MAX_STACK_SIZE)) {
					code_size++;
					code_mask += available;
				}
				old_code = in_code;
			}

			//  Pop a pixel off the pixel stack.

			top--;
			pixels[pi++] = pixelStack[top];
			i++;
		}

		for (i = pi; i < npix; i++) {
			pixels[i] = 0; // clear missing pixels
		}

	}

	/**
	 * Returns true if an error was encountered during reading/decoding
	 *
	 * @return true if an error was encountered during reading/decoding
	 */
	protected boolean err() {
		return status != STATUS_OK;
	}

	/**
	 * Initializes or re-initializes reader
	 */
	protected void init() {
		status = STATUS_OK;
		frameCount = 0;
		frames = new ArrayList<>();
		gct = null;
		lct = null;
	}

	/**
	 * Reads a single byte from the input stream.
	 *
	 * @return single byte
	 */
	protected int read() {
		int curByte = 0;
		try {
			curByte = in.read();
		} catch (IOException e) {
			status = STATUS_FORMAT_ERROR;
		}
		return curByte;
	}

	/**
	 * Reads next variable length block from input.
	 *
	 * @return number of bytes stored in "buffer"
	 */
	protected int readBlock() {
		blockSize = read();
		int n = 0;
		if (blockSize > 0) {
			try {
				int count;
				while (n < blockSize) {
					count = in.read(block, n, blockSize - n);
					if (count == -1)
						break;
					n += count;
				}
			} catch (IOException e) {
				//ignore
			}

			if (n < blockSize) {
				status = STATUS_FORMAT_ERROR;
			}
		}
		return n;
	}

	/**
	 * Reads color table as 256 RGB integer values
	 *
	 * @param ncolors int number of colors to read
	 * @return int array containing 256 colors (packed ARGB with full alpha)
	 */
	protected int[] readColorTable(int ncolors) {
		int nbytes = 3 * ncolors;
		int[] tab = null;
		byte[] c = new byte[nbytes];
		int n = 0;
		try {
			n = in.read(c);
		} catch (IOException e) {
			//ignore
		}
		if (n < nbytes) {
			status = STATUS_FORMAT_ERROR;
		} else {
			tab = new int[256]; // max size to avoid bounds checks
			int i = 0;
			int j = 0;
			while (i < ncolors) {
				int r = ((int) c[j++]) & 0xff;
				int g = ((int) c[j++]) & 0xff;
				int b = ((int) c[j++]) & 0xff;
				tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
			}
		}
		return tab;
	}

	/**
	 * Main file parser.  Reads GIF content blocks.
	 */
	protected void readContents() {
		// read GIF file content blocks
		boolean done = false;
		while (!(done || err())) {
			int code = read();
			switch (code) {

				case 0x2C: // image separator
					readImage();
					break;

				case 0x21: // extension
					code = read();
					switch (code) {
						case 0xf9: // graphics control extension
							readGraphicControlExt();
							break;

						case 0xff: // application extension
							readBlock();
							final StringBuilder app = new StringBuilder();
							for (int i = 0; i < 11; i++) {
								app.append((char) block[i]);
							}
							if ("NETSCAPE2.0".equals(app.toString())) {
								readNetscapeExt();
							} else {
								skip(); // don't care
							}
							break;

						default: // uninteresting extension
							skip();
					}
					break;

				case 0x3b: // terminator
					done = true;
					break;

				case 0x00: // bad byte, but keep going and see what happens
					break;

				default:
					status = STATUS_FORMAT_ERROR;
			}
		}
	}

	/**
	 * Reads Graphics Control Extension values
	 */
	protected void readGraphicControlExt() {
		read(); // block size
		int packed = read(); // packed fields
		dispose = (packed & 0x1c) >> 2; // disposal method
		if (dispose == 0) {
			dispose = 1; // elect to keep old image if discretionary
		}
		transparency = (packed & 1) != 0;
		delay = readShort() * 10; // delay in milliseconds
		transIndex = read(); // transparent color index
		read(); // block terminator
	}

	/**
	 * Reads GIF file header information.
	 */
	protected void readHeader() {
		final StringBuilder id = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			id.append((char) read());
		}
		if (false == id.toString().startsWith("GIF")) {
			status = STATUS_FORMAT_ERROR;
			return;
		}

		readLSD();
		if (gctFlag && !err()) {
			gct = readColorTable(gctSize);
			bgColor = gct[bgIndex];
		}
	}

	/**
	 * Reads next frame image
	 */
	protected void readImage() {
		ix = readShort(); // (sub)image position & size
		iy = readShort();
		iw = readShort();
		ih = readShort();

		int packed = read();
		lctFlag = (packed & 0x80) != 0; // 1 - local color table flag
		interlace = (packed & 0x40) != 0; // 2 - interlace flag
		// 3 - sort flag
		// 4-5 - reserved
		lctSize = 2 << (packed & 7); // 6-8 - local color table size

		if (lctFlag) {
			lct = readColorTable(lctSize); // read table
			act = lct; // make local table active
		} else {
			act = gct; // make global table active
			if (bgIndex == transIndex)
				bgColor = 0;
		}
		int save = 0;
		if (transparency) {
			save = act[transIndex];
			act[transIndex] = 0; // set transparent color if specified
		}

		if (act == null) {
			status = STATUS_FORMAT_ERROR; // no color table defined
		}

		if (err()) return;

		decodeImageData(); // decode pixel data
		skip();

		if (err()) return;

		frameCount++;

		// create new image to receive frame data
		image =
				new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);

		setPixels(); // transfer pixel data to image

		frames.add(new GifFrame(image, delay)); // add image to frame list

		if (transparency) {
			act[transIndex] = save;
		}
		resetFrame();

	}

	/**
	 * Reads Logical Screen Descriptor
	 */
	protected void readLSD() {

		// logical screen size
		width = readShort();
		height = readShort();

		// packed fields
		int packed = read();
		gctFlag = (packed & 0x80) != 0; // 1   : global color table flag
		// 2-4 : color resolution
		// 5   : gct sort flag
		gctSize = 2 << (packed & 7); // 6-8 : gct size

		bgIndex = read(); // background color index
		pixelAspect = read(); // pixel aspect ratio
	}

	/**
	 * Reads Netscape extenstion to obtain iteration count
	 */
	protected void readNetscapeExt() {
		do {
			readBlock();
			if (block[0] == 1) {
				// loop count sub-block
				int b1 = ((int) block[1]) & 0xff;
				int b2 = ((int) block[2]) & 0xff;
				loopCount = (b2 << 8) | b1;
			}
		} while ((blockSize > 0) && !err());
	}

	/**
	 * Reads next 16-bit value, LSB first
	 *
	 * @return next 16-bit value
	 */
	protected int readShort() {
		// read 16-bit value, LSB first
		return read() | (read() << 8);
	}

	/**
	 * Resets frame state for reading next image.
	 */
	protected void resetFrame() {
		lastDispose = dispose;
		lastRect = new Rectangle(ix, iy, iw, ih);
		lastImage = image;
		lastBgColor = bgColor;
		int dispose = 0;
		boolean transparency = false;
		int delay = 0;
		lct = null;
	}

	/**
	 * Skips variable length blocks up to and including
	 * next zero length block.
	 */
	protected void skip() {
		do {
			readBlock();
		} while ((blockSize > 0) && !err());
	}
}
