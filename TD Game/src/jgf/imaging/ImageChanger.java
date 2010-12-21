package jgf.imaging;

import java.awt.image.BufferedImage;

/**
 * The <code>ImageChanger</code> is capable of rotating, flipping and
 * rescaling images in a single operation.
 * 
 * @author Vinícius
 */
public class ImageChanger
{
    private boolean flipHorizontal;
    private boolean flipVertical;
    private float rotationAngle;

    private int width;
    private int height;

    /**
     * Create a new image changer to operate over the image.
     * 
     * @param width The width of the changed image. Zero does not make any width
     *        change.
     * @param height The height of the changed image. Zero does not make any
     *        width change.
     * @param rotationAngle Rotation to be applied in the image.
     * @param flipHorizontal True to make the changer flip the image in
     *        horizontal direction.
     * @param flipVertical True to make the changer flip the image in vertical
     *        direction.
     */
    public ImageChanger(int width,
            int height,
            float rotationAngle,
            boolean flipHorizontal,
            boolean flipVertical)
    {
        setWidth(width);
        setHeight(height);
        setRotationAngle(rotationAngle);
        setFlipHorizontal(flipHorizontal);
        setFlipVertical(flipVertical);
    }

    /**
     * Construct a new image changer. Applying it in the image will have no
     * effect.
     */
    public ImageChanger()
    {
        this(0, 0, 0, false, false);
    }

    /**
     * Constructs a new image changer that only rescale the image.
     * 
     * @param width The new width.
     * @param height The new height.
     */
    public ImageChanger(int width, int height)
    {
        this(width, height, 0, false, false);
    }

    /**
     * Indicate if this changer will flip the image in horizontal direction.
     * 
     * @return True if it will flip, false if not.
     */
    public boolean isFlipHorizontal()
    {
        return flipHorizontal;
    }

    /**
     * Makes this changer flip the image in horizontal direction.
     * 
     * @param flipHorizontal True to flip, false to not.
     */
    public void setFlipHorizontal(boolean flipHorizontal)
    {
        this.flipHorizontal = flipHorizontal;
    }

    /**
     * Indicate if this changer will flip the image in vertical direction.
     * 
     * @return True if it will flip, false if not.
     */
    public boolean isFlipVertical()
    {
        return flipVertical;
    }

    /**
     * Makes this changer flip the image in vertical direction.
     * 
     * @param flipVertical True to flip, false to not.
     */
    public void setFlipVertical(boolean flipVertical)
    {
        this.flipVertical = flipVertical;
    }

    /**
     * Get the height of the changed image. Returns zero if the height will not
     * be changed.
     * 
     * @return The height of the changed image. Zero if the image will not
     *         change this dimension.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Makes the image change its height. To cancel this dimension changing, set
     * this value to zero.
     * 
     * @param height The height of the new image. Zero to not change this size.
     */
    public void setHeight(int height)
    {
        if (height < 0)
            throw new IllegalArgumentException("Invalid height to change!");
        
        this.height = height;
    }

    /**
     * Get the width of the changed image. Returns zero if the width will not be
     * changed.
     * 
     * @return The width of the changed image. Zero if the image will not change
     *         this dimension.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Makes the image change its width. To cancel this dimension changing, set
     * this value to zero.
     * 
     * @param width The width of the new image. Zero to not change this size.
     */
    public void setWidth(int width)
    {
        if (width < 0)
            throw new IllegalArgumentException("Invalid width to change!");
        
        this.width = width;
    }

    /**
     * Get the rotation angle of the image. The image will be rotated in
     * clockwise direction.
     * 
     * @return The rotation angle. It's a number between zero and 360 degrees.
     */
    public float getRotationAngle()
    {
        return rotationAngle;
    }

    /**
     * Sets a new rotation angle in degrees. Images will be rotated in clowise
     * direction. The image size will not change and there will be no resizing
     * of the rotated image. Make sure the rotated area of the image always
     * falls inside the image boundaries.
     * 
     * @param rotation A rotation angle. If the angle is not a number between 0
     *        and 360 the changer will automatically convert it to a equivalent
     *        angle. An angle of 410 degrees will be converted to 50 degrees. An
     *        angle of -5 degrees will be converted to 355 degrees.
     */
    public void setRotationAngle(float rotation)
    {
        this.rotationAngle = rotation % 360;

        if (this.rotationAngle < 0)
            this.rotationAngle += 360;
    }

    /**
     * Indicate if any change will be made. No changes will be made if all these
     * conditions are met:
     * <ul>
     * <li>Width and height set to zero.
     * <li>Rotation angle of zero degrees;
     * <li>All flippings set to false;
     * </ul>
     * 
     * @return True if the image will change, false if the same image will be
     *         returned.
     */
    public boolean willChange()
    {
        return isFlipHorizontal() || isFlipVertical() || getRotationAngle() > 0
                || getWidth() > 0 || getHeight() > 0;
    }

    /**
     * Return a changed copy of this image, according to this changer
     * configuration. If no change is made, not a copy but the same image will
     * be returned.
     * 
     * @param source The image to change.
     * @return A changed copy of the image or the image itself if not change is
     *         made.
     * @see ImageChanger#willChange()
     */
    public BufferedImage change(BufferedImage source)
    {
        if (source == null)
            throw new IllegalArgumentException("Source cannot be null!");

        if (!willChange())
            return source;

        // Creation of the rescaled/flipped copy
        int newWidth = getWidth() > 0 ? getWidth() : source.getWidth();
        int newHeight = getHeight() > 0 ? getHeight() : source.getHeight();

        BufferedImage newImage = ImageWorker.getInstance()
                .resizeAndFlip(source,
                        newWidth,
                        newHeight,
                        isFlipVertical(),
                        isFlipHorizontal());

        newImage = ImageWorker.getInstance().rotateImage(newImage,
                getRotationAngle());

        return newImage;
    }
}
