package jgf.imaging;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Plays an animation sequence on the screen. All methods deals with a time in
 * second. To calculate this time, Animation class will need to know the ups
 * rate. This value must be supplied in animation constructor. A call to
 * update() method must be made at every logics update cicle.
 * 
 * @author Vinícius
 */
public class Animation
{
    // Updates per second to be considered for this animation
    private int ups;

    // Duration in ticks
    private int frameDurations[];
    private int frameIndex;

    private ImageItemList<ImageItem> images;
    private long updateCount;
    private boolean running = false;

    /**
     * Create a new stopped animation.
     * 
     * @param ups Estimated number of calls to update() in one second. Normally
     *        is the same as the updates per second rate.
     * @param images A list of images to be animated.
     * @param seconds Number of seconds for each frame in the sequence.
     */
    public Animation(int ups, ImageItemList<ImageItem> images, double seconds)
    {
        this.ups = ups;
        this.images = images;
        this.updateCount = 0;
        this.frameIndex = 0;
        this.frameDurations = new int[images.size()];
        setRunning(false);

        Arrays.fill(frameDurations, secondsToUpdates(seconds));
    }

    /**
     * Updates this animation logic. Must be called every update cicle.
     */
    public void update()
    {
        if (images.size() == 1 || !running)
            return;

        if (updateCount > frameDurations[frameIndex])
        {
            frameIndex = (frameIndex + 1) % images.size();
            updateCount = 0;
        }

        updateCount++;
    }

    /**
     * Return the current image in the sequence. This image will change over
     * time, allowing animation to take place.
     * 
     * @return the image that must be drawn.
     */
    public BufferedImage getImage()
    {
        return images.get(frameIndex).getImage();
    }

    /**
     * Calculate the number of updates in the given time.
     * 
     * @param seconds The number of seconds.
     * @return The number of updates in the given number of seconds.
     */
    private int secondsToUpdates(double seconds)
    {
        return (int) (seconds * ups);
    }

    /**
     * Calculate the number of seconds that this number of updates will take.
     * 
     * @param updates number of updates to calculate.
     * @return the number of seconds that this number of updates will take.
     */
    private double updatesToSeconds(long updates)
    {
        return (double) updates / (double) ups;
    }

    /**
     * Change the duration of a single frame in the animation sequence.
     * 
     * @param index The index of the frame to change, starting in zero.
     * @param seconds Number of seconds in the frame.
     */
    public void setFrameDuration(int index, double seconds)
    {
        frameDurations[index] = secondsToUpdates(seconds);
    }

    /**
     * Returns the total duration of this animation. Basically it's the sum of
     * each frame.
     * 
     * @return the total duration of this animation
     */
    public double getTotalDuration()
    {
        long totalDuration = 0;

        for (int duration : frameDurations)
            totalDuration += duration;

        return updatesToSeconds(totalDuration);
    }

    /**
     * Changes the running state of the animation. Stopping the animation does
     * not automatically rewinds it. To rewind, use the rewind() method.
     * 
     * @param running True to make the animation run, false to stop it.
     * @see Animation#rewind()
     */
    public void setRunning(boolean running)
    {
        this.running = running;
    }

    /**
     * Indicate if the current animation sequence is running.
     * 
     * @return True if the animation is running, false if not.
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Positions the animation beginning. It does not stop the animation
     * sequence.
     */
    public void rewind()
    {
        frameIndex = 0;
        updateCount = 0;
    }
}
