/*
 * To the extent possible under law, the Fiji developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import ij.IJ;
import ij.ImageJ;
import ij.plugin.PlugIn;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;

/**
 * Fiji-OpenCV
 *
 * OpenCV Bindings for Fiji
 *
 * @author The Fiji Team
 */
public class Fiji_OpenCV implements PlugIn {

	/**
	 * @see ij.plugin.filter.PlugIn#run(String arg)
	 */

	@Override
	public void run(String arg) {
		nu.pattern.OpenCV.loadShared();
		IJ.log("Welcome to OpenCV " + Core.VERSION);
	    Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
	    IJ.log("OpenCV Mat: " + m);
	    Mat mr1 = m.row(1);
	    mr1.setTo(new Scalar(1));
	    Mat mc5 = m.col(5);
	    mc5.setTo(new Scalar(5));
	    IJ.log("OpenCV Mat data:\n" + m.dump());
	}

	public void showAbout() {
		IJ.showMessage("Fiji-OpenCV",
			"OpenCV Bindings for Fiji"
		);
	}

	/**
	 * Main method for debugging.
	 *
	 * For debugging, it is convenient to have a method that starts ImageJ, loads an
	 * image and calls the plugin, e.g. after setting breakpoints.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		// set the plugins.dir property to make the plugin appear in the Plugins menu
		Class<?> clazz = Fiji_OpenCV.class;
		String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
		String pluginsDir = url.substring(5, url.length() - clazz.getName().length() - 6);
		System.setProperty("plugins.dir", pluginsDir);

		// start ImageJ
		new ImageJ();

		// run the plugin
		IJ.runPlugIn(clazz.getName(), "");
	}

}
