package jgf.core;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * The ScreenManager class manages initializing and displaying full screen
 * graphics modes.
 */
public class ScreenManager {
	private static ScreenManager myInstance = new ScreenManager();
	private GraphicsDevice device;

	/**
	 * Creates a new ScreenManager object.
	 */
	private ScreenManager() {
		GraphicsEnvironment environment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
	}

	public static ScreenManager getInstance() {
		return myInstance;
	}

	/**
	 * Returns a list of compatible display modes for the default device on the
	 * system.
	 */
	public DisplayMode[] getCompatibleDisplayModes() {
		return device.getDisplayModes();
	}

	/**
	 * Returns the first compatible mode in a list of modes. Returns null if no
	 * modes are compatible.
	 */
	public DisplayMode findFirstCompatibleMode(DisplayMode modes[]) {
		DisplayMode systemModes[] = device.getDisplayModes();
		for (DisplayMode mode : modes)
			for (DisplayMode systemMode : systemModes)
				if (displayModesMatch(mode, systemMode))
					return systemMode;

		return null;
	}

	/**
	 * Returns the current display mode.
	 */
	public DisplayMode getCurrentDisplayMode() {
		return device.getDisplayMode();
	}

	/**
	 * Determines if two display modes "match". Two display modes match if they
	 * have the same resolution, bit depth, and refresh rate. The bit depth is
	 * ignored if one of the modes has a bit depth of
	 * DisplayMode.BIT_DEPTH_MULTI. Likewise, the refresh rate is ignored if one
	 * of the modes has a refresh rate of DisplayMode.REFRESH_RATE_UNKNOWN.
	 */
	public boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2)

	{
		if (mode1.getWidth() != mode2.getWidth()
				|| mode1.getHeight() != mode2.getHeight())
			return false;

		if (mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
				&& mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
				&& mode1.getBitDepth() != mode2.getBitDepth())
			return false;

		if (mode1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
				&& mode2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
				&& mode1.getRefreshRate() != mode2.getRefreshRate())
			return false;

		return true;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Enters full screen mode and changes the display mode. If the specified
	 * display mode is null or not compatible with this device, or if the
	 * display mode cannot be changed on this system, the current display mode
	 * is used.
	 * <p>
	 * The display uses a BufferStrategy with 2 buffers.
	 */
	public void setFullScreen(DisplayMode displayMode) {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);

		device.setFullScreenWindow(frame);

		if (displayMode != null && device.isDisplayChangeSupported()) {
			setDisplayMode(displayMode);
			// fix for mac os x
			frame.setSize(displayMode.getWidth(), displayMode.getHeight());
		}

		frame.createBufferStrategy(2);
		sleep(500);
	}

	/**
	 * Gets the graphics context for the display. The ScreenManager uses double
	 * buffering, so applications must call update() to show any graphics drawn.
	 * <p>
	 * The application must dispose of the graphics object.
	 */
	public Graphics2D getGraphics() {
		if (getFullScreenWindow() == null)
			throw new IllegalStateException("Not in full screen mode.");

		return (Graphics2D) getFullScreenWindow().getBufferStrategy()
				.getDrawGraphics();
	}

	/**
	 * Updates the display.
	 */
	public void update() {
		if (getFullScreenWindow() != null) {
			BufferStrategy strategy = getFullScreenWindow().getBufferStrategy();
			if (!strategy.contentsLost())
				strategy.show();
		}

		// Sync the display on some systems.
		// (on Linux, this fixes event queue problems)
		Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * Returns the window currently used in full screen mode. Returns null if
	 * the device is not in full screen mode.
	 */
	public JFrame getFullScreenWindow() {
		return (JFrame) device.getFullScreenWindow();
	}

	/**
	 * Returns the width of the window currently used in full screen mode.
	 * Returns 0 if the device is not in full screen mode.
	 */
	public int getWidth() {
		if (getFullScreenWindow() == null)
			throw new IllegalStateException("Not in full screen mode.");

		return getFullScreenWindow().getWidth();
	}

	/**
	 * Returns the height of the window currently used in full screen mode.
	 * Returns 0 if the device is not in full screen mode.
	 */
	public int getHeight() {
		if (getFullScreenWindow() == null)
			throw new IllegalStateException("Not in full screen mode.");

		return getFullScreenWindow().getHeight();
	}

	/**
	 * Restores the screen's display mode.
	 */
	public void restoreScreen() {
		if (getFullScreenWindow() != null)
			getFullScreenWindow().dispose();

		device.setFullScreenWindow(null);
	}

	/**
	 * Creates an image compatible with the current display.
	 */
	public BufferedImage createCompatibleImage(int w, int h, int transparency) {
		if (getFullScreenWindow() == null)
			return GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice().getDefaultConfiguration()
					.createCompatibleImage(w, h, transparency);

		return getFullScreenWindow().getGraphicsConfiguration()
				.createCompatibleImage(w, h, transparency);

	}

	/**
	 * Creates an image compatible with the current display.
	 */
	public BufferedImage createCompatibleImage(int w, int h) {
		if (getFullScreenWindow() == null)
			return GraphicsEnvironment.getLocalGraphicsEnvironment()
					.getDefaultScreenDevice().getDefaultConfiguration()
					.createCompatibleImage(w, h, Transparency.TRANSLUCENT);

		return getFullScreenWindow().getGraphicsConfiguration()
				.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
	}

	/**
	 * Changes the video display mode, if possible.
	 * 
	 * @param displayMode
	 */
	public void setDisplayMode(DisplayMode displayMode) {
		try {
			device.setDisplayMode(displayMode);
			sleep(500);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}
	}

	public boolean isDisplayChangeSupported() {
		return device.isDisplayChangeSupported();
	}

}
