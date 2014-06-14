/*
 * To the extent possible under law, the Fiji developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.process.ColorProcessor;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Fiji-OpenCV
 * 
 * OpenCV Bindings for Fiji
 * 
 * @author The Fiji Team
 */
public class Fiji_OpenCV implements PlugInFilter {
	protected ImagePlus imp;
	protected ImageProcessor ip;

	// image property members
	private int width;
	private int height;

	/**
	 * @see ij.plugin.filter.PlugInFilter#setup(java.lang.String, ij.ImagePlus)
	 */

	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}

		this.imp = imp;
		return DOES_RGB;
	}

	/**
	 * @see ij.plugin.filter.PlugInFilter#run(String arg)
	 */

	@Override
	public void run(ImageProcessor ip) {
		nu.pattern.OpenCV.loadShared();
		IJ.log("Welcome to OpenCV " + Core.VERSION);

		this.ip = ip;
		// get width and height
		width = ip.getWidth();
		height = ip.getHeight();

		process(ip);
	}

	public void showAbout() {
		IJ.showMessage("Fiji-OpenCV", "OpenCV Bindings for Fiji");
	}

	/**
	 * Process an image.
	 * 
	 * Please provide this method even if {@link ij.plugin.filter.PlugInFilter}
	 * does require it; the method
	 * {@link ij.plugin.filter.PlugInFilter#run(ij.process.ImageProcessor)} can
	 * only handle 2-dimensional data.
	 * 
	 * If your plugin does not change the pixels in-place, make this method
	 * return the results and change the
	 * {@link #setup(java.lang.String, ij.ImagePlus)} method to return also the
	 * <i>DOES_NOTHING</i> flag.
	 * 
	 * @param image
	 *            the image (possible multi-dimensional)
	 */
	public void process(ImagePlus image) {
		// slice numbers start with 1 for historical reasons
		for (int i = 1; i <= image.getStackSize(); i++)
			process(image.getStack().getProcessor(i));
	}

	// Select processing method depending on image type
	public void process(ImageProcessor ip) {
		int type = this.imp.getType();
		if (type == ImagePlus.COLOR_RGB)
			process((int[]) ip.getPixels());
		else {
			throw new RuntimeException("not supported");
		}
	}

	// processing of COLOR_RGB images
	public void process(int[] pixels) {
		int channels = 3;
		byte[] buf = new byte[pixels.length * channels];
		for (int i = 0; i < pixels.length; i++) {
			buf[i * channels] = (byte) (0x000000ff & (pixels[i]));
			buf[i * channels + 1] = (byte) (0x000000ff & (pixels[i] >>> 8));
			buf[i * channels + 2] = (byte) (0x000000ff & (pixels[i] >>> 16));
		}

		Mat image = new Mat(width, height, CvType.CV_8UC3);
		image.put(0, 0, buf);

		// Create a face detector from the cascade file in the resources
		// directory.
		CascadeClassifier faceDetector = new CascadeClassifier(getClass()
				.getResource(
						"/opencv/data/haarcascades/haarcascade_frontalface_alt2.xml")
				.getPath());

		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		System.out.println(String.format("Detected %s faces",
				faceDetections.toArray().length));

		// Draw a bounding box around each face.
		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}

		image.get(0, 0, buf);
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x80000000 + ((int) (buf[i * channels + 2]) << 16)
					+ ((int) (buf[i * channels + 1]) << 8)
					+ ((int) (buf[i * channels + 0]));
		}
		this.ip = new ColorProcessor(width, height, pixels);
	}

	/**
	 * Main method for debugging.
	 * 
	 * For debugging, it is convenient to have a method that starts ImageJ,
	 * loads an image and calls the plugin, e.g. after setting breakpoints.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		// set the plugins.dir property to make the plugin appear in the Plugins
		// menu
		Class<?> clazz = Fiji_OpenCV.class;
		String url = clazz.getResource(
				"/" + clazz.getName().replace('.', '/') + ".class").toString();
		String pluginsDir = url.substring(5, url.length()
				- clazz.getName().length() - 6);
		System.setProperty("plugins.dir", pluginsDir);

		// start ImageJ
		new ImageJ();

		// open the Lena sample
		ImagePlus image = IJ.openImage("http://imagej.net/images/lena.jpg");
		image.show();

		// run the plugin
		IJ.runPlugIn(clazz.getName(), "");
	}
}
